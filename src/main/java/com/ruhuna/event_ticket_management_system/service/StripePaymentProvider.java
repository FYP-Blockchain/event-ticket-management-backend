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
            
            log.info("Creating payment intent for event ID: {}", request.getEventId());
            
            BigInteger priceInWei = eventService.getEventDetails(request.getEventId()).getPriceInWei();

            // Convert Wei to ETH: 1 ETH = 10^18 Wei
            // Assuming ticket prices are set in a reasonable ETH amount on blockchain
            // Example: 0.01 ETH = 10^16 Wei
            double priceInEth = priceInWei.doubleValue() / 1_000_000_000_000_000_000.0; // 10^18
            
            // Convert ETH to LKR using approximate exchange rate
            // TODO: Use a real-time exchange rate API for production
            double ethToLkrRate = 500000.0; // Example: 1 ETH ≈ 500,000 LKR (update this!)
            double priceInLkr = priceInEth * ethToLkrRate;
            
            // Convert LKR to cents (smallest currency unit for Stripe)
            Long priceInCents = Math.round(priceInLkr * 100);
            
            log.info("Price calculated: {} wei = {} ETH = {} LKR = {} cents", 
                priceInWei, priceInEth, priceInLkr, priceInCents);
            
            // Validate against Stripe's LKR limit (999,999.99 LKR = 99,999,999 cents)
            if (priceInCents > 99_999_999) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    String.format("Price exceeds Stripe limit. Max: 999,999.99 LKR, Requested: %.2f LKR", 
                        priceInCents / 100.0));
            }

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(priceInCents)
                    .setCurrency(CURRENCY)
                    .build();


            PaymentIntent intent = PaymentIntent.create(params);
            log.info("Payment intent created successfully: {}", intent.getId());
            return intent.getClientSecret();

        } catch (StripeException ex) {
            log.error("Payment intent creation not successful: {}", ex.getMessage(), ex);
            String errorMessage = ex.getStripeError() != null 
                ? ex.getStripeError().getMessage() 
                : ex.getMessage();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        } catch (Exception ex) {
            log.error("Unexpected error during payment intent creation", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Payment intent creation failed: " + ex.getMessage());
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
            log.error("Payment verification failed: {}", ex.getMessage(), ex);
            String errorMessage = ex.getStripeError() != null 
                ? ex.getStripeError().getMessage() 
                : ex.getMessage();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }

    @Override
    public String getProviderName() {
        return PaymentProviderType.STRIPE.name();
    }
}
