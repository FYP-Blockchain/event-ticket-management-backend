package com.ruhuna.event_ticket_management_system.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerificationRequest {
    @NotBlank(message = "Token Id cannot be blank")
    private String tokenId;

    @NotBlank(message = "Fabric Event Id cannot be blanked")
    private String ticketId;

    @NotBlank(message = "Event Id cannot be blank")
    private String eventId;

    @NotBlank(message = "Wallet Address cannot be blank")
    private String walletAddress;

    @NotBlank(message = "Secret Nonce cannot be blank")
    private String secretNonce;

    @NotBlank(message = "IPFS CID cannot be blank")
    private String ipfsCid;
}
