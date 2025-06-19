package com.ruhuna.event_ticket_management_system.service;

import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.client.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// This is for initial testing purposes only, we can get rid of this when doing further implementations
@Component
@RequiredArgsConstructor
public class StartUpRunner implements CommandLineRunner {

    private final Contract contract;

    @Override
    public void run(String... args) throws EndorseException, CommitException, SubmitException, CommitStatusException {

        System.out.println("\n--> Submit Transaction: InitLedger, function creates the initial set of tickets on the ledger");

        contract.submitTransaction("initLedger");

        System.out.println("*** Transaction committed successfully");
    }
}
