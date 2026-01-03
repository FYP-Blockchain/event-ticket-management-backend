package com.ruhuna.event_ticket_management_system.repository;

import com.ruhuna.event_ticket_management_system.entity.EventSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventSeatRepository extends JpaRepository<EventSeat, Long> {
    
    List<EventSeat> findByEventId(BigInteger eventId);
    
    Optional<EventSeat> findByEventIdAndSeatNumber(BigInteger eventId, String seatNumber);
    
    @Query("SELECT COUNT(s) FROM EventSeat s WHERE s.eventId = :eventId AND s.isAvailable = true")
    long countAvailableSeats(@Param("eventId") BigInteger eventId);
    
    @Query("SELECT COUNT(s) FROM EventSeat s WHERE s.eventId = :eventId AND s.isAvailable = false")
    long countOccupiedSeats(@Param("eventId") BigInteger eventId);
    
    boolean existsByEventIdAndSeatNumber(BigInteger eventId, String seatNumber);
    
    @Modifying
    @Query("UPDATE EventSeat s SET s.isAvailable = false, s.reservedBy = :reservedBy, " +
           "s.reservationTime = CURRENT_TIMESTAMP, s.tokenId = :tokenId, s.fabricTicketId = :fabricTicketId " +
           "WHERE s.eventId = :eventId AND s.seatNumber = :seatNumber AND s.isAvailable = true")
    int reserveSeat(@Param("eventId") BigInteger eventId, 
                    @Param("seatNumber") String seatNumber,
                    @Param("reservedBy") String reservedBy,
                    @Param("tokenId") String tokenId,
                    @Param("fabricTicketId") String fabricTicketId);
    
    @Modifying
    @Query("UPDATE EventSeat s SET s.isAvailable = true, s.reservedBy = null, " +
           "s.reservationTime = null, s.tokenId = null, s.fabricTicketId = null " +
           "WHERE s.eventId = :eventId AND s.seatNumber = :seatNumber")
    int releaseSeat(@Param("eventId") BigInteger eventId, @Param("seatNumber") String seatNumber);
    
    @Query("SELECT s.fabricTicketId FROM EventSeat s WHERE s.tokenId = :tokenId")
    Optional<String> findFabricTicketIdByTokenId(@Param("tokenId") String tokenId);
}
