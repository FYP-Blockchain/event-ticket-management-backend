package com.ruhuna.event_ticket_management_system.controller;

import com.ruhuna.event_ticket_management_system.dto.event.EventResponse;
import com.ruhuna.event_ticket_management_system.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping(value = "/createEvent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventResponse> createEvent(
            @RequestParam("name") String name,
            @RequestParam("eventDateUTC") String eventDateUTC,
            @RequestParam("totalSupply") Long totalSupply,
            @RequestParam("priceInEther") BigDecimal priceInEther,
            @RequestParam("description") String description,
            @RequestParam("eventStartTime") String eventStartTime,
            @RequestParam("eventEndTime") String eventEndTime,
            @RequestParam("category") String category,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam("imageFile") MultipartFile imageFile) {
        EventResponse newEvent = eventService.createEvent(name, eventDateUTC, totalSupply, priceInEther, description, eventStartTime, eventEndTime, category, location, imageFile);
        return ResponseEntity.ok(newEvent);
    }

    @PutMapping(value = "/update/{eventId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable BigInteger eventId,
            @RequestParam("name") String name,
            @RequestParam("eventDateUTC") String eventDateUTC,
            @RequestParam("totalSupply") Long totalSupply,
            @RequestParam("priceInEther") BigDecimal priceInEther,
            @RequestParam("description") String description,
            @RequestParam("eventStartTime") String eventStartTime,
            @RequestParam("eventEndTime") String eventEndTime,
            @RequestParam("category") String category,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        EventResponse updatedEvent = eventService.updateEventDetails(eventId, name, eventDateUTC, totalSupply, priceInEther, description, eventStartTime, eventEndTime, category, location, imageFile);
        return ResponseEntity.ok(updatedEvent);
    }

    @PostMapping("/deactivate")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<String> deactivate(@Valid @RequestParam BigInteger eventId) {
        String txHash = eventService.deactivateEvent(eventId);
        return ResponseEntity.ok(txHash);
    }

    @GetMapping("/getDetails")
    public ResponseEntity<EventResponse> getDetails(@NotNull @RequestParam BigInteger eventId) {
        EventResponse eventResponse = eventService.getEventDetails(eventId);
        return ResponseEntity.ok(eventResponse);
    }

    @GetMapping("/my-events")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<List<EventResponse>> getMyEvents() {
        List<EventResponse> events = eventService.getEventsForCurrentOrganizer();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

}