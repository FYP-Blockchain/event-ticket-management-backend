package com.ruhuna.event_ticket_management_system.dto.event;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class SelfCustodyEventRegistrationRequest {

    @NotNull(message = "eventId is required")
    private BigInteger eventId;
}
