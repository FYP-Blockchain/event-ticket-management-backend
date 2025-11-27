package com.ruhuna.event_ticket_management_system.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruhuna.event_ticket_management_system.contracts.EventManager;
import com.ruhuna.event_ticket_management_system.dto.event.EventMetadata;
import com.ruhuna.event_ticket_management_system.dto.event.EventResponse;
import com.ruhuna.event_ticket_management_system.entity.EventOrganizerAssignment;
import com.ruhuna.event_ticket_management_system.entity.User;
import com.ruhuna.event_ticket_management_system.exception.BadRequestException;
import com.ruhuna.event_ticket_management_system.exception.NotFoundException;
import com.ruhuna.event_ticket_management_system.repository.EventOrganizerAssignmentRepository;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.ruhuna.event_ticket_management_system.utils.ConversionHelper.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {
    private final EventManager eventManager;
    private final IPFSService ipfsService;
    private final CacheManager cacheManager;
    private final EventOrganizerAssignmentRepository eventOrganizerAssignmentRepository;
    private final CurrentUserService currentUserService;
    private final EventSeatService eventSeatService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Disposable eventSubscription;

    @PostConstruct
    public void listenForEvents() {
        log.info("Starting to listen for all events from contract: {}", eventManager.getContractAddress());
        
        // Listen only for new events starting from the latest block to avoid null pointer issues
        // with historical blocks when contracts are redeployed
        Flowable<?> allEvents = Flowable.merge(
                eventManager.eventCreatedEventFlowable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST),
                eventManager.eventDetailsUpdatedEventFlowable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST),
                eventManager.eventDeactivatedEventFlowable(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST)
        );

        eventSubscription = allEvents.subscribe(eventResponse -> {
            try {
                Cache eventsCache = cacheManager.getCache("events");
                if (eventsCache != null) {
                    eventsCache.clear();
                    log.info("All event caches cleared due to a blockchain event.");
                }

                if (eventResponse instanceof EventManager.EventCreatedEventResponse) {
                    handleEventCreated((EventManager.EventCreatedEventResponse) eventResponse);
                } else if (eventResponse instanceof EventManager.EventDetailsUpdatedEventResponse) {
                    handleEventUpdated((EventManager.EventDetailsUpdatedEventResponse) eventResponse);
                } else if (eventResponse instanceof EventManager.EventDeactivatedEventResponse) {
                    handleEventDeactivated((EventManager.EventDeactivatedEventResponse) eventResponse);
                }
            } catch (Exception e) {
                log.error("Error processing event from blockchain", e);
            }
        }, error -> log.error("Error in event subscription stream: ", error));
    }

    private void handleEventCreated(EventManager.EventCreatedEventResponse event) {
        log.info("Listener: New Event Created - ID={}, Name='{}'", event.eventId, event.name);
        getEventDetails(event.eventId);
    }

    private void handleEventUpdated(EventManager.EventDetailsUpdatedEventResponse event) {
        log.info("Listener: Event Details Updated - ID={}", event.eventId);
        cacheManager.getCache("events").evict(event.eventId.toString());
    }

    private void handleEventDeactivated(EventManager.EventDeactivatedEventResponse event) {
        log.info("Listener: Event Deactivated - ID={}", event.eventId);
        cacheManager.getCache("events").evict(event.eventId.toString());
    }

    public List<EventResponse> getAllEvents() {
        final String CACHE_KEY = "allActiveEvents";
        Cache eventsCache = cacheManager.getCache("events");

        List<EventResponse> cachedEvents = eventsCache.get(CACHE_KEY, List.class);
        if (cachedEvents != null) {
            log.info("Cache HIT for all events.");
            return cachedEvents;
        }

        log.info("Cache MISS for all events. Fetching from blockchain...");
        try {
            BigInteger totalEvents = eventManager.nextEventId().send();
            if (totalEvents.compareTo(BigInteger.ONE) <= 0) {
                return new ArrayList<>();
            }

            List<CompletableFuture<EventResponse>> futures = new ArrayList<>();
            for (BigInteger i = BigInteger.ONE; i.compareTo(totalEvents) < 0; i = i.add(BigInteger.ONE)) {
                final BigInteger eventId = i;
                futures.add(CompletableFuture.supplyAsync(() -> getEventDetails(eventId)));
            }

            List<EventResponse> allResponses = futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());

            List<EventResponse> activeEvents = allResponses.stream()
                    .filter(EventResponse::isActive)
                    .collect(Collectors.toList());

            eventsCache.put(CACHE_KEY, activeEvents);

            return activeEvents;

        } catch (Exception e) {
            log.error("Failed to fetch all events from the blockchain", e);
            throw new RuntimeException("Could not retrieve all events: " + e.getMessage(), e);
        }
    }

    @Transactional
    public EventResponse createEvent(String name, String eventDateUTC, Long totalSupply, BigDecimal priceInEther, String description, String eventStartTime, String eventEndTime, String category, String location, MultipartFile imageFile) {
        log.info("Processing createEvent request for '{}'", name);
        try {
            String imageCid = ipfsService.addFile(imageFile);
            String imageUrl = ipfsService.getFullUrl("ipfs://" + imageCid);
            log.info("Uploaded event image to IPFS. Image URL: {}", imageUrl);
            EventMetadata metadata = EventMetadata.builder()
                    .description(description)
                    .image(imageUrl)
                    .location(location)
                    .eventStartTime(eventStartTime)
                    .eventEndTime(eventEndTime)
                    .category(category)
                    .build();

            String metadataJson = objectMapper.writeValueAsString(metadata);
            String metadataCid = ipfsService.addJson(metadataJson);
            String metadataURI = "ipfs://" + metadataCid;
            log.info("Uploaded event metadata to IPFS. Metadata URI: {}", metadataURI);

            TransactionReceipt txReceipt = eventManager.createEvent(
                    name,
                    stringUTC2Timestamp(eventDateUTC),
                    BigInteger.valueOf(totalSupply),
                    convertEtherToWei(priceInEther),
                    metadataURI
            ).send();

            List<EventManager.EventCreatedEventResponse> events = EventManager.getEventCreatedEvents(txReceipt);
            if (events.isEmpty()) {
                throw new RuntimeException("Failed to find EventCreated event log in transaction receipt.");
            }
            BigInteger eventId = events.get(0).eventId;
            log.info("Transaction successful. Tx Hash: {}, New Event ID: {}", txReceipt.getTransactionHash(), eventId);

            // Initialize seats for the event
            try {
                eventSeatService.initializeSeatsForEvent(eventId, totalSupply.intValue());
                log.info("Successfully initialized {} seats for event ID: {}", totalSupply, eventId);
            } catch (Exception seatError) {
                log.error("Failed to initialize seats for event ID: {}. Error: {}", eventId, seatError.getMessage());
                // Don't fail the entire event creation if seat initialization fails
                // Seats can be initialized later via the API endpoint
            }

            EventResponse eventResponse = getEventDetails(eventId);
            registerEventOwnership(eventId, eventResponse.getOrganizerAddress());
            return eventResponse;

        } catch (Exception e) {
            log.error("Failed to create event via contract", e);
            throw new RuntimeException("Failed to create event: " + e.getMessage(), e);
        }
    }

    @Transactional
    public EventResponse updateEventDetails(BigInteger eventId, String name, String eventDateUTC, Long totalSupply, BigDecimal priceInEther, String description, String eventStartTime, String eventEndTime, String category, String location, MultipartFile imageFile) {
        log.info("Processing updateEventDetails for event ID: {}", eventId);
        assertCurrentUserOwnsEvent(eventId);
        try {
            // Check if a new image was provided
            String imageUrl;
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageCid = ipfsService.addFile(imageFile);
                imageUrl = ipfsService.getFullUrl("ipfs://" + imageCid);
                log.info("Uploaded updated event image to IPFS. New Image URL: {}", imageUrl);
            } else {
                EventManager.Event existingEvent = eventManager.getEventDetails(eventId).send();
                String cid = existingEvent.metadataURI.replace("ipfs://", "");
                byte[] metadataBytes = ipfsService.getFile(cid);
                EventMetadata oldMetadata = objectMapper.readValue(metadataBytes, EventMetadata.class);
                imageUrl = oldMetadata.getImage();
            }

            EventMetadata metadata = EventMetadata.builder()
                    .description(description)
                    .image(imageUrl)
                    .location(location)
                    .eventStartTime(eventStartTime)
                    .eventEndTime(eventEndTime)
                    .category(category)
                    .build();

            String metadataJson = objectMapper.writeValueAsString(metadata);
            String newMetadataCid = ipfsService.addJson(metadataJson);
            String newMetadataURI = "ipfs://" + newMetadataCid;
            log.info("Uploaded updated event metadata to IPFS. New Metadata URI: {}", newMetadataURI);

            TransactionReceipt txReceipt = eventManager.updateEventDetails(
                    eventId,
                    name,
                    stringUTC2Timestamp(eventDateUTC),
                    newMetadataURI,
                    BigInteger.valueOf(totalSupply),
                    convertEtherToWei(priceInEther)
            ).send();

            log.info("Update transaction successful. Tx Hash: {}", txReceipt.getTransactionHash());

            Cache eventsCache = cacheManager.getCache("events");
            if (eventsCache != null) {
                eventsCache.evict(eventId.toString());
            }

            return getEventDetails(eventId);

        } catch (Exception e) {
            log.error("Failed to update event {} via contract", eventId, e);
            throw new RuntimeException("Failed to update event: " + e.getMessage(), e);
        }
    }

    @Transactional
    public String deactivateEvent(BigInteger eventId) {
        log.info("Submitting deactivateEvent transaction for event ID: {}", eventId);
        assertCurrentUserOwnsEvent(eventId);
        try {
            TransactionReceipt txReceipt = eventManager.deactivateEvent(eventId).send();
            log.info("Deactivation transaction successful. Tx Hash: {}", txReceipt.getTransactionHash());

            // Invalidate the cache for this event and the list of all events
            Cache eventsCache = cacheManager.getCache("events");
            if (eventsCache != null) {
                eventsCache.evict(eventId.toString());
                eventsCache.evict("allActiveEvents"); // Evict the cached list
            }

            return "Event with ID " + eventId + " deactivated successfully.";

        } catch (Exception e) {
            log.error("Failed to deactivate event via contract", e);
            throw new RuntimeException("Failed to deactivate event: " + e.getMessage(), e);
        }
    }

    @Transactional
    public EventResponse registerSelfCustodyEvent(BigInteger eventId) {
        log.info("Registering self-custody event {} for current organizer", eventId);
        
        // Clear cache before fetching to ensure fresh data from blockchain
        Cache eventsCache = cacheManager.getCache("events");
        if (eventsCache != null) {
            eventsCache.evict(eventId.toString());
            eventsCache.evict("allActiveEvents");
        }
        
        EventResponse eventResponse = getEventDetails(eventId);
        registerEventOwnership(eventId, eventResponse.getOrganizerAddress());
        return eventResponse;
    }

    public EventResponse getEventDetails(BigInteger eventId) {
        String eventIdStr = eventId.toString();
        EventResponse cachedEvent = cacheManager.getCache("events").get(eventIdStr, EventResponse.class);
        if (cachedEvent != null) {
            log.info("Cache HIT for event ID: {}", eventIdStr);
            return cachedEvent;
        }

        log.info("Cache MISS for event ID: {}. Fetching from blockchain...", eventIdStr);
        try {
            EventManager.Event event = eventManager.getEventDetails(eventId).send();
            if (event.id.equals(BigInteger.ZERO)) {
                throw new NotFoundException("Event with ID " + eventId + " not found on blockchain.");
            }

            EventResponse eventResponse = toEventResponse(event);
            cacheManager.getCache("events").put(eventIdStr, eventResponse);
            return eventResponse;
        } catch (Exception e) {
            log.error("Error fetching event {} from blockchain: {}", eventId, e.getMessage());
            throw new RuntimeException("Could not fetch event details: " + e.getMessage());
        }
    }

    private EventResponse toEventResponse(EventManager.Event event) {
        EventResponse.EventResponseBuilder builder = EventResponse.builder()
                .id(event.id.toString())
                .name(event.name)
                .organizerAddress(event.organizer)
                .eventDate(toLocalDateTime(event.eventDate))
                .totalSupply(event.totalSupply.longValue())
                .priceInWei(event.ticketPrice)
                .metadataURI(event.metadataURI)
                .active(event.isActive);

        // Fetch and combine metadata from IPFS
        try {
            String cid = event.metadataURI.replace("ipfs://", "");
            byte[] metadataBytes = ipfsService.getFile(cid);
            EventMetadata metadata = objectMapper.readValue(metadataBytes, EventMetadata.class);

            builder.description(metadata.getDescription())
                    .imageUrl(metadata.getImage()) // Assuming image is a full URL
                    .location(metadata.getLocation())
                    .eventStartTime(metadata.getEventStartTime())
                    .eventEndTime(metadata.getEventEndTime())
                    .category(metadata.getCategory());

        } catch (Exception e) {
            log.error("Failed to fetch or parse metadata from IPFS for CID: {}", event.metadataURI, e);
            // Continue without the IPFS data if it fails
        }

        return builder.build();
    }

    public List<EventResponse> getEventsForCurrentOrganizer() {
        User currentUser = currentUserService.getCurrentUser();
        log.info("Fetching events for organizer user id={}", currentUser.getId());

        List<EventOrganizerAssignment> assignments = eventOrganizerAssignmentRepository.findAllByOrganizer_Id(currentUser.getId());
        if (assignments.isEmpty()) {
            return Collections.emptyList();
        }

        List<CompletableFuture<EventResponse>> futures = new ArrayList<>();
        for (EventOrganizerAssignment assignment : assignments) {
            try {
                BigInteger eventId = new BigInteger(assignment.getEventId());
                futures.add(CompletableFuture.supplyAsync(() -> {
                    try {
                        return getEventDetails(eventId);
                    } catch (Exception ex) {
                        log.warn("Event {} from DB does not exist on blockchain (possibly redeployed). Skipping.", eventId, ex);
                        return null;
                    }
                }));
            } catch (NumberFormatException ex) {
                log.warn("Skipping assignment {} due to invalid event id {}", assignment.getId(), assignment.getEventId());
            }
        }

        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void evictEventCache(BigInteger eventId) {
        log.info("Evicting cache for event ID: {}", eventId);
        Cache eventsCache = cacheManager.getCache("events");
        if (eventsCache != null) {
            eventsCache.evict(eventId.toString());
            eventsCache.evict("allActiveEvents");
        }
    }

    @PreDestroy
    public void cleanup() {
        if (eventSubscription != null && !eventSubscription.isDisposed()) {
            log.info("Disposing Ethereum event subscription.");
            eventSubscription.dispose();
        }
    }

    private void registerEventOwnership(BigInteger eventId, String organizerAddress) {
        User currentUser = currentUserService.getCurrentUser();
        String eventIdString = normalizeEventId(eventId);
        String normalizedAddress = normalizeAddress(organizerAddress);

        eventOrganizerAssignmentRepository.findByEventId(eventIdString).ifPresentOrElse(existing -> {
            if (!existing.getOrganizer().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("This event is already linked to another organizer.");
            }
            if (!Objects.equals(existing.getOrganizerWalletAddress(), normalizedAddress)) {
                existing.setOrganizerWalletAddress(normalizedAddress);
                eventOrganizerAssignmentRepository.save(existing);
            }
        }, () -> {
            EventOrganizerAssignment assignment = EventOrganizerAssignment.builder()
                    .eventId(eventIdString)
                    .organizer(currentUser)
                    .organizerWalletAddress(normalizedAddress)
                    .build();
            eventOrganizerAssignmentRepository.save(assignment);
        });
    }

    private void assertCurrentUserOwnsEvent(BigInteger eventId) {
        User currentUser = currentUserService.getCurrentUser();
        EventOrganizerAssignment assignment = eventOrganizerAssignmentRepository.findByEventId(normalizeEventId(eventId))
                .orElseThrow(() -> new BadRequestException("This event is not registered under your account."));
        if (!assignment.getOrganizer().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to modify this event.");
        }
    }

    private String normalizeEventId(BigInteger eventId) {
        return eventId.toString();
    }

    private String normalizeAddress(String address) {
        return address == null ? null : address.toLowerCase();
    }
}