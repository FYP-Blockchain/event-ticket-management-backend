package com.ruhuna.event_ticket_management_system.dto.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventResponse {
    private String id;
    private String name;
    private String organizerAddress;
    private LocalDateTime eventDate;
    private Long totalSupply;
    private BigInteger priceInWei;
    private String metadataURI;
    private boolean active;

    private String description;
    private String imageUrl;
    private String location;
    private String eventStartTime;
    private String eventEndTime;
    private String category;
    
    // Resale configuration
    private Integer maxResalePriceMultiplier; // e.g., 150 = 150% of original price
    private Integer organizerResaleShare; // in basis points (e.g., 1000 = 10%)
    private Boolean resaleAllowed;
}