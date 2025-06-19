package com.ruhuna.event_ticket_management_system.service;

import com.owlike.genson.Genson;
import com.ruhuna.event_ticket_management_system.contracts.TicketManager;
import com.ruhuna.event_ticket_management_system.entity.FabricTicket;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.Gateway;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Hash;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@AllArgsConstructor
public class TicketService {

    private static final Genson genson = new Genson();
    //private final TicketManager ticketManager;
    private final Contract contract;

    public static byte[] calculateCommitmentHash(String seat, String secretNonce) {
        byte[] seatBytes = seat.getBytes(StandardCharsets.UTF_8);
        byte[] nonceBytes = secretNonce.getBytes(StandardCharsets.UTF_8);

        byte[] packed = new byte[seatBytes.length + nonceBytes.length];
        System.arraycopy(seatBytes, 0, packed, 0, seatBytes.length);
        System.arraycopy(nonceBytes, 0, packed, seatBytes.length, nonceBytes.length);
        return Hash.sha3(packed);
    }

    public String createTicketOnFabric(String publicEventId, String seat, String secretNonce, String initialOwner)
            throws Exception {
        log.info("FabricTicketService: Submitting 'createTicket' - eventId: {}, seat: {}", publicEventId, seat);

        byte[] resultBytes = contract.submitTransaction("createTicket",
                publicEventId, seat, secretNonce, initialOwner);

        String resultJson = new String(resultBytes, StandardCharsets.UTF_8);
        log.debug("FabricTicketService: 'createTicket' raw response: {}", resultJson);

        FabricTicket ticket = genson.deserialize(resultJson, FabricTicket.class);
        if (ticket.getSecretNonce() == null || ticket.getSecretNonce().isEmpty()) {
            log.error("FabricTicketService: Secret Nonce missing from Fabric chaincode response for ticket with privateId: {}", ticket.getPrivateTicketId());
            throw new IllegalStateException("Fabric chaincode did not return a secret nonce.");
        }
        return resultJson;
    }

//    public String publishTicketToPublicChain(BigInteger publicTicketId, BigInteger publicEventId, String seat, byte[] commitmentHash) throws Exception {
//        log.info("Publishing ticket to public chain: publicTicketId={}, publicEventId={}, seat={}, commitmentHash={}",
//                publicTicketId, publicEventId, seat, org.web3j.utils.Numeric.toHexString(commitmentHash));
//
//        TransactionReceipt receipt = ticketManager.publishTicket(
//                publicTicketId,
//                publicEventId,
//                seat,
//                commitmentHash
//        ).send();
//
//        if (receipt.isStatusOK()) {
//            log.info("Ticket published successfully on public chain. TxHash: {}", receipt.getTransactionHash());
//            // You can parse logs from receipt to confirm TicketPublished event if needed
//            // List<TicketManager.TicketPublishedEventResponse> events = ticketManager.getTicketPublishedEvents(receipt);
//            // if(!events.isEmpty()){ logger.info("TicketPublished event confirmed for ticketId: {}", events.get(0).ticketId); }
//            return receipt.getTransactionHash();
//        } else {
//            log.error("Failed to publish ticket. Tx status: {}. Revert reason (if available): {}", receipt.getStatus(), receipt.getRevertReason());
//            throw new RuntimeException("Failed to publish ticket on public chain. Status: " + receipt.getStatus());
//        }
//    }
}
