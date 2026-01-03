package com.ruhuna.event_ticket_management_system.dto.resale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResaleInfoResponse {
    private String tokenId;
    private boolean isListed;
    private String resalePriceWei;
    private String maxResalePriceWei;
    private String originalPriceWei;
    private String organizerResaleShareBps; // basis points
    private String organizerAddress;
    private String eventId;
    private String seat;
    private String currentOwner;
}
