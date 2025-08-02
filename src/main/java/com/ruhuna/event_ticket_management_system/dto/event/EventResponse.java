package com.ruhuna.event_ticket_management_system.dto.event;

import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private String id;
    private String name;
    private String organizerAddress;
    private LocalDateTime eventDate;
    private Long totalSupply;
    private BigInteger priceInEther;
    private String metadataURI;
    private String imageUrl;
    private boolean active;
}
