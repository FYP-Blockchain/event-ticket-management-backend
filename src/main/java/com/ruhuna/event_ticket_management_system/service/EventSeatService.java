package com.ruhuna.event_ticket_management_system.service;

import com.ruhuna.event_ticket_management_system.entity.EventSeat;
import com.ruhuna.event_ticket_management_system.repository.EventSeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventSeatService {

    private final EventSeatRepository eventSeatRepository;

    /**
     * Initialize seats for an event
     */
    @Transactional
    public void initializeSeatsForEvent(BigInteger eventId, int totalSupply) {
        log.info("Initializing {} seats for event {}", totalSupply, eventId);
        
        // Check if seats already exist for this event
        if (eventSeatRepository.findByEventId(eventId).isEmpty()) {
            List<String> seatNumbers = generateSeatNumbers(totalSupply);
            
            for (String seatNumber : seatNumbers) {
                EventSeat seat = EventSeat.builder()
                        .eventId(eventId)
                        .seatNumber(seatNumber)
                        .isAvailable(true)
                        .build();
                eventSeatRepository.save(seat);
            }
            
            log.info("Successfully initialized {} seats for event {}", totalSupply, eventId);
        } else {
            log.info("Seats already exist for event {}", eventId);
        }
    }

    /**
     * Generate seat numbers based on total supply
     */
    private List<String> generateSeatNumbers(int totalSupply) {
        List<String> seatNumbers = new java.util.ArrayList<>();
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        int seatsPerRow = (int) Math.ceil((double) totalSupply / rows.length);
        int seatCount = 0;

        for (String row : rows) {
            for (int i = 1; i <= seatsPerRow && seatCount < totalSupply; i++) {
                seatNumbers.add(row + i);
                seatCount++;
            }
        }

        return seatNumbers;
    }

    /**
     * Get all seats for an event
     */
    public List<EventSeat> getSeatsForEvent(BigInteger eventId) {
        return eventSeatRepository.findByEventId(eventId);
    }

    /**
     * Check if a seat is available
     */
    public boolean isSeatAvailable(BigInteger eventId, String seatNumber) {
        return eventSeatRepository.findByEventIdAndSeatNumber(eventId, seatNumber)
                .map(EventSeat::getIsAvailable)
                .orElse(false);
    }

    /**
     * Reserve a seat for a user
     */
    @Transactional
    public boolean reserveSeat(BigInteger eventId, String seatNumber, String reservedBy, 
                              String tokenId, String fabricTicketId) {
        log.info("Attempting to reserve seat {} for event {} by user {}", seatNumber, eventId, reservedBy);
        
        // Check if seat exists and is available
        if (!isSeatAvailable(eventId, seatNumber)) {
            log.warn("Seat {} for event {} is not available", seatNumber, eventId);
            return false;
        }

        int updated = eventSeatRepository.reserveSeat(eventId, seatNumber, reservedBy, tokenId, fabricTicketId);
        
        if (updated > 0) {
            log.info("Successfully reserved seat {} for event {}", seatNumber, eventId);
            return true;
        }
        
        log.warn("Failed to reserve seat {} for event {}", seatNumber, eventId);
        return false;
    }

    /**
     * Release a seat (for cancellation or error recovery)
     */
    @Transactional
    public boolean releaseSeat(BigInteger eventId, String seatNumber) {
        log.info("Releasing seat {} for event {}", seatNumber, eventId);
        int updated = eventSeatRepository.releaseSeat(eventId, seatNumber);
        return updated > 0;
    }

    /**
     * Get available seat count
     */
    public long getAvailableSeatCount(BigInteger eventId) {
        return eventSeatRepository.countAvailableSeats(eventId);
    }

    /**
     * Get occupied seat count
     */
    public long getOccupiedSeatCount(BigInteger eventId) {
        return eventSeatRepository.countOccupiedSeats(eventId);
    }
}
