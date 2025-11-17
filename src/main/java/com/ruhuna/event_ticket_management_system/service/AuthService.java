package com.ruhuna.event_ticket_management_system.service;

import com.ruhuna.event_ticket_management_system.dto.JwtResponse;
import com.ruhuna.event_ticket_management_system.dto.LoginRequest;
import com.ruhuna.event_ticket_management_system.dto.SignupRequest;
import com.ruhuna.event_ticket_management_system.entity.ERole;
import com.ruhuna.event_ticket_management_system.entity.Role;
import com.ruhuna.event_ticket_management_system.entity.User;
import com.ruhuna.event_ticket_management_system.exception.AuthenticationException;
import com.ruhuna.event_ticket_management_system.repository.RoleRepository;
import com.ruhuna.event_ticket_management_system.repository.UserRepository;
import com.ruhuna.event_ticket_management_system.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        log.info("Authentication attempt for username='{}'", username);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtils.generateJwtToken(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            log.info("Authentication successful for username='{}'", username);
            return new JwtResponse(token, userDetails.getUsername(), roles);
        } catch (BadCredentialsException ex) {
            log.warn("Invalid credentials for username='{}'", username);
            throw new AuthenticationException("Invalid username or password.");
        } catch (Exception ex) {
            log.error("Authentication failed for username='{}' - {}", username, ex.getMessage(), ex);
            throw new AuthenticationException("Authentication failed.", ex);
        }
    }


    public String registerUser(SignupRequest signUpRequest) {
        String normalizedUsername = signUpRequest.getUsername().trim().toLowerCase();
        if (userRepository.existsByUsername(normalizedUsername)) {
            throw new RuntimeException("Username is already taken!");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = new User(
                normalizedUsername,
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword())
        );

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role defaultRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Default role USER not found."));
            roles.add(defaultRole);
        } else {
            strRoles.forEach(role -> {
                Role foundRole = switch (role.toLowerCase()) {
                    case "admin" -> roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role ADMIN not found."));
                    case "organizer" -> roleRepository.findByName(ERole.ROLE_ORGANIZER)
                            .orElseThrow(() -> new RuntimeException("Error: Role ORGANIZER not found."));
                    default -> roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role USER not found."));
                };
                roles.add(foundRole);
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        log.info("Registered new user: {} (roles={})", normalizedUsername, roles.stream().map(r->r.getName().name()).toList());
        return "User registered successfully!";
    }
}