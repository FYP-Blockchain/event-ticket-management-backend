package com.ruhuna.event_ticket_management_system.controller;

import com.ruhuna.event_ticket_management_system.dto.payment.PaymentIntentRequest;
import com.ruhuna.event_ticket_management_system.service.PaymentService;
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
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/createIntent")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> createPaymentIntent(@Valid @RequestBody PaymentIntentRequest request) {
        String clientSecret = paymentService.createPaymentIntent(request);
        return ResponseEntity.ok(clientSecret);
    }
}
