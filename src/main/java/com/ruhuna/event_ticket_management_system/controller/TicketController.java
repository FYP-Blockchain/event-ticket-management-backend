package com.ruhuna.event_ticket_management_system.controller;

import com.ruhuna.event_ticket_management_system.dto.ticket.TicketRequest;
import com.ruhuna.event_ticket_management_system.service.TicketService;
import jakarta.validation.Valid;
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
    public ResponseEntity<String> createTicket(@Valid @RequestBody TicketRequest request) throws Exception {
            ticketService.createAndIssueTicket(request);
            return ResponseEntity.ok("Ticket creation and issuance process started successfully.");
    }
}
