package com.ruhuna.event_ticket_management_system.controller;

import com.ruhuna.event_ticket_management_system.dto.event.CreateEventRequest;
import com.ruhuna.event_ticket_management_system.dto.event.EventResponse;
import com.ruhuna.event_ticket_management_system.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/createEvent")
    public ResponseEntity<String> createEvent(@Valid @RequestBody CreateEventRequest createEventRequest) {
        String txHash = eventService.createEvent(createEventRequest);
        return ResponseEntity.ok(txHash);
    }

    @PostMapping("/deactivate")
    public ResponseEntity<String> deactivate(@Valid @RequestParam BigInteger eventId) {
        String txHash = eventService.deactivateEvent(eventId);
        return ResponseEntity.ok(txHash);
    }

    @GetMapping("/getDetails")
    public ResponseEntity<EventResponse> getDetails(@NotNull @RequestParam BigInteger eventId) {
        EventResponse eventResponse = eventService.getEventDetails(eventId);
        return ResponseEntity.ok(eventResponse);
    }
}
