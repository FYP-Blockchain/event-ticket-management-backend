package com.ruhuna.event_ticket_management_system.service;

import com.ruhuna.event_ticket_management_system.dto.payment.PaymentIntentRequest;
import com.ruhuna.event_ticket_management_system.entity.PaymentProviderType;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;

@Service("STRIPE")
@RequiredArgsConstructor
@Slf4j
public class StripePaymentProvider implements PaymentProvider {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    private static final String CURRENCY = "LKR";

    private final EventService eventService;

    @Override
    public String createPaymentIntent(PaymentIntentRequest request) {
        try {
            Stripe.apiKey = stripeSecretKey;
            BigInteger priceInWei = eventService.getEventDetails(request.getEventId()).getPriceInWei();

            //Price conversion should be done using public API
            Long priceInCents = Math.round(priceInWei.doubleValue() * 0.000001);

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(priceInCents)
                    .setCurrency(CURRENCY)
                    .build();


            PaymentIntent intent = PaymentIntent.create(params);
            return intent.getClientSecret();

        } catch (StripeException ex) {
            log.error("Payment intent creation not successful: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getStripeError().getMessage());
        }
    }

    @Override
    public void verifyPayment(String paymentIntentId) {
        try {
            Stripe.apiKey = stripeSecretKey;
            PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);

            if (!"succeeded".equals(intent.getStatus())) {
                log.warn("Payment intent verification not successful: {}", intent.getStatus());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Payment not successful. Status: " + intent.getStatus());
            }

            log.info("Payment verified successfully: {}", intent.getId());
        } catch (StripeException ex) {
            log.error("Payment verification failed: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getStripeError().getMessage());
        }
    }

    @Override
    public String getProviderName() {
        return PaymentProviderType.STRIPE.name();
    }
}
