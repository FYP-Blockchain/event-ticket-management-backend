package com.ruhuna.event_ticket_management_system.service;

import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.client.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartUpRunner implements CommandLineRunner {

    private final Contract contract;
    private final EventService eventService;

    @Override
    public void run(String... args) {
        try {
            System.out.println("--> Checking if ledger is already initialized...");
            contract.evaluateTransaction("readTicket", "TICKET0");
            System.out.println("Ledger already initialized. Skipping InitLedger.");
        } catch (Exception e) {
            System.out.println("Ledger not initialized or TICKET0 not found. Attempting InitLedger...");

            try {
                contract.submitTransaction("initLedger");
                System.out.println("InitLedger submitted successfully.");
            } catch (Exception initEx) {
                System.err.println("Error submitting InitLedger: " + initEx.getMessage());
                // Don't rethrow this, let the application continue
            }
        }

        try {
            System.out.println("--> Optionally create a test event here...");
            // eventService.createStaticTestEvent();
            System.out.println("*** Startup completed");
        } catch (Exception e) {
            System.err.println("Error creating test event: " + e.getMessage());
        }
    }
}
