package com.ruhuna.event_ticket_management_system.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class TicketRequest {
    private String publicEventId;
    private String seat;
    private String secretNonce;
    private String initialOwner;
}

