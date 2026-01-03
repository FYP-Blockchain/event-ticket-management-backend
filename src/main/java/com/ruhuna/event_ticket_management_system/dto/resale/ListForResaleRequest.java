package com.ruhuna.event_ticket_management_system.dto.resale;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListForResaleRequest {
    
    @NotBlank(message = "Token ID is required")
    private String tokenId;
    
    @NotNull(message = "Resale price is required")
    @Positive(message = "Resale price must be positive")
    private BigInteger resalePriceWei;
    
    @NotBlank(message = "Seller wallet address is required")
    private String sellerAddress;
}
