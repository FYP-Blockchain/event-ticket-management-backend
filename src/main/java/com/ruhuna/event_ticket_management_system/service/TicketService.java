package com.ruhuna.event_ticket_management_system.service;

import com.owlike.genson.Genson;
import com.ruhuna.event_ticket_management_system.contracts.TicketManager;
import com.ruhuna.event_ticket_management_system.entity.FabricTicket;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.client.Contract;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Hash;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@AllArgsConstructor
public class TicketService {

    private static final Genson genson = new Genson();

    private final TicketManager ticketManager;
    private final Contract contract;

    public String createTicketAndQueueForPublicPublishing(
            BigInteger publicEventId,
            String seat,
            String secretNonce,
            String initialOwner
    ) throws Exception {
        String fabricTicketJson = createTicketOnFabric(publicEventId.toString(), seat, secretNonce, initialOwner);
        FabricTicket ticket = genson.deserialize(fabricTicketJson, FabricTicket.class);

        // Publish tickets in public asynchronously
        CompletableFuture.runAsync(() -> {
            try {
                BigInteger publicTicketId = new BigInteger(
                        UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16), 16);
                byte[] commitmentHash = calculateCommitmentHash(ticket.getSeat(), secretNonce);
                String tx = publishTicketToPublicChain(
                        publicTicketId,
                        publicEventId,
                        ticket.getSeat(),
                        commitmentHash
                );
                // Notify users that tickets are published.
            } catch (Exception e) {
                log.error("Async Ethereum publishing failed for ticketId: {}", ticket.getPrivateTicketId(), e);
                // implement logic to submit failed ones again
            }
        });

        return fabricTicketJson;
    }

    // For now, we use a simple hash of seat and secretNonce as the commitment.
    private static byte[] calculateCommitmentHash(String seat, String secretNonce) {
        String packedHex = FunctionEncoder.encodeConstructorPacked(
                List.of(
                        new Utf8String(seat),
                        new Utf8String(secretNonce)
                )
        );
        return Hash.sha3(packedHex.getBytes(StandardCharsets.UTF_8));
    }

    private String createTicketOnFabric(String publicEventId, String seat, String secretNonce, String initialOwner)
            throws Exception {
        log.info("FabricTicketService: Submitting 'createTicket' - eventId: {}, seat: {}", publicEventId, seat);

        byte[] resultBytes = contract.submitTransaction("createTicket",
                publicEventId, seat, secretNonce, initialOwner);

        String resultJson = new String(resultBytes, StandardCharsets.UTF_8);
        log.info("FabricTicketService: 'createTicket' raw response: {}", resultJson);

        FabricTicket ticket = genson.deserialize(resultJson, FabricTicket.class);
        if (ticket.getSecretNonce() == null || ticket.getSecretNonce().isEmpty()) {
            log.error("FabricTicketService: Secret Nonce missing from Fabric chaincode response for ticket with privateId: {}", ticket.getPrivateTicketId());
            throw new IllegalStateException("Fabric chaincode did not return a secret nonce.");
        }
        return resultJson;
    }

    private String publishTicketToPublicChain(BigInteger publicTicketId, BigInteger publicEventId, String seat,
                                             byte[] commitmentHash) throws Exception {
        log.info("Publishing ticket to public chain: publicTicketId={}, publicEventId={}, seat={}, commitmentHash={}",
                publicTicketId, publicEventId, seat, org.web3j.utils.Numeric.toHexString(commitmentHash));

        TransactionReceipt receipt = ticketManager.publishTicket(
                publicTicketId,
                publicEventId,
                seat,
                commitmentHash
        ).send();

        if (receipt.isStatusOK()) {
            log.info("Ticket published successfully on public chain. TxHash: {}", receipt.getTransactionHash());
            // Can parse logs from receipt to confirm TicketPublished event if needed
            // List<TicketManager.TicketPublishedEventResponse> events = ticketManager.getTicketPublishedEvents(receipt);
            // if(!events.isEmpty()){ logger.info("TicketPublished event confirmed for ticketId: {}", events.get(0).ticketId); }
            return receipt.getTransactionHash();
        } else {
            log.error("Failed to publish ticket. Tx status: {}. Revert reason (if available): {}",
                    receipt.getStatus(), receipt.getRevertReason());
            throw new RuntimeException("Failed to publish ticket on public chain. Status: " + receipt.getStatus());
        }
    }
}
