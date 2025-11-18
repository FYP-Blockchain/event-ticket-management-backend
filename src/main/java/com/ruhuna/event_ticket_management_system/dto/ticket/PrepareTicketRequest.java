package com.ruhuna.event_ticket_management_system.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigInteger;

@Data
public class PrepareTicketRequest {
    @NotNull(message = "Event ID is required")
    private BigInteger publicEventId;

    private String seat;

    @NotBlank(message = "Initial owner address is required")
    private String initialOwner;
}
