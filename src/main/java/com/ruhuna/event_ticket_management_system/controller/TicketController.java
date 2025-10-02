package com.ruhuna.event_ticket_management_system.controller;

import com.ruhuna.event_ticket_management_system.dto.ticket.TicketPurchaseResponse;
import com.ruhuna.event_ticket_management_system.dto.ticket.TicketRequest;
import com.ruhuna.event_ticket_management_system.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TicketPurchaseResponse> createTicket(@AuthenticationPrincipal UserDetails userDetails,
                                               @Valid @RequestBody TicketRequest request) throws Exception {
        TicketPurchaseResponse response = ticketService.createAndIssueTicket(request, userDetails);
        return ResponseEntity.ok(response);
    }
}
