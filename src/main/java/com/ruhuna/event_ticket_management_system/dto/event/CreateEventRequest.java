package com.ruhuna.event_ticket_management_system.dto.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateEventRequest {
    // On-chain data
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Event date cannot be blank")
    private String eventDateUTC;

    @Min(value = 1)
    private Long totalSupply;

    @Min(value = 0)
    private BigDecimal priceInEther;

    // Off-chain (IPFS) data
    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Event start time cannot be blank")
    private String eventStartTime;

    @NotBlank(message = "Event end time cannot be blank")
    private String eventEndTime;

    @NotBlank(message = "Image URL cannot be blank")
    private String image;

    @NotBlank(message = "Category cannot be blank")
    private String category;

    @NotBlank(message = "location cannot be blank")
    private String location;
}