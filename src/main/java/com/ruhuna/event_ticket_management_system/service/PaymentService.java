package com.ruhuna.event_ticket_management_system.service;

import com.ruhuna.event_ticket_management_system.dto.payment.PaymentIntentRequest;
import com.ruhuna.event_ticket_management_system.entity.PaymentProviderType;
import com.ruhuna.event_ticket_management_system.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class PaymentService {

    private final Map<String, PaymentProvider> providerMap = new ConcurrentHashMap<>();

    public PaymentService(List<PaymentProvider> providers) {
        for (PaymentProvider provider : providers) {
            log.info("Registering payment provider: {}", provider.getProviderName());
            providerMap.put(provider.getProviderName(), provider);
        }
    }

    public String createPaymentIntent(PaymentIntentRequest request) {
        PaymentProvider provider = getProvider(request.getProvider());
        return provider.createPaymentIntent(request);
    }

    public void verifyPayment(String paymentReferenceId, PaymentProviderType providerEnum) {
        PaymentProvider provider = getProvider(providerEnum);
        provider.verifyPayment(paymentReferenceId);
    }

    private PaymentProvider getProvider(PaymentProviderType providerEnum) {
        PaymentProvider provider = providerMap.get(providerEnum.name());
        if (provider == null) {
            log.error("Implementation for payment provider {} not found.", providerEnum);
            throw new NotFoundException("Payment provider implementation not found for: " + providerEnum);
        }
        return provider;
    }
}
