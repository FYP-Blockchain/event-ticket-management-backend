package com.ruhuna.event_ticket_management_system.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QrDataResponse {

    private String tokenId;
    private String fabricTicketId;
    private String secretNonce;
}
