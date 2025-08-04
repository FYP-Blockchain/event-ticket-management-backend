package com.ruhuna.event_ticket_management_system.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@RequiredArgsConstructor
public class TicketRequest {
    private BigInteger publicEventId;
    private String seat;
    private String secretNonce;
    private String initialOwner;
}

