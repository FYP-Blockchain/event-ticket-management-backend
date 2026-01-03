package com.ruhuna.event_ticket_management_system.dto.resale;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyResaleTicketRequest {
    
    @NotBlank(message = "Token ID is required")
    private String tokenId;
    
    @NotBlank(message = "Buyer wallet address is required")
    private String buyerAddress;
    
    @NotBlank(message = "Transaction hash is required")
    private String transactionHash;
}
