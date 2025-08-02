package com.ruhuna.event_ticket_management_system.controller;

import com.ruhuna.event_ticket_management_system.dto.TicketRequest;
import com.ruhuna.event_ticket_management_system.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/createTicket")
    public ResponseEntity<String> createTicket(@RequestBody TicketRequest request) {
        try {
            String result = ticketService.createTicketAndQueueForPublicPublishing(
                    request.getPublicEventId(),
                    request.getSeat(),
                    request.getSecretNonce(),
                    request.getInitialOwner()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
