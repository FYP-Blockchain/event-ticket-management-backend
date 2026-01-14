package com.ruhuna.event_ticket_management_system.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
@Slf4j
public class DevController {

    @PersistenceContext
    private EntityManager entityManager;
    
    private final CacheManager cacheManager;
    
    /**
     * Instructions for clearing Hyperledger Fabric data.
     * Note: Fabric doesn't support mass deletion via chaincode for security reasons.
     * You must restart the network to clear the ledger.
     */
    @DeleteMapping("/fabric-clear-instructions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getFabricClearInstructions() {
        String instructions = """
            ⚠️ HYPERLEDGER FABRIC DATA CLEARING INSTRUCTIONS:
            
            Fabric ledgers are immutable by design. To clear data, you must restart the network:
            
            1. Navigate to test-network directory:
               cd d:/Academics/Semester7/FYP/Project/fabric-samples/test-network
            
            2. Stop and clean the network:
               ./network.sh down
            
            3. Start the network fresh:
               ./network.sh up createChannel -c mychannel -ca
            
            4. Deploy your chaincode:
               ./network.sh deployCC -ccn ETS-Chaincode -ccp ../ETS-Chaincode -ccl java
            
            5. Restart your backend application to reconnect to the new network
            
            Note: This will create a fresh ledger with no ticket data.
            """;
        
        return ResponseEntity.ok(instructions);
    }

    /**
     * Clear all database tables except user-related tables (users, roles, user_roles)
     * WARNING: This is for development/testing only!
     */
    @DeleteMapping("/clear-data")
    @Transactional
    public ResponseEntity<String> clearNonUserData() {
        try {
            log.warn("⚠️ CLEARING ALL NON-USER DATA - This action cannot be undone!");
            
            int deletedCount = 0;
            
            // Clear event_seats table
            try {
                int seats = entityManager.createNativeQuery("DELETE FROM event_db.event_seats").executeUpdate();
                log.info("Deleted {} records from event_seats", seats);
                deletedCount += seats;
            } catch (Exception e) {
                log.debug("event_seats table might not exist: {}", e.getMessage());
            }
            
            // Clear event_organizer_assignments table
            try {
                int assignments = entityManager.createNativeQuery("DELETE FROM event_db.event_organizer_assignments").executeUpdate();
                log.info("Deleted {} records from event_organizer_assignments", assignments);
                deletedCount += assignments;
            } catch (Exception e) {
                log.debug("event_organizer_assignments table might not exist: {}", e.getMessage());
            }
            
            // Add any other event/ticket related tables here as needed
            // Example:
            // int tickets = entityManager.createNativeQuery("DELETE FROM event_db.tickets").executeUpdate();
            // deletedCount += tickets;
            
            // Clear all caches
            cacheManager.getCacheNames().forEach(cacheName -> {
                var cache = cacheManager.getCache(cacheName);
                if (cache != null) {
                    cache.clear();
                    log.info("Cleared cache: {}", cacheName);
                }
            });
            
            log.info("✅ Successfully cleared {} total records (excluding user data)", deletedCount);
            
            return ResponseEntity.ok(String.format(
                "Successfully cleared %d records from database (users preserved). All caches cleared.",
                deletedCount
            ));
            
        } catch (Exception e) {
            log.error("❌ Error clearing data: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body("Error clearing data: " + e.getMessage());
        }
    }
}
