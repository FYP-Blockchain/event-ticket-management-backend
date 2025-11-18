package com.ruhuna.event_ticket_management_system.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfirmTicketRequest {
    @NotBlank(message = "Fabric ticket ID is required")
    private String fabricTicketId;

    @NotBlank(message = "Token ID is required")
    private String tokenId;

    @NotBlank(message = "Transaction hash is required")
    private String transactionHash;
}
