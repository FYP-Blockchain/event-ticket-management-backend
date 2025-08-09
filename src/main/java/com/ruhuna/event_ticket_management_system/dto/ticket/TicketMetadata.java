package com.ruhuna.event_ticket_management_system.dto.ticket;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketMetadata {
    private String name;
    private String description;
    private String image;
    private List<Attribute> attributes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attribute {
        private String trait_type;
        private Object value;
    }
}
