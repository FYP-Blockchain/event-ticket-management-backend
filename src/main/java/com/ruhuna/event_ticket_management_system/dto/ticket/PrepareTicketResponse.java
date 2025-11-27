package com.ruhuna.event_ticket_management_system.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrepareTicketResponse {
    private String fabricTicketId;
    private String ipfsCid;
    private String commitmentHash;
    private String tokenId;
    private String seat;
}
