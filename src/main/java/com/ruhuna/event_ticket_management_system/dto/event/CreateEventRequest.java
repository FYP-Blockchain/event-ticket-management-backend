package com.ruhuna.event_ticket_management_system.dto.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateEventRequest {
    @NotBlank(message = "Event name cannot be blank")
    private String name;
    @NotBlank(message = "Event date cannot be blank")
    private String eventDateUTC; ;
    @Min(value = 1, message = "Total supply must be at least 1")
    private Long totalSupply;
    @Min(value = 0, message = "Price in Ether must be at least 0")
    private BigDecimal priceInEther;
    private String metadataURI;
}
