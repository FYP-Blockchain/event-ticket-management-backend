package com.ruhuna.event_ticket_management_system.service;

import com.owlike.genson.GenericType;
import com.ruhuna.event_ticket_management_system.contracts.TicketNFT;
import com.ruhuna.event_ticket_management_system.dto.ticket.ChaincodeResponse;
import com.ruhuna.event_ticket_management_system.dto.ticket.FabricTicket;
import com.ruhuna.event_ticket_management_system.dto.ticket.VerificationRequest;
import com.ruhuna.event_ticket_management_system.dto.ticket.VerificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.client.Contract;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.web3j.crypto.Hash;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;

import static com.ruhuna.event_ticket_management_system.utils.ConversionHelper.deserializeResponse;
import static io.ipfs.multibase.Base16.bytesToHex;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketVerificationService {

    private final TicketNFT ticketNFT;
    private final Contract fabricContract;

    public VerificationResponse verifyTicket(VerificationRequest request) {
        long startTime = System.currentTimeMillis();
        String tokenId = request.getTokenId();
        String eventId = request.getEventId();
        String walletAddress = request.getWalletAddress();
        String secretNonce = request.getSecretNonce();
        String ipfsCid = request.getIpfsCid();

        log.info("Gate verification started token: {}, event: {}, wallet: {}", tokenId, eventId, walletAddress);

        try {
            String actualOwner = verifyOwnership(tokenId, walletAddress);

            byte[] onChainCommitment = getCommitmentHash(tokenId);
            log.info("Commitment hash retrieved: {}", bytesToHex(onChainCommitment));

            FabricTicket fabricTicket = getFabricTicket(tokenId);

            validateTicketEventAndStatus(fabricTicket, eventId);

            validateCommitment(ipfsCid, secretNonce, onChainCommitment);
            validateSecretNonce(fabricTicket, secretNonce);

            markTicketAsUsed(tokenId);
            log.info("Ticket marked as USED in Fabric");

            long duration = System.currentTimeMillis() - startTime;
            log.info("Ticket Successfully Verified, Duration: {}ms", duration);

            return VerificationResponse.builder()
                    .success(true)
                    .tokenId(tokenId)
                    .eventId(eventId)
                    .seat(fabricTicket.getSeat())
                    .ownerAddress(actualOwner)
                    .message("Ticket verified successfully - Entry granted")
                    .verificationTime(Instant.now().getEpochSecond())
                    .verificationDurationMs(duration)
                    .build();
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected verification error: {}", ex.getMessage(), ex);
            return buildFailureResponse(
                    startTime);
        }
    }

    private String verifyOwnership(String tokenId, String expectedOwner) {
        try {
            log.debug("Verifying NFT ownership...");
            String actualOwner = verifyNFTOwnership(tokenId);
            if (!actualOwner.equals(expectedOwner)) {
                // remove actual owner from log after debug
                log.warn("Verification failed due to ownership mismatch {}", actualOwner);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wallet does not own this ticket");
            }
            log.info("NFT ownership verified of tokenId: {}", tokenId);
            return actualOwner;
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Ownership verification error: {}", ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Blockchain verification failed");
        }
    }

    // In here tokenId is not a fabric ticket Id
    private FabricTicket getFabricTicket(String tokenId) {
        log.debug("Retrieving ticket from fabric...");
        FabricTicket ticket = getTicketFromFabric(tokenId);
        log.info("Ticket found on Fabric: Seat={}, Status={}", ticket.getSeat(), ticket.getStatus());
        return ticket;
    }

    private void validateTicketEventAndStatus(FabricTicket fabricTicket, String expectedEventId) {
        if (!fabricTicket.getEventId().equals(expectedEventId)) {
            log.warn("Event mismatch: Expected={}, Actual={}", expectedEventId, fabricTicket.getEventId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket is not for this event");
        }

        String status = fabricTicket.getStatus();
        if ("USED".equals(status)) {
            log.warn("Ticket already used for the event");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ticket has already been scanned");
        }
        if ("CANCELLED".equals(status)) {
            log.warn("Ticket is cancelled");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket has been cancelled");
        }
        if (!"ISSUED".equals(status)) {
            log.warn("Invalid ticket status: {}", status);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket is in invalid state");
        }
    }

    private void validateCommitment(String ipfsCid, String secretNonce, byte[] expectedCommitment) {
        log.debug("Validating commitment hash...");
        byte[] calculatedCommitment = calculateCommitmentHash(ipfsCid, secretNonce);
        if (!Arrays.equals(expectedCommitment, calculatedCommitment)) {
            log.error("Commitment hash mismatch");
            log.debug("Expected: {}, Actual: {}", bytesToHex(expectedCommitment), bytesToHex(calculatedCommitment));
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Ticket verification failed - Invalid credentials");
        }
        log.info("Commitment hash validated successfully");
    }

    private void validateSecretNonce(FabricTicket fabricTicket, String secretNonce) {
        log.debug("Verifying secret nonce...");
        if (!fabricTicket.getSecretNonce().equals(secretNonce)) {
            log.error("Secret nonce mismatch");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Ticket verification failed - Invalid secret");
        }
        log.info("Secret nonce verified");
    }

    private String verifyNFTOwnership(String tokenId) {
        try {
            BigInteger tokenIdBigInt = new BigInteger(tokenId);
            String owner = ticketNFT.ownerOf(tokenIdBigInt).send();

            if (owner == null || owner.equals("0x0000000000000000000000000000000000000000")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Ticket NFT does not exist");
            }

            return owner;
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to verify NFT ownership: {}", ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Blockchain verification failed");
        }
    }

    private byte[] getCommitmentHash(String tokenId) {
        try {
            BigInteger tokenIdBigInt = new BigInteger(tokenId);
            return ticketNFT.getCommitment(tokenIdBigInt).send();
        } catch (Exception ex) {
            log.error("Failed to get commitment hash: {}", ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to retrieve commitment hash");
        }
    }

    private FabricTicket getTicketFromFabric(String tokenId) {
        try {
            byte[] resultBytes = fabricContract.evaluateTransaction("queryTicket", tokenId);

            ChaincodeResponse<FabricTicket> response = deserializeResponse(
                    resultBytes,
                    new GenericType<>() {
                    }
            );

            if ("ERROR".equals(response.getStatus())) {
                log.error("Fabric query error: {}", response.getErrorMessage());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        response.getErrorMessage());
            }

            return response.getPayload();
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to retrieve ticket from Fabric: {}", ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to retrieve ticket data");
        }
    }

    private void markTicketAsUsed(String tokenId) {
        try {
            byte[] resultBytes = fabricContract.submitTransaction(
                    "markTicketAsUsed",
                    tokenId
            );

            ChaincodeResponse<?> response = deserializeResponse(
                    resultBytes,
                    new GenericType<ChaincodeResponse<?>>() {
                    }
            );

            if ("ERROR".equals(response.getStatus())) {
                log.error("Failed to mark ticket as used: {}", response.getErrorMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to update ticket status");
            }
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error marking ticket as used: {}", ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to mark ticket as used");
        }
    }

    // This should be util func (used in both ticket and verification service)
    private byte[] calculateCommitmentHash(String ipfsCid, String secretNonce) {
        byte[] cidHash = Hash.sha3(ipfsCid.getBytes(StandardCharsets.UTF_8));
        byte[] nonceHash = Hash.sha3(secretNonce.getBytes(StandardCharsets.UTF_8));
        byte[] combined = new byte[cidHash.length + nonceHash.length];
        System.arraycopy(cidHash, 0, combined, 0, cidHash.length);
        System.arraycopy(nonceHash, 0, combined, cidHash.length, nonceHash.length);
        return Hash.sha3(combined);
    }

    private VerificationResponse buildFailureResponse(long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        return VerificationResponse.builder()
                .success(false)
                .errorCode("VERIFICATION_ERROR")
                .message("System error during verification")
                .verificationTime(Instant.now().getEpochSecond())
                .verificationDurationMs(duration)
                .build();
    }
}
