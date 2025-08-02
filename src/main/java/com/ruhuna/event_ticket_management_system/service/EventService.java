package com.ruhuna.event_ticket_management_system.service;

import com.ruhuna.event_ticket_management_system.contracts.EventManager;
import com.ruhuna.event_ticket_management_system.dto.event.CreateEventRequest;
import com.ruhuna.event_ticket_management_system.dto.event.EventResponse;
import com.ruhuna.event_ticket_management_system.exception.NotFoundException;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

import static com.ruhuna.event_ticket_management_system.utils.ConversionHelper.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {
    private final EventManager eventManager;
    private final IPFSService ipfsService;
    private final CacheManager cacheManager;

    private Disposable eventSubscription;

    @PostConstruct
    public void listenForEvents() {
        log.info("Starting to listen for all events from contract: {}", eventManager.getContractAddress());
        Flowable<?> allEvents = Flowable.merge(
                eventManager.eventCreatedEventFlowable(null, null),
                eventManager.eventDetailsUpdatedEventFlowable(null, null),
                eventManager.eventDeactivatedEventFlowable(null, null)
        );

        eventSubscription = allEvents.subscribe(eventResponse -> {
            try {
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

        EventResponse eventResponse = EventResponse.builder()
                .id(event.eventId.toString())
                .name(event.name)
                .organizerAddress(event.organizer)
                .eventDate(toLocalDateTime(event.eventDate))
                .totalSupply(event.totalSupply.longValue())
                .priceInEther(event.ticketPrice)
                .metadataURI(event.metadataURI)
                .imageUrl(ipfsService.getFullUrl(event.metadataURI)) // In here we should get imageUrl by metadata object
                .active(true)
                .build();

        cacheManager.getCache("events").put(event.eventId.toString(), eventResponse);
        log.info("Cache updated for new event ID: {}", event.eventId);
    }

    private void handleEventUpdated(EventManager.EventDetailsUpdatedEventResponse event) {
        log.info("Listener: Event Details Updated - ID={}", event.eventId);
        cacheManager.getCache("events").evict(event.eventId.toString());
        log.info("Cache evicted for updated event ID: {}", event.eventId);
    }

    private void handleEventDeactivated(EventManager.EventDeactivatedEventResponse event) {
        log.info("Listener: Event Deactivated - ID={}", event.eventId);
        cacheManager.getCache("events").evict(event.eventId.toString());
        log.info("Caches evicted for deactivated event ID: {}", event.eventId);
    }

    public String createEvent(CreateEventRequest request) {
        log.info("Submitting createEvent transaction for '{}'", request.getName());
        try {
            TransactionReceipt txReceipt = eventManager.createEvent(
                    request.getName(),
                    stringUTC2Timestamp(request.getEventDateUTC()),
                    BigInteger.valueOf(request.getTotalSupply()),
                    convertEtherToWei(request.getPriceInEther()),
                    request.getMetadataURI()
            ).send();
            log.info("Transaction successful. Tx Hash: {}", txReceipt.getTransactionHash());
            return txReceipt.getTransactionHash();
        } catch (Exception e) {
            log.error("Failed to create event via contract", e);
            throw new RuntimeException("Failed to create event: " + e.getMessage(), e);
        }
    }

    public String deactivateEvent(BigInteger eventId) {
        log.info("Submitting deactivateEvent transaction for event ID: {}", eventId);

        try {
            TransactionReceipt txReceipt = eventManager.deactivateEvent(eventId).send();
            log.info("Deactivation transaction successful. Tx Hash: {}", txReceipt.getTransactionHash());
            return txReceipt.getTransactionHash();
        } catch (Exception e) {
            log.error("Failed to deactivate event via contract", e);
            throw new RuntimeException("Failed to deactivate event: " + e.getMessage(), e);
        }
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
        return EventResponse.builder()
                .id(event.id.toString())
                .name(event.name)
                .organizerAddress(event.organizer)
                .eventDate(toLocalDateTime(event.eventDate))
                .totalSupply(event.totalSupply.longValue())
                .priceInEther(event.ticketPrice)
                .metadataURI(event.metadataURI)
                .imageUrl(ipfsService.getFullUrl(event.metadataURI))
                .active(event.isActive)
                .build();
    }

    @PreDestroy
    public void cleanup() {
        if (eventSubscription != null && !eventSubscription.isDisposed()) {
            log.info("Disposing Ethereum event subscription.");
            eventSubscription.dispose();
        }
    }
}

