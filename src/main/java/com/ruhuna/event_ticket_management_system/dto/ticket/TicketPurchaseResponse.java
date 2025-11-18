package com.ruhuna.event_ticket_management_system.dto.ticket;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketPurchaseResponse {
    private String tokenId;
    private String fabricTicketId;
    private String transactionHash;
    private String status;
}
