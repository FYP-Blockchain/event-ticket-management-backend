package com.ruhuna.event_ticket_management_system.service;

import com.ruhuna.event_ticket_management_system.contracts.EventManager;
import io.reactivex.disposables.Disposable;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final EventManager eventManager;

    private Disposable eventSubscription;

    @PostConstruct
    public void listenForEventCreations() {
        log.info("Starting to listen for EventCreated events from contract: {}", eventManager.getContractAddress());
        // Should update necessary event info, better to maintain cache for events
        eventSubscription = eventManager.eventCreatedEventFlowable(null, null)
                .subscribe(
                        eventResponse -> {
                            BigInteger publicEventId = eventResponse.eventId;
                            String eventName = eventResponse.name;
                            log.info("Received EventCreated: ID={}, Name='{}'", publicEventId, eventName);
                        },
                        error -> log.error("Error listening to EventCreated events: ", error),
                        () -> log.info("EventCreated event stream completed.")
                );
    }

    // This method is for testing purposes only, to create a static event
    public void createStaticTestEvent() {
        try {
            String name = "Blockchain Conference";
            BigInteger eventDate = BigInteger.valueOf(System.currentTimeMillis() / 1000 + 3600);
            BigInteger totalSupply = BigInteger.valueOf(100);
            BigInteger ticketPrice = BigInteger.valueOf(10_000_000_000_000_000L);
            String metadataURI = "ipfs://example-ipfs-metadata-uri";

            log.info("Calling createEvent() with static test values...");

            var txReceipt = eventManager
                    .createEvent(name, eventDate, totalSupply, ticketPrice, metadataURI)
                    .send();

            log.info("Transaction successful. Tx Hash: {}", txReceipt.getTransactionHash());

        } catch (Exception e) {
            log.error("Failed to create event via contract", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        if (eventSubscription != null && !eventSubscription.isDisposed()) {
            log.info("Disposing Ethereum event subscription.");
            eventSubscription.dispose();
        }
    }
}

