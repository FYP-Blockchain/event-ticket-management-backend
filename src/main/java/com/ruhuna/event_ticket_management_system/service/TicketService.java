package com.ruhuna.event_ticket_management_system.service;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import com.ruhuna.event_ticket_management_system.contracts.TicketNFT;
import com.ruhuna.event_ticket_management_system.dto.event.EventResponse;
import com.ruhuna.event_ticket_management_system.dto.ticket.ChaincodeResponse;
import com.ruhuna.event_ticket_management_system.dto.ticket.FabricTicket;
import com.ruhuna.event_ticket_management_system.dto.ticket.TicketPurchaseResponse;
import com.ruhuna.event_ticket_management_system.dto.ticket.TicketRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.client.Contract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerErrorException;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.ruhuna.event_ticket_management_system.utils.ConversionHelper.deserializeResponse;
import static com.ruhuna.event_ticket_management_system.utils.CryptoUtils.calculateCommitmentHash;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketService {

    @Value("${payment.verification.enabled:true}")
    private boolean isPaymentVerificationEnabled;

    private static final Genson genson = new Genson();

    private final TicketNFT ticketNFT;
    private final EventService eventService;
    private final IPFSService ipfsService;
    private final PaymentService paymentService;
    private final Contract contract;
    private final SecureRandom random = new SecureRandom();
    private final String GA_PREFIX = "GA-";

    public TicketPurchaseResponse createAndIssueTicket(TicketRequest request, UserDetails userDetails) {

        if (isPaymentVerificationEnabled && request.getPaymentIntentId() != null && !request.getPaymentIntentId().isBlank()) {
            paymentService.verifyPayment(request.getPaymentIntentId(), request.getProvider());
        }

        String username = userDetails.getUsername();
        log.info("Purchase request: eventId={}, seat={}, buyer={}, wallet={}",
                request.getPublicEventId(), request.getSeat(), username, request.getInitialOwner());
        boolean fabricCommitted = false;
        FabricTicket fabricTicket = null;

        try {
            EventResponse event = validateAndGetEvent(request.getPublicEventId());
            log.debug("Event validated: name={}, price={} wei, supply={}, organizer={}",
                    event.getName(), event.getPriceInWei(), event.getTotalSupply(),
                    event.getOrganizerAddress());


            boolean isGA = request.getSeat() == null || request.getSeat().isBlank();
            if (isGA) {
                String seatIdentifier = GA_PREFIX + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                request.setSeat(seatIdentifier);
            }

            fabricTicket = createTicketOnFabric(request, userDetails.getUsername());
            fabricCommitted = true;
            log.info("Fabric ticket created: fabricId={}, seat={}", fabricTicket.getTicketId(), fabricTicket.getSeat());

            Map<String, Object> metadata = buildTicketMetadata(event, request.getSeat(), username);
            String metadataJson = genson.serialize(metadata);

            String ipfsCid = ipfsService.addJson(metadataJson);
            log.info("Metadata uploaded to IPFS: CID={}", ipfsCid);

            byte[] commitmentHash = calculateCommitmentHash(ipfsCid, fabricTicket.getSecretNonce());
            BigInteger tokenId = generateUniqueTokenId();

            log.info("Submitting Ethereum transaction: tokenId={}, price={} wei",
                    tokenId, event.getPriceInWei());

            TransactionReceipt receipt = ticketNFT.mintWithPayment(
                    request.getInitialOwner(),
                    tokenId,
                    ipfsCid,
                    commitmentHash,
                    event.getOrganizerAddress(),
                    event.getPriceInWei(),
                    event.getPriceInWei()
            ).send();

            log.info("NFT minted successfully: tokenId={}, txHash={}, gasUsed={}",
                    tokenId, receipt.getTransactionHash(), receipt.getGasUsed());

            updateFabricTicketStatus(fabricTicket.getTicketId(), "ISSUED", tokenId.toString(), ipfsCid);

            //eventService.decrementTicketSupply(request.getPublicEventId());

            return TicketPurchaseResponse.builder()
                    .tokenId(tokenId.toString())
                    .fabricTicketId(fabricTicket.getTicketId())
                    .transactionHash(receipt.getTransactionHash())
                    .status("ISSUED")
                    .build();
        } catch (Exception ex) {
            log.error("Ticket purchase failed: {}", ex.getMessage(), ex);

            if (fabricCommitted && fabricTicket != null) {
                try {
                    updateFabricTicketStatus(fabricTicket.getTicketId(), "CANCELLED", null, null);
                    log.warn("Rolled back Fabric ticket: {}", fabricTicket.getTicketId());
                } catch (Exception rollbackEx) {
                    log.error("CRITICAL: Failed to rollback Fabric ticket {}: {}",
                            fabricTicket.getTicketId(), rollbackEx.getMessage());
                }
            }

            if (ex instanceof ResponseStatusException responseStatusException) {
                throw responseStatusException;
            }
            String errorMessage = extractErrorMessage(ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }
    }

    private EventResponse validateAndGetEvent(BigInteger eventId) {
        EventResponse event = eventService.getEventDetails(eventId);

        if (!event.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event is not active");
        }

        if (event.getTotalSupply() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event is sold out");
        }

        if (event.getEventDate().isBefore(java.time.LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event has already occurred");
        }

        if (event.getPriceInWei().compareTo(BigInteger.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ticket price");
        }

        return event;
    }

    private FabricTicket createTicketOnFabric(TicketRequest ticketRequest, String userName) {
        try {
            String secretNonce = generateSecretNonce();
            log.debug("Generated secret nonce for seat {}, nonce {}", ticketRequest.getSeat(), secretNonce);
            byte[] resultBytes = contract.submitTransaction("createTicket",
                    ticketRequest.getPublicEventId().toString(),
                    ticketRequest.getSeat(),
                    secretNonce,
                    ticketRequest.getInitialOwner(),
                    userName
            );

            ChaincodeResponse<FabricTicket> cft = deserializeResponse(
                    resultBytes,
                    new GenericType<ChaincodeResponse<FabricTicket>>() {}
            );
            if ("ERROR".equals(cft.getStatus())) {
                log.error("Failed to create ticket on Fabric: {}", cft.getErrorMessage());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cft.getErrorMessage());
            }
            log.info("Fabric record created for seat {}: privateId={}", ticketRequest.getSeat(),
                    cft.getPayload().getTicketId());
            return cft.getPayload();
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected Fabric error for seat {}: {}", ticketRequest.getSeat(), ex.getMessage(), ex);
            throw new ServerErrorException("Failed to create ticket on Fabric: " + ex.getMessage(), ex);
        }
    }

    private void updateFabricTicketStatus(String fabricTicketId, String status, String tokenId, String ipfsCid) {
        try {
            byte[] resultBytes = contract.submitTransaction(
                    "updateTicketStatus",
                    fabricTicketId,
                    status,
                    tokenId,
                    ipfsCid
            );

            ChaincodeResponse<?> response = deserializeResponse(
                    resultBytes,
                    new GenericType<ChaincodeResponse<?>>() {
                    }
            );

            if ("ERROR".equals(response.getStatus())) {
                log.warn("Failed to update Fabric ticket status: {}", response.getErrorMessage());
            } else {
                log.info("Fabric ticket status updated: {} -> {}", fabricTicketId, status);
            }

        } catch (Exception ex) {
            log.error("Error updating Fabric ticket status: {}", ex.getMessage(), ex);
        }
    }

    private void cancelTicketOnFabric(String fabricTicketId) {
        try {
            contract.submitTransaction("cancelTicket", fabricTicketId);
            log.info("Cancelled Fabric ticket: {}", fabricTicketId);
        } catch (Exception ex) {
            log.error("Failed to cancel Fabric ticket {}: {}", fabricTicketId, ex.getMessage());
            throw new RuntimeException("Rollback failed", ex);
        }
    }

    private Map<String, Object> buildTicketMetadata(
            EventResponse event,
            String seat,
            String username
    ) {
        Map<String, Object> metadata = new HashMap<>();

        metadata.put("name", "Ticket - " + event.getName());
        metadata.put("description", "Event ticket for " + event.getName() + " - Seat: " + seat);
        metadata.put("image", event.getImageUrl());

        metadata.put("eventId", event.getId());
        metadata.put("eventName", event.getName());
        metadata.put("seat", seat);
        metadata.put("purchasedBy", username);

        metadata.put("eventDate", event.getEventDate().toString());
        metadata.put("location", event.getLocation());
        metadata.put("category", event.getCategory());
        metadata.put("venue", event.getLocation());

        metadata.put("organizer", event.getOrganizerAddress());
        metadata.put("priceWei", event.getPriceInWei().toString());
        metadata.put("issuedAt", Instant.now().toString());
        metadata.put("version", "1.0");

        return metadata;
    }

    // Change this to a string type after moving TicketNFT
    private BigInteger generateUniqueTokenId() {
        return new BigInteger(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16), 16);
    }

    private String generateSecretNonce() {
        byte[] nonceBytes = new byte[32];
        random.nextBytes(nonceBytes);
        return Hex.encodeHexString(nonceBytes);
    }

    private String extractErrorMessage(Exception ex) {
        String message = ex.getMessage();
        if (message == null) {
            return "UNKNOWN_ERROR";
        }

        if (message.contains("revert")) {
            int startIdx = message.indexOf("revert");
            int endIdx = message.indexOf("\"", startIdx + 7);
            if (endIdx > startIdx) {
                return message.substring(startIdx + 7, endIdx).trim();
            }
        }

        if (message.contains("Insufficient payment")) {
            return "Insufficient payment sent";
        } else if (message.contains("Seat already sold")) {
            return "ALREADY_SOLD";
        } else if (message.contains("Event not active")) {
            return "EVENT_INACTIVE";
        } else if (message.contains("Event sold out")) {
            return "SOLD_OUT";
        }

        return message;
    }

    /**
     * Prepare ticket for MetaMask purchase - creates Fabric record and uploads to IPFS
     */
    public Map<String, String> prepareTicketForCryptoPurchase(BigInteger eventId, String seat, String initialOwner, String username) {
        try {
            EventResponse event = validateAndGetEvent(eventId);
            
            boolean isGA = seat == null || seat.isBlank();
            if (isGA) {
                seat = GA_PREFIX + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            }

            // Create Fabric ticket
            FabricTicket fabricTicket = createTicketOnFabric(
                new TicketRequest(eventId, seat, initialOwner, null, null),
                username
            );

            // Upload metadata to IPFS
            Map<String, Object> metadata = buildTicketMetadata(event, seat, username);
            String metadataJson = genson.serialize(metadata);
            String ipfsCid = ipfsService.addJson(metadataJson);

            // Calculate commitment hash
            byte[] commitmentHash = calculateCommitmentHash(ipfsCid, fabricTicket.getSecretNonce());
            BigInteger tokenId = generateUniqueTokenId();

            Map<String, String> result = new HashMap<>();
            result.put("fabricTicketId", fabricTicket.getTicketId());
            result.put("ipfsCid", ipfsCid);
            result.put("commitmentHash", Hex.encodeHexString(commitmentHash));
            result.put("tokenId", tokenId.toString());

            return result;
        } catch (ResponseStatusException ex) {
            // Re-throw ResponseStatusException with original status code
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to prepare ticket for crypto purchase: {}", ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Failed to prepare ticket: " + ex.getMessage());
        }
    }

    /**
     * Confirm ticket purchase after MetaMask transaction
     */
    public void confirmCryptoPurchase(String fabricTicketId, String tokenId, String transactionHash) {
        try {
            updateFabricTicketStatus(fabricTicketId, "ISSUED", tokenId, null);
            log.info("Crypto purchase confirmed: fabricId={}, tokenId={}, txHash={}", 
                fabricTicketId, tokenId, transactionHash);
        } catch (Exception ex) {
            log.error("Failed to confirm crypto purchase: {}", ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Failed to confirm purchase: " + ex.getMessage());
        }
    }
}

