package com.ruhuna.event_ticket_management_system.dto.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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

    @JsonProperty("ipfsCid")
    private String ipfsCid;

    // Resale tracking fields
    @JsonProperty("nftTokenId")
    private String nftTokenId;

    @JsonProperty("isListedForResale")
    private boolean listedForResale;

    @JsonProperty("resalePrice")
    private String resalePrice;

    @JsonProperty("originalOwner")
    private String originalOwner;

    @JsonProperty("transferHistory")
    private String transferHistory;

    public String toString() {
        return "FabricTicket{" +
                "privateTicketId='" + ticketId + '\'' +
                ", publicEventId='" + eventId + '\'' +
                ", seat='" + seat + '\'' +
                ", secretNonce='***'" +
                ", owner='" + owner + '\'' +
                ", status='" + status + '\'' +
                ", username='" + username + '\'' +
                ", ipfsCid='" + ipfsCid + '\'' +
                '}';
    }
}
