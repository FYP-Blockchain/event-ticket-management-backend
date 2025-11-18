package com.ruhuna.event_ticket_management_system.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrDataResponse {
    private String message;
    private String signature;
}
