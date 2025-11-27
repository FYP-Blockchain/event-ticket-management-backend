package com.ruhuna.event_ticket_management_system.controller;

import com.ruhuna.event_ticket_management_system.dto.seat.SeatResponse;
import com.ruhuna.event_ticket_management_system.entity.EventSeat;
import com.ruhuna.event_ticket_management_system.service.EventSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class EventSeatController {

    private final EventSeatService eventSeatService;

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<SeatResponse>> getSeatsForEvent(@PathVariable String eventId) {
        BigInteger eventIdBigInt = new BigInteger(eventId);
        List<EventSeat> seats = eventSeatService.getSeatsForEvent(eventIdBigInt);
        
        List<SeatResponse> seatResponses = seats.stream()
                .map(seat -> SeatResponse.builder()
                        .seatNumber(seat.getSeatNumber())
                        .isAvailable(seat.getIsAvailable())
                        .reservedBy(seat.getReservedBy())
                        .reservationTime(seat.getReservationTime() != null ? 
                                seat.getReservationTime().toString() : null)
                        .build())
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(seatResponses);
    }

    @GetMapping("/event/{eventId}/available-count")
    public ResponseEntity<Long> getAvailableSeatCount(@PathVariable String eventId) {
        BigInteger eventIdBigInt = new BigInteger(eventId);
        long count = eventSeatService.getAvailableSeatCount(eventIdBigInt);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/event/{eventId}/seat/{seatNumber}/available")
    public ResponseEntity<Boolean> checkSeatAvailability(
            @PathVariable String eventId,
            @PathVariable String seatNumber) {
        BigInteger eventIdBigInt = new BigInteger(eventId);
        boolean available = eventSeatService.isSeatAvailable(eventIdBigInt, seatNumber);
        return ResponseEntity.ok(available);
    }

    @PostMapping("/event/{eventId}/initialize")
    public ResponseEntity<String> initializeSeatsForEvent(
            @PathVariable String eventId,
            @RequestParam int totalSupply) {
        BigInteger eventIdBigInt = new BigInteger(eventId);
        eventSeatService.initializeSeatsForEvent(eventIdBigInt, totalSupply);
        return ResponseEntity.ok("Seats initialized successfully for event " + eventId);
    }
}
