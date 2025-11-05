package com.ruhuna.event_ticket_management_system.service;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import com.ruhuna.event_ticket_management_system.contracts.TicketNFT;
import com.ruhuna.event_ticket_management_system.dto.ticket.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.hyperledger.fabric.client.Contract;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerErrorException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.time.Instant;
import java.util.Arrays;

import static com.ruhuna.event_ticket_management_system.utils.ConversionHelper.deserializeResponse;
import static com.ruhuna.event_ticket_management_system.utils.CryptoUtils.calculateCommitmentHash;
import static io.ipfs.multibase.Base16.bytesToHex;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketVerificationService {

    private final Credentials signerCredentials = Credentials.create("YOUR_BACKEND_SIGNER_PRIVATE_KEY");
    private static final Genson genson = new Genson();

    private final TicketNFT ticketNFT;
    private final Contract fabricContract;

    public VerificationResponse verifyTicket(VerificationRequest request) {
        long startTime = System.currentTimeMillis();

        try {
            String signerAddress = verifySignatureAndGetSigner(request.getMessage(), request.getSignature());
            if (!signerAddress.equalsIgnoreCase(signerCredentials.getAddress())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid QR code signature.");
            }

            DecodedQrPayload payload = parseQrMessage(request.getMessage());
            String tokenId = payload.getTokenId();
            String fabricTicketId = payload.getFabricTicketId();
            String secretNonce = payload.getSecretNonce();
            String ipfsCid = payload.getIpfsCid();

            byte[] onChainCommitment = getCommitmentHash(tokenId);

            FabricTicket fabricTicket = getFabricTicket(fabricTicketId);

            validateTicketEventAndStatus(fabricTicket, fabricTicket.getEventId());

            validateCommitment(ipfsCid, secretNonce, onChainCommitment);
            validateSecretNonce(fabricTicket, secretNonce);
            markTicketAsUsed(fabricTicketId);

            long duration = System.currentTimeMillis() - startTime;
            log.info("Ticket Successfully Verified, Duration: {}ms", duration);

            return VerificationResponse.builder()
                    .success(true)
                    .tokenId(tokenId)
                    .eventId(fabricTicket.getEventId())
                    .seat(fabricTicket.getSeat())
                    .message("Ticket verified successfully - Entry granted")
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

    public QrDataResponse getQrData(String tokenId, String walletId) {
        FabricTicket fabricTicket = getVerifyFabricTicket(tokenId, walletId);

        String messageToSign = String.format(
                "{\"tokenId\":\"%s\",\"fabricTicketId\":\"%s\",\"secretNonce\":\"%s\",\"timestamp\":%d}",
                tokenId,
                fabricTicket.getTicketId(),
                fabricTicket.getSecretNonce(),
                Instant.now().getEpochSecond()
        );

        Sign.SignatureData signatureData = Sign.signMessage(
                messageToSign.getBytes(StandardCharsets.UTF_8),
                signerCredentials.getEcKeyPair(),
                false
        );
        String signature = "0x" + Hex.toHexString(signatureData.getR()) +
                Hex.toHexString(signatureData.getS()) +
                Hex.toHexString(signatureData.getV());

        return new QrDataResponse(messageToSign, signature);
    }

    private DecodedQrPayload parseQrMessage(String messageJson) {
        try {
            return genson.deserialize(messageJson, DecodedQrPayload.class);
        } catch (Exception ex) {
            log.error("Failed to parse QR code JSON payload: {}", messageJson, ex);
            // This error indicates a malformed or corrupt QR code.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid QR code data.");
        }
    }

    private FabricTicket getVerifyFabricTicket(String tokenId, String expectedOwner) {
        try {
            log.debug("Verifying NFT ownership for token {}", tokenId);
            String actualOwner = verifyNFTOwnership(tokenId);

            if (!actualOwner.equalsIgnoreCase(expectedOwner)) {
                log.warn("Ownership mismatch for token {}. Expected: {}, Actual: {}", tokenId, expectedOwner, actualOwner);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wallet does not own this ticket NFT");
            }
            log.info("NFT ownership verified of tokenId: {}", tokenId);

            return queryTicketByNftIdAndOwner(tokenId, expectedOwner);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Ownership verification error: {}", ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Blockchain verification failed");
        }
    }

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

    private void markTicketAsUsed(String fabricTicketId) {
        try {
            byte[] resultBytes = fabricContract.submitTransaction(
                    "markTicketAsUsed",
                    fabricTicketId
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

    private VerificationResponse buildFailureResponse(long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        return VerificationResponse.builder()
                .success(false)
                .errorCode("VERIFICATION_ERROR")
                .message("System error during verification")
                .verificationDurationMs(duration)
                .build();
    }

    private FabricTicket queryTicketByNftIdAndOwner(String tokenId, String walletId) {
        try {
            byte[] resultBytes = fabricContract.evaluateTransaction(
                    "queryTicketByNftIdAndOwner",
                    tokenId,
                    walletId
            );

            ChaincodeResponse<FabricTicket> response = deserializeResponse(
                    resultBytes,
                    new GenericType<ChaincodeResponse<FabricTicket>>() {
                    }
            );

            if ("ERROR".equals(response.getStatus())) {
                log.error("Failed to get ticket by token and owner on Fabric: {}", response.getErrorMessage());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, response.getErrorMessage());
            }

            return response.getPayload();
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected Fabric error for tokenId {}: {}", tokenId, ex.getMessage(), ex);
            throw new ServerErrorException("Failed to create ticket on Fabric: " + ex.getMessage(), ex);
        }
    }

    private String verifySignatureAndGetSigner(String message, String signature) throws SignatureException {
        if (signature == null || !signature.startsWith("0x") || signature.length() != 132) {
            throw new SignatureException("Invalid signature format or length.");
        }

        String cleanSignature = Numeric.cleanHexPrefix(signature);
        byte[] r = Numeric.hexStringToByteArray(cleanSignature.substring(0, 64));
        byte[] s = Numeric.hexStringToByteArray(cleanSignature.substring(64, 128));
        byte[] v = Numeric.hexStringToByteArray(cleanSignature.substring(128, 130));

        Sign.SignatureData signatureData = new Sign.SignatureData(v, r, s);
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

        try {
            BigInteger publicKey = Sign.signedMessageToKey(messageBytes, signatureData);
            String recoveredAddress = Keys.getAddress(publicKey);
            return Numeric.prependHexPrefix(recoveredAddress);
        } catch (Exception e) {
            log.error("Failed to recover address from signature.", e);
            throw new SignatureException("Could not recover address from signature.", e);
        }
    }
}
