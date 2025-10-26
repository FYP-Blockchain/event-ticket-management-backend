package com.ruhuna.event_ticket_management_system.dto.ticket;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificationResponse {
    private boolean success;
    private String tokenId;
    private String eventId;
    private String errorCode;
    private String seat;
    private String ownerAddress;
    private String purchasedBy;
    private String message;
    private long verificationTime;
    private long verificationDurationMs;
}
