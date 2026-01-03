package com.ruhuna.event_ticket_management_system.dto.resale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResaleListingResponse {
    private String tokenId;
    private String eventId;
    private String seat;
    private String resalePriceWei;
    private String originalPriceWei;
    private String maxResalePriceWei;
    private String sellerAddress;
    private String sellerUsername;
    private String eventName;
    private String eventDate;
    private boolean isListed;
}
