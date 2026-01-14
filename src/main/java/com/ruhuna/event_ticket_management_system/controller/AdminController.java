package com.ruhuna.event_ticket_management_system.controller;

import com.ruhuna.event_ticket_management_system.repository.EventOrganizerAssignmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final EventOrganizerAssignmentRepository eventOrganizerAssignmentRepository;
    private final CacheManager cacheManager;

    @PostMapping("/clear-event-assignments")
    public ResponseEntity<String> clearEventAssignments() {
        log.warn("Admin requested to clear all event organizer assignments");
        long count = eventOrganizerAssignmentRepository.count();
        eventOrganizerAssignmentRepository.deleteAll();
        
        var eventsCache = cacheManager.getCache("events");
        if (eventsCache != null) {
            eventsCache.clear();
            log.info("Cleared events cache");
        }
        
        log.info("Cleared {} event assignments and cache", count);
        return ResponseEntity.ok(String.format("Cleared %d event assignments and cache. Ready for redeployed blockchain.", count));
    }
}
