package com.ruhuna.event_ticket_management_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());

        // Check if this is a backend mode unavailable error
        if (ex.getMessage() != null && ex.getMessage().contains("BACKEND_MODE_UNAVAILABLE")) {
            // Extract metadata URI from error message
            String message = ex.getMessage();
            String metadataURI = null;
            if (message.contains("Metadata uploaded to IPFS: ")) {
                int start = message.indexOf("Metadata uploaded to IPFS: ") + "Metadata uploaded to IPFS: ".length();
                int end = message.indexOf(".", start);
                if (end > start) {
                    metadataURI = message.substring(start, end).trim();
                }
            }

            errorDetails.put("errorCode", "BACKEND_MODE_UNAVAILABLE");
            errorDetails.put("metadataURI", metadataURI);
            errorDetails.put("requiresSelfCustody", true);
            errorDetails.put("instructions", "Hardhat node is not running. Please use self-custody mode (create event from your wallet).");
            return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE); // 503
        }

        if (ex.getMessage().contains("Username is already taken")) {
            return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT); // 409
        } else if (ex.getMessage().contains("Email is already in use")) {
            return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT); // 409
        } else if (ex.getMessage().contains("Invalid username or password")) {
            return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED); // 401
        } else if (ex.getMessage().contains("Role is not found")) {
            return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST); // fallback
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundExceptions(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("status", HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }
}
