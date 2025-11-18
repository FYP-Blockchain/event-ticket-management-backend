package com.ruhuna.event_ticket_management_system.dto.ticket;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificationResponse {
    private boolean success;
    private String errorCode;
    private String tokenId;
    private String eventId;
    private String seat;
    private String message;
    private long verificationDurationMs;
}
