package com.ruhuna.event_ticket_management_system.dto.ticket;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
public class TicketPurchaseResponse {
    private BigInteger tokenId;
    private String fabricTicketId;
    private String transactionHash;
    private String status;
}
