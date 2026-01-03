package com.ruhuna.event_ticket_management_system.service;

import com.owlike.genson.GenericType;
import com.ruhuna.event_ticket_management_system.contracts.TicketNFT;
import com.ruhuna.event_ticket_management_system.dto.resale.BuyResaleTicketRequest;
import com.ruhuna.event_ticket_management_system.dto.resale.ListForResaleRequest;
import com.ruhuna.event_ticket_management_system.dto.resale.ResaleInfoResponse;
import com.ruhuna.event_ticket_management_system.dto.resale.ResaleListingResponse;
import com.ruhuna.event_ticket_management_system.dto.ticket.ChaincodeResponse;
import com.ruhuna.event_ticket_management_system.dto.ticket.FabricTicket;
import com.ruhuna.event_ticket_management_system.repository.EventSeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.client.Contract;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.web3j.tuples.generated.Tuple6;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.ruhuna.event_ticket_management_system.utils.ConversionHelper.deserializeResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResaleService {
    
    private final TicketNFT ticketNFT;
    private final Contract fabricContract;
    private final EventSeatService eventSeatService;
    private final EventSeatRepository eventSeatRepository;

    /**
     * List a ticket for resale
     * 1. Verify ownership on blockchain
     * 2. Check resale price against max allowed
     * 3. Update Fabric ledger for off-chain tracking
     * 4. List on blockchain (done by frontend via MetaMask)
     */
    public ResaleInfoResponse listForResale(ListForResaleRequest request, UserDetails userDetails) {
        log.info("Listing ticket for resale: tokenId={}, price={}, seller={}", 
                request.getTokenId(), request.getResalePriceWei(), request.getSellerAddress());
        
        try {
            BigInteger tokenId = new BigInteger(request.getTokenId());
            
            // Verify ownership on blockchain
            String currentOwner = ticketNFT.ownerOf(tokenId).send();
            if (!currentOwner.equalsIgnoreCase(request.getSellerAddress())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not the ticket owner");
            }
            
            // Get resale info from blockchain
            Tuple6<Boolean, BigInteger, BigInteger, BigInteger, BigInteger, String> resaleInfo = 
                    ticketNFT.getResaleInfo(tokenId).send();
            
            boolean isListed = resaleInfo.component1();
            BigInteger currentResalePrice = resaleInfo.component2();
            BigInteger maxResalePrice = resaleInfo.component3();
            BigInteger originalPrice = resaleInfo.component4();
            BigInteger organizerShare = resaleInfo.component5();
            String organizer = resaleInfo.component6();
            
            if (isListed) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket is already listed for resale");
            }
            
            // Verify resale price doesn't exceed maximum
            if (request.getResalePriceWei().compareTo(maxResalePrice) > 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        "Resale price exceeds maximum allowed: " + maxResalePrice.toString());
            }
            
            // Get fabric ticket ID from the lookup
            String fabricTicketId = findFabricTicketId(tokenId.toString());
            
            // Update Fabric ledger
            if (fabricTicketId != null && !fabricTicketId.isEmpty()) {
                byte[] result = fabricContract.submitTransaction(
                        "listTicketForResale",
                        fabricTicketId,
                        request.getResalePriceWei().toString(),
                        request.getSellerAddress()
                );
                
                // Parse as String response since chaincode now returns String
                String resultJson = new String(result, java.nio.charset.StandardCharsets.UTF_8);
                com.owlike.genson.Genson genson = new com.owlike.genson.Genson();
                java.util.Map<String, Object> responseMap = genson.deserialize(resultJson, java.util.Map.class);
                
                if ("ERROR".equals(responseMap.get("status"))) {
                    log.error("Fabric listing failed: {}", responseMap.get("errorMessage"));
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                            "Failed to update off-chain ledger: " + responseMap.get("errorMessage"));
                }
                log.info("Fabric ledger updated for resale listing: {}", fabricTicketId);
            }
            
            log.info("Ticket listed for resale successfully: tokenId={}", request.getTokenId());
            
            return ResaleInfoResponse.builder()
                    .tokenId(request.getTokenId())
                    .isListed(false) // Will be true after blockchain tx
                    .resalePriceWei(request.getResalePriceWei().toString())
                    .maxResalePriceWei(maxResalePrice.toString())
                    .originalPriceWei(originalPrice.toString())
                    .organizerResaleShareBps(organizerShare.toString())
                    .organizerAddress(organizer)
                    .currentOwner(currentOwner)
                    .build();
                    
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to list ticket for resale: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to list ticket for resale: " + e.getMessage());
        }
    }

    /**
     * Unlist a ticket from resale
     */
    public void unlistFromResale(String tokenId, String ownerAddress, UserDetails userDetails) {
        log.info("Unlisting ticket from resale: tokenId={}, owner={}", tokenId, ownerAddress);
        
        try {
            BigInteger tokenIdBig = new BigInteger(tokenId);
            
            // Verify ownership
            String currentOwner = ticketNFT.ownerOf(tokenIdBig).send();
            if (!currentOwner.equalsIgnoreCase(ownerAddress)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not the ticket owner");
            }
            
            // Get fabric ticket ID
            String fabricTicketId = findFabricTicketId(tokenId);
            
            // Update Fabric ledger
            if (fabricTicketId != null && !fabricTicketId.isEmpty()) {
                byte[] result = fabricContract.submitTransaction(
                        "unlistTicketFromResale",
                        fabricTicketId,
                        ownerAddress
                );
                
                // Parse as String response since chaincode now returns String
                String resultJson = new String(result, java.nio.charset.StandardCharsets.UTF_8);
                com.owlike.genson.Genson genson = new com.owlike.genson.Genson();
                java.util.Map<String, Object> responseMap = genson.deserialize(resultJson, java.util.Map.class);
                
                if ("ERROR".equals(responseMap.get("status"))) {
                    log.error("Fabric unlisting failed: {}", responseMap.get("errorMessage"));
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                            "Failed to update off-chain ledger: " + responseMap.get("errorMessage"));
                }
                log.info("Fabric ledger updated for unlisting: {}", fabricTicketId);
            }
            
            log.info("Ticket unlisted from resale successfully: tokenId={}", tokenId);
            
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to unlist ticket from resale: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to unlist ticket: " + e.getMessage());
        }
    }

    /**
     * Confirm resale purchase after blockchain transaction
     * Updates Fabric ledger with new ownership
     */
    public void confirmResalePurchase(BuyResaleTicketRequest request, UserDetails userDetails) {
        log.info("Confirming resale purchase: tokenId={}, buyer={}, txHash={}", 
                request.getTokenId(), request.getBuyerAddress(), request.getTransactionHash());
        
        try {
            BigInteger tokenId = new BigInteger(request.getTokenId());
            
            // Verify new ownership on blockchain
            String newOwner = ticketNFT.ownerOf(tokenId).send();
            if (!newOwner.equalsIgnoreCase(request.getBuyerAddress())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        "Ownership not transferred on blockchain yet");
            }
            
            // Get fabric ticket ID
            String fabricTicketId = findFabricTicketId(request.getTokenId());
            
            // Update Fabric ledger with new ownership
            if (fabricTicketId != null && !fabricTicketId.isEmpty()) {
                byte[] result = fabricContract.submitTransaction(
                        "transferTicketOwnership",
                        fabricTicketId,
                        request.getBuyerAddress(),
                        userDetails.getUsername(),
                        request.getTransactionHash()
                );
                
                // Parse as String response since chaincode now returns String
                String resultJson = new String(result, java.nio.charset.StandardCharsets.UTF_8);
                com.owlike.genson.Genson genson = new com.owlike.genson.Genson();
                java.util.Map<String, Object> responseMap = genson.deserialize(resultJson, java.util.Map.class);
                
                if ("ERROR".equals(responseMap.get("status"))) {
                    log.error("Fabric ownership transfer failed: {}", responseMap.get("errorMessage"));
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                            "Failed to update off-chain ledger: " + responseMap.get("errorMessage"));
                }
                
                // Update seat ownership in database - extract ticket from payload
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> payload = (java.util.Map<String, Object>) responseMap.get("payload");
                if (payload != null && payload.get("eventId") != null && payload.get("seat") != null) {
                    try {
                        // Release old seat and reserve for new owner
                        BigInteger eventIdBig = new BigInteger((String) payload.get("eventId"));
                        String seat = (String) payload.get("seat");
                        eventSeatService.releaseSeat(eventIdBig, seat);
                        eventSeatService.reserveSeat(
                                eventIdBig,
                                seat,
                                userDetails.getUsername(),
                                request.getTokenId(),
                                fabricTicketId
                        );
                    } catch (Exception e) {
                        log.warn("Failed to update seat ownership: {}", e.getMessage());
                    }
                }
            }
            
            log.info("Resale purchase confirmed successfully: tokenId={}", request.getTokenId());
            
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to confirm resale purchase: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to confirm resale: " + e.getMessage());
        }
    }

    /**
     * Get resale info for a specific ticket
     */
    public ResaleInfoResponse getResaleInfo(String tokenId) {
        try {
            BigInteger tokenIdBig = new BigInteger(tokenId);
            
            Tuple6<Boolean, BigInteger, BigInteger, BigInteger, BigInteger, String> resaleInfo = 
                    ticketNFT.getResaleInfo(tokenIdBig).send();
            
            String currentOwner = ticketNFT.ownerOf(tokenIdBig).send();
            String fabricTicketId = findFabricTicketId(tokenId);
            
            String eventId = null;
            String seat = null;
            
            // Get additional info from Fabric
            if (fabricTicketId != null && !fabricTicketId.isEmpty()) {
                try {
                    byte[] result = fabricContract.evaluateTransaction("queryTicket", fabricTicketId);
                    // Parse as String response since chaincode now returns String
                    String resultJson = new String(result, java.nio.charset.StandardCharsets.UTF_8);
                    com.owlike.genson.Genson genson = new com.owlike.genson.Genson();
                    java.util.Map<String, Object> responseMap = genson.deserialize(resultJson, java.util.Map.class);
                    
                    if ("SUCCESS".equals(responseMap.get("status")) && responseMap.get("payload") != null) {
                        @SuppressWarnings("unchecked")
                        java.util.Map<String, Object> payload = (java.util.Map<String, Object>) responseMap.get("payload");
                        eventId = (String) payload.get("eventId");
                        seat = (String) payload.get("seat");
                    }
                } catch (Exception e) {
                    log.warn("Failed to get Fabric ticket info: {}", e.getMessage());
                }
            }
            
            return ResaleInfoResponse.builder()
                    .tokenId(tokenId)
                    .isListed(resaleInfo.component1())
                    .resalePriceWei(resaleInfo.component2().toString())
                    .maxResalePriceWei(resaleInfo.component3().toString())
                    .originalPriceWei(resaleInfo.component4().toString())
                    .organizerResaleShareBps(resaleInfo.component5().toString())
                    .organizerAddress(resaleInfo.component6())
                    .currentOwner(currentOwner)
                    .eventId(eventId)
                    .seat(seat)
                    .build();
                    
        } catch (Exception e) {
            log.error("Failed to get resale info: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Failed to get resale info: " + e.getMessage());
        }
    }

    /**
     * Get all tickets listed for resale for a specific event
     */
    public List<ResaleListingResponse> getResaleListingsForEvent(BigInteger eventId) {
        List<ResaleListingResponse> listings = new ArrayList<>();
        
        try {
            // Query Fabric for tickets listed for resale
            byte[] result = fabricContract.evaluateTransaction(
                    "queryTicketsForResaleByEvent", 
                    eventId.toString()
            );
            
            // The chaincode returns ChaincodeResponse<String> where payload is a JSON array string
            ChaincodeResponse<String> response = deserializeResponse(
                    result,
                    new com.owlike.genson.GenericType<ChaincodeResponse<String>>() {}
            );
            
            if ("SUCCESS".equals(response.getStatus()) && response.getPayload() != null) {
                // Parse the payload string as a list of FabricTicket
                String ticketsJson = response.getPayload();
                com.owlike.genson.Genson genson = new com.owlike.genson.Genson();
                List<FabricTicket> tickets = genson.deserialize(
                        ticketsJson, 
                        new com.owlike.genson.GenericType<List<FabricTicket>>() {}
                );
                
                for (FabricTicket ticket : tickets) {
                    if (ticket.isListedForResale() && ticket.getNftTokenId() != null) {
                        try {
                            BigInteger tokenId = new BigInteger(ticket.getNftTokenId());
                            Tuple6<Boolean, BigInteger, BigInteger, BigInteger, BigInteger, String> resaleInfo = 
                                    ticketNFT.getResaleInfo(tokenId).send();
                            
                            listings.add(ResaleListingResponse.builder()
                                    .tokenId(ticket.getNftTokenId())
                                    .eventId(ticket.getEventId())
                                    .seat(ticket.getSeat())
                                    .resalePriceWei(ticket.getResalePrice())
                                    .originalPriceWei(resaleInfo.component4().toString())
                                    .maxResalePriceWei(resaleInfo.component3().toString())
                                    .sellerAddress(ticket.getOwner())
                                    .sellerUsername(ticket.getUsername())
                                    .isListed(true)
                                    .build());
                        } catch (Exception e) {
                            log.warn("Failed to get blockchain resale info for token {}: {}", 
                                    ticket.getNftTokenId(), e.getMessage());
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            log.error("Failed to get resale listings for event {}: {}", eventId, e.getMessage(), e);
        }
        
        return listings;
    }

    /**
     * Check if a user is registered on the platform
     * Note: Registration is now handled at the application level, not blockchain
     * Platform users are considered registered when they have an account
     */
    public boolean isUserRegistered(String walletAddress) {
        // All platform users are considered registered since we removed on-chain registration
        // The security is now handled by blocking direct transfers and requiring platform transactions
        return walletAddress != null && !walletAddress.isEmpty();
    }

    private String findFabricTicketId(String nftTokenId) {
        // First, try to get from database (most reliable)
        try {
            java.util.Optional<String> fabricTicketId = eventSeatRepository.findFabricTicketIdByTokenId(nftTokenId);
            if (fabricTicketId.isPresent() && fabricTicketId.get() != null) {
                log.info("Found fabric ticket ID {} for NFT {} from database", fabricTicketId.get(), nftTokenId);
                return fabricTicketId.get();
            }
        } catch (Exception e) {
            log.warn("Failed to lookup fabric ticket ID from database for NFT {}: {}", nftTokenId, e.getMessage());
        }
        
        // Fallback: try chaincode query
        try {
            byte[] result = fabricContract.evaluateTransaction(
                    "queryTicketByNftIdAndOwner", 
                    nftTokenId, 
                    "0x0000000000000000000000000000000000000000" // Dummy address
            );
            
            String resultJson = new String(result, java.nio.charset.StandardCharsets.UTF_8);
            com.owlike.genson.Genson genson = new com.owlike.genson.Genson();
            java.util.Map<String, Object> responseMap = genson.deserialize(resultJson, java.util.Map.class);
            
            // Even with owner mismatch, if we get a payload, extract the ticketId
            if (responseMap.get("payload") != null) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> payload = (java.util.Map<String, Object>) responseMap.get("payload");
                String ticketId = (String) payload.get("ticketId");
                log.info("Found fabric ticket ID {} for NFT {} from chaincode", ticketId, nftTokenId);
                return ticketId;
            }
            
            log.warn("Could not find fabric ticket for NFT {}: {}", nftTokenId, responseMap.get("errorMessage"));
            return null;
        } catch (Exception e) {
            log.warn("Failed to find fabric ticket ID for NFT {}: {}", nftTokenId, e.getMessage());
            return null;
        }
    }
}
