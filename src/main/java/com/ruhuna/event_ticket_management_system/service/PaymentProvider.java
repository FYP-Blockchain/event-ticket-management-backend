package com.ruhuna.event_ticket_management_system.service;

import com.ruhuna.event_ticket_management_system.dto.payment.PaymentIntentRequest;

public interface PaymentProvider {

    String createPaymentIntent(PaymentIntentRequest request);

    void verifyPayment(String paymentRefId);

    String getProviderName();
}