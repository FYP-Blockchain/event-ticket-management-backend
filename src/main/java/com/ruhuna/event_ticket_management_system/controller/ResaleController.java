package com.ruhuna.event_ticket_management_system.controller;

import com.ruhuna.event_ticket_management_system.dto.resale.BuyResaleTicketRequest;
import com.ruhuna.event_ticket_management_system.dto.resale.ListForResaleRequest;
import com.ruhuna.event_ticket_management_system.dto.resale.ResaleInfoResponse;
import com.ruhuna.event_ticket_management_system.dto.resale.ResaleListingResponse;
import com.ruhuna.event_ticket_management_system.service.ResaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resale")
@RequiredArgsConstructor
public class ResaleController {

    private final ResaleService resaleService;

    /**
     * Prepare listing a ticket for resale (validates and updates off-chain ledger)
     * Frontend will call this before sending blockchain transaction
     */
    @PostMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResaleInfoResponse> listForResale(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ListForResaleRequest request) {
        ResaleInfoResponse response = resaleService.listForResale(request, userDetails);
        return ResponseEntity.ok(response);
    }

    /**
     * Unlist a ticket from resale
     */
    @PostMapping("/unlist")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> unlistFromResale(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String tokenId,
            @RequestParam String ownerAddress) {
        resaleService.unlistFromResale(tokenId, ownerAddress, userDetails);
        return ResponseEntity.ok().build();
    }

    /**
     * Confirm resale purchase after blockchain transaction
     */
    @PostMapping("/confirm-purchase")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> confirmResalePurchase(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BuyResaleTicketRequest request) {
        resaleService.confirmResalePurchase(request, userDetails);
        return ResponseEntity.ok().build();
    }

    /**
     * Get resale info for a specific ticket
     */
    @GetMapping("/info/{tokenId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResaleInfoResponse> getResaleInfo(@PathVariable String tokenId) {
        ResaleInfoResponse response = resaleService.getResaleInfo(tokenId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all tickets listed for resale for a specific event
     */
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<ResaleListingResponse>> getResaleListingsForEvent(
            @PathVariable BigInteger eventId) {
        List<ResaleListingResponse> listings = resaleService.getResaleListingsForEvent(eventId);
        return ResponseEntity.ok(listings);
    }

    /**
     * Check if user is registered for resale
     * Note: With the current design, all platform users are considered registered
     * since we removed on-chain registration in favor of blocking direct transfers
     */
    @GetMapping("/user/registered")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Boolean>> isUserRegistered(@RequestParam String walletAddress) {
        boolean isRegistered = resaleService.isUserRegistered(walletAddress);
        return ResponseEntity.ok(Map.of("isRegistered", isRegistered));
    }
}
