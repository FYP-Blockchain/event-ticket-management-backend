package com.ruhuna.event_ticket_management_system.service;

import com.ruhuna.event_ticket_management_system.entity.User;
import com.ruhuna.event_ticket_management_system.exception.AuthenticationException;
import com.ruhuna.event_ticket_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrentUserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || isAnonymous(authentication)) {
            throw new AuthenticationException("Authenticated user context is not available");
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Unable to locate authenticated user: " + username));
    }

    private boolean isAnonymous(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        return principal == null || "anonymousUser".equals(principal);
    }
}
