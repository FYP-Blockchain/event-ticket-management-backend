package com.ruhuna.event_ticket_management_system.repository;

import com.ruhuna.event_ticket_management_system.entity.EventOrganizerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventOrganizerAssignmentRepository extends JpaRepository<EventOrganizerAssignment, Long> {
    Optional<EventOrganizerAssignment> findByEventId(String eventId);
    List<EventOrganizerAssignment> findAllByOrganizer_Id(Long organizerId);
}
