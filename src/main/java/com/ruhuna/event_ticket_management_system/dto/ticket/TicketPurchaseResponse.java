package com.ruhuna.event_ticket_management_system.dto.ticket;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
public class TicketPurchaseResponse {

    private BigInteger tokenId;
    private String fabricTicketId;
    private BigInteger eventId;
    private String seat;
    private String ownerAddress;
    private String ipfsCid;
    private BigInteger pricePaid;
    private String transactionHash;
    private String status;
    private Long timestamp;
}
