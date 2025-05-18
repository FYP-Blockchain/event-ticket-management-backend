package com.ruhuna.event_ticket_management_system.repository;

import com.ruhuna.event_ticket_management_system.entity.ERole;
import com.ruhuna.event_ticket_management_system.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
