package com.ruhuna.event_ticket_management_system.dto.payment;

import com.ruhuna.event_ticket_management_system.entity.PaymentProviderType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIntentRequest {

    @NotNull(message = "Event Id cannot be blank")
    private BigInteger eventId;

    private PaymentProviderType provider = PaymentProviderType.STRIPE;
}
