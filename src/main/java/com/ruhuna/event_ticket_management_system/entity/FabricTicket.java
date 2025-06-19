package com.ruhuna.event_ticket_management_system.entity;

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

    @JsonProperty("privateTicketId")
    private String privateTicketId;

    @JsonProperty("publicEventId")
    private String publicEventId;

    @JsonProperty("seat")
    private String seat;

    @JsonProperty("secretNonce")
    private String secretNonce;

    @JsonProperty("owner")
    private String owner;

    @JsonProperty("status")
    private String status;

    public String toString() {
        return "FabricTicket{" +
                "privateTicketId='" + privateTicketId + '\'' +
                ", publicEventId='" + publicEventId + '\'' +
                ", seat='" + seat + '\'' +
                ", secretNonce='***'" +
                ", owner='" + owner + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
