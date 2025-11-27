package com.ruhuna.event_ticket_management_system.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigInteger;

@Data
public class ConfirmTicketRequest {
    @NotBlank(message = "Fabric ticket ID is required")
    private String fabricTicketId;

    @NotBlank(message = "Token ID is required")
    private String tokenId;

    @NotBlank(message = "Transaction hash is required")
    private String transactionHash;

    @NotNull(message = "Event ID is required")
    private BigInteger eventId;

    @NotBlank(message = "IPFS CID is required")
    private String ipfsCid;

    @NotBlank(message = "Seat is required")
    private String seat;
}
