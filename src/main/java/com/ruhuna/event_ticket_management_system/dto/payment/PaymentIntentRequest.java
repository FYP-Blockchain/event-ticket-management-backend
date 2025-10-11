package com.ruhuna.event_ticket_management_system.dto.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigInteger;

@Getter
public class PaymentIntentRequest {

    @NotNull(message = "Event Id cannot be blank")
    private BigInteger eventId;
}
