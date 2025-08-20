package com.ruhuna.event_ticket_management_system.dto.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FabricTicket {

    @JsonProperty("ticketId")
    private String ticketId;

    @JsonProperty("eventId")
    private String eventId;

    @JsonProperty("seat")
    private String seat;

    @JsonProperty("secretNonce")
    private String secretNonce;

    @JsonProperty("owner")
    private String owner;

    @JsonProperty("status")
    private String status;

    @JsonProperty("username")
    private String username;

    public String toString() {
        return "FabricTicket{" +
                "privateTicketId='" + ticketId + '\'' +
                ", publicEventId='" + eventId + '\'' +
                ", seat='" + seat + '\'' +
                ", secretNonce='***'" +
                ", owner='" + owner + '\'' +
                ", status='" + status + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
