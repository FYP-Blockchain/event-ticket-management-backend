package com.ruhuna.event_ticket_management_system.dto.seat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatResponse {
    private String seatNumber;
    private Boolean isAvailable;
    private String reservedBy;
    private String reservationTime;
}
