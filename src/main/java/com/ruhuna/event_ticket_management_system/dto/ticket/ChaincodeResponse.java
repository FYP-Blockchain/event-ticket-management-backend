package com.ruhuna.event_ticket_management_system.dto.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ChaincodeResponse<T> {
    @JsonProperty("status")
    private String status;

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("errorMessage")
    private String errorMessage;

    @JsonProperty("payload")
    private T payload;
    public String toJsonString() {
        return String.format("{\"status\":\"%s\", \"errorCode\":\"%s\", \"errorMessage\":\"%s\", \"payload\":%s}",
                status, errorCode, errorMessage, payload);
    }
}
