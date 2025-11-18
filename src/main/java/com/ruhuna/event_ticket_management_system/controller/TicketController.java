package com.ruhuna.event_ticket_management_system.controller;

import com.ruhuna.event_ticket_management_system.dto.ticket.*;
import com.ruhuna.event_ticket_management_system.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("/prepare")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PrepareTicketResponse> prepareTicket(@AuthenticationPrincipal UserDetails userDetails,
                                                               @Valid @RequestBody PrepareTicketRequest request) {
        Map<String, String> result = ticketService.prepareTicketForCryptoPurchase(
            request.getPublicEventId(),
            request.getSeat(),
            request.getInitialOwner(),
            userDetails.getUsername()
        );
        
        PrepareTicketResponse response = PrepareTicketResponse.builder()
            .fabricTicketId(result.get("fabricTicketId"))
            .ipfsCid(result.get("ipfsCid"))
            .commitmentHash(result.get("commitmentHash"))
            .tokenId(result.get("tokenId"))
            .build();
            
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> confirmTicket(@Valid @RequestBody ConfirmTicketRequest request) {
        ticketService.confirmCryptoPurchase(
            request.getFabricTicketId(),
            request.getTokenId(),
            request.getTransactionHash()
        );
        return ResponseEntity.ok().build();
    }
}
