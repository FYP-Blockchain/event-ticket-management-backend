package com.ruhuna.event_ticket_management_system.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerificationRequest {
    @NotBlank(message = "Event Id cannot be blank")
    private String eventId;

    @NotBlank(message = "Message cannot be blank")
    private String message;

    @NotBlank(message = "Signature cannot be blank")
    private String signature;
}
