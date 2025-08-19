package com.ruhuna.event_ticket_management_system.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventMetadata {
    private String description;
    private String image;
    private String location;
    private String eventStartTime;
    private String eventEndTime;
    private String category;
}