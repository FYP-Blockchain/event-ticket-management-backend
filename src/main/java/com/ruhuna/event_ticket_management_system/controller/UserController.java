package com.ruhuna.event_ticket_management_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/info")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserInfo(Principal principal) {
        return ResponseEntity.ok(
                "Hello, " + principal.getName() + ". This is your user info endpoint."
        );
    }
}
