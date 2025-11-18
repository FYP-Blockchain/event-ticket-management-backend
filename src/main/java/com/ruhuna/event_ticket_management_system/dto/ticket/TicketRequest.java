package com.ruhuna.event_ticket_management_system.dto.ticket;

import com.ruhuna.event_ticket_management_system.entity.PaymentProviderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequest {
    @NotNull(message = "publicEventId cannot be null")
    private BigInteger publicEventId;

    private String seat;

    @NotBlank(message = "initialOwner cannot be blank")
    private String initialOwner;

    private String paymentIntentId;

    private PaymentProviderType provider = PaymentProviderType.STRIPE;
}

