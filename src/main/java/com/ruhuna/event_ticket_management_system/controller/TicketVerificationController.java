package com.ruhuna.event_ticket_management_system.controller;

import com.ruhuna.event_ticket_management_system.dto.ticket.QrDataResponse;
import com.ruhuna.event_ticket_management_system.dto.ticket.VerificationRequest;
import com.ruhuna.event_ticket_management_system.dto.ticket.VerificationResponse;
import com.ruhuna.event_ticket_management_system.service.TicketService;
import com.ruhuna.event_ticket_management_system.service.TicketVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verification")
public class TicketVerificationController {

    private final TicketVerificationService ticketVerificationService;
    private final TicketService ticketService;

    @PostMapping("/verify")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<VerificationResponse> verifyTicket(@Valid @RequestBody VerificationRequest request) {
        VerificationResponse response = ticketVerificationService.verifyTicket(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{tokenId}/qrData")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<QrDataResponse> getQrDataForTicket(@PathVariable String tokenId, @RequestParam String walletId) {
        QrDataResponse qrData = ticketVerificationService.getQrData(tokenId, walletId);
        return ResponseEntity.ok(qrData);
    }
}
