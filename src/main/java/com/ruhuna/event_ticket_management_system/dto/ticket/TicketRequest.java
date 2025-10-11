package com.ruhuna.event_ticket_management_system.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class TicketRequest {
    @NotNull(message = "publicEventId cannot be null")
    private BigInteger publicEventId;

    private String seat;

    @NotBlank(message = "initialOwner cannot be blank")
    private String initialOwner;

    @NotBlank(message = "Payment Intent Id cannot be blank")
    private String paymentIntentId;
}

