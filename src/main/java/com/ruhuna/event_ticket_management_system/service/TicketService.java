package com.ruhuna.event_ticket_management_system.service;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import com.ruhuna.event_ticket_management_system.contracts.TicketNFT;
import com.ruhuna.event_ticket_management_system.dto.event.EventResponse;
import com.ruhuna.event_ticket_management_system.dto.ticket.ChaincodeResponse;
import com.ruhuna.event_ticket_management_system.dto.ticket.TicketRequest;
import com.ruhuna.event_ticket_management_system.dto.ticket.FabricTicket;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.client.Contract;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerErrorException;
import org.web3j.crypto.Hash;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.ruhuna.event_ticket_management_system.utils.ConversionHelper.deserializeResponse;

@Service
@Slf4j
@AllArgsConstructor
public class TicketService {

    private static final Genson genson = new Genson();

    private final TicketNFT ticketNFT;
    private final EventService eventService;
    private final IPFSService ipfsService;
    private final Contract contract;
    private final SecureRandom random = new SecureRandom();

    // Use pooling service or ws to watch the transaction status
    public BigInteger createAndIssueTicket(TicketRequest request, UserDetails userDetails) {
        log.info("Processing ticket request for eventId {} and seat {}", request.getPublicEventId(), request.getSeat());

        // Check whether the ticket can be issued for the event for the user (in the case of user is blocked for event or seat limit exceeds)
        EventResponse eventResponse = eventService.getEventDetails(request.getPublicEventId());
        Long totalSupply = eventResponse.getTotalSupply();
        if (totalSupply <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "EVENT_FULLY_BOOKED");
        }

        // Add seat locking logic here for concurrent requests.

        // Create ticket on Fabric
        FabricTicket fabricTicket = createTicketOnFabric(request, userDetails.getUsername());
        String ipfsCid = buildTicketMetadata(eventResponse);
        log.info("Metadata for ticket, eventID {} uploaded to IPFS. CID: {}", request.getPublicEventId(), ipfsCid);

        byte[] commitmentHash = calculateCommitmentHash(ipfsCid, fabricTicket.getSecretNonce());

        BigInteger tokenId = generateUniqueTokenId();
        try {
            TransactionReceipt receipt = ticketNFT.safeMint(
                    request.getInitialOwner(),
                    tokenId,
                    ipfsCid,
                    commitmentHash
            ).send();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        log.info("SUCCESS: NFT (Token ID: {}) for seat {} minted to address {}",
                tokenId, request.getSeat(), request.getInitialOwner());

        // Update status in fabric
        // updateTicketStatus(fabricTicket, "ISSUED", userDetails.getUsername());

        return tokenId;
    }

    private FabricTicket createTicketOnFabric(TicketRequest ticketRequest, String userName) {
        try {
            String secretNonce = generateSecretNonce();
            log.debug("Generated secret nonce for seat {}", ticketRequest.getSeat());
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

    private String buildTicketMetadata(EventResponse eventResponse) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("eventId", eventResponse.getId());
        metadata.put("event", eventResponse.getName());
        metadata.put("date", eventResponse.getEventDate());
        metadata.put("price", eventResponse.getPriceInWei());
        metadata.put("eventMetadata", eventResponse.getMetadataURI());
        metadata.put("version", "1.0");

        String metadataJson = genson.serialize(metadata);
        return ipfsService.addJson(metadataJson);
    }

    private byte[] calculateCommitmentHash(String ipfsCid, String secretNonce) {
        byte[] cidHash = Hash.sha3(ipfsCid.getBytes(StandardCharsets.UTF_8));
        byte[] nonceHash = Hash.sha3(secretNonce.getBytes(StandardCharsets.UTF_8));
        byte[] combined = new byte[cidHash.length + nonceHash.length];
        System.arraycopy(cidHash, 0, combined, 0, cidHash.length);
        System.arraycopy(nonceHash, 0, combined, cidHash.length, nonceHash.length);
        return Hash.sha3(combined);
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
}
