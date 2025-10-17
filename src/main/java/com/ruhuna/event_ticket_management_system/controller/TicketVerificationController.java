package com.ruhuna.event_ticket_management_system.controller;

import com.ruhuna.event_ticket_management_system.dto.ticket.VerificationRequest;
import com.ruhuna.event_ticket_management_system.dto.ticket.VerificationResponse;
import com.ruhuna.event_ticket_management_system.service.TicketVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verification")
public class TicketVerificationController {

    private final TicketVerificationService ticketVerificationService;

    @PostMapping("/verify")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<VerificationResponse> verifyTicket(@Valid @RequestBody VerificationRequest request) {
        VerificationResponse response = ticketVerificationService.verifyTicket(request);
        return ResponseEntity.ok(response);
    }
}
