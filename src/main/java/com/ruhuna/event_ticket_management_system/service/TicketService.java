package com.ruhuna.event_ticket_management_system.service;

import com.owlike.genson.Genson;
import com.ruhuna.event_ticket_management_system.contracts.TicketNFT;
import com.ruhuna.event_ticket_management_system.dto.ticket.TicketRequest;
import com.ruhuna.event_ticket_management_system.entity.FabricTicket;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.client.Contract;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Hash;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@AllArgsConstructor
public class TicketService {

    private static final Genson genson = new Genson();

    private final TicketNFT ticketNFT;
    private final Contract contract;
    private final SecureRandom random = new SecureRandom();

    // Use pooling service or ws to watch the transaction status
    public void createAndIssueTicket(TicketRequest request) {
        CompletableFuture.runAsync(() -> {
            try {
                log.info("Processing ticket request for eventId {} and seat {}", request.getPublicEventId(), request.getSeat());

                String secretNonce = generateSecretNonce();
                log.debug("Generated secret nonce for seat {}", request.getSeat());

                FabricTicket fabricTicket = createTicketOnFabric(
                        request.getPublicEventId().toString(),
                        request.getSeat(),
                        secretNonce,
                        request.getInitialOwner()
                );
                log.info("Fabric record created for seat {}: privateId={}", request.getSeat(), fabricTicket.getPrivateTicketId());

                // this cid is for metadata via IPFS
                // ex:- String ipfsCid = ipfsService.addJson(metadataJson);
                String ipfsCid = "example-cid";
                log.info("Metadata for ticket, eventID {} uploaded to IPFS. CID: {}", request.getPublicEventId(), ipfsCid);

                byte[] commitmentHash = calculateCommitmentHash(ipfsCid, fabricTicket.getSecretNonce());

                BigInteger tokenId = generateUniqueTokenId();

                TransactionReceipt receipt = ticketNFT.safeMint(
                        request.getInitialOwner(),
                        tokenId,
                        ipfsCid,
                        commitmentHash
                ).send();

                log.info("SUCCESS: NFT (Token ID: {}) for seat {} minted to address {}!",
                        tokenId, request.getSeat(), request.getInitialOwner());

                // Update status in fabric

            } catch (Exception e) {
                log.error("Failed to create and issue ticket for seat {}: {}", request.getSeat(), e.getMessage(), e);
                // Implement retry logic or fallback logic here
            }
        });
    }

    private byte[] calculateCommitmentHash(String ipfsCid, String secretNonce) {
        byte[] cidBytes = ipfsCid.getBytes(StandardCharsets.UTF_8);
        byte[] nonceBytes = secretNonce.getBytes(StandardCharsets.UTF_8);
        byte[] combined = new byte[cidBytes.length + nonceBytes.length];
        System.arraycopy(cidBytes, 0, combined, 0, cidBytes.length);
        System.arraycopy(nonceBytes, 0, combined, cidBytes.length, nonceBytes.length);
        return Hash.sha3(combined);
    }

    // handle specific type fabric create ticket errors
    private FabricTicket createTicketOnFabric(String publicEventId, String seat, String secretNonce, String initialOwner) throws Exception {
        byte[] resultBytes = contract.submitTransaction("createTicket",
                publicEventId, seat, secretNonce, initialOwner);
        return genson.deserialize(new String(resultBytes, StandardCharsets.UTF_8), FabricTicket.class);
    }

    private BigInteger generateUniqueTokenId() {
        return new BigInteger(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16), 16);
    }

    private String generateSecretNonce() {
        byte[] nonceBytes = new byte[32];
        random.nextBytes(nonceBytes);
        return Hex.encodeHexString(nonceBytes);
    }
}
