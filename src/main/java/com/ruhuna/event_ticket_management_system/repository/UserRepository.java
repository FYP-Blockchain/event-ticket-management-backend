package com.ruhuna.event_ticket_management_system.repository;

import com.ruhuna.event_ticket_management_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
