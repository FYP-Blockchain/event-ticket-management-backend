package com.ruhuna.event_ticket_management_system.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecodedQrPayload {
    private String tokenId;
    private String fabricTicketId;
    private String secretNonce;
    private String ipfsCid;
    private long timestamp;
}
