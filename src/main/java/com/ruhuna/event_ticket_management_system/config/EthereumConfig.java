package com.ruhuna.event_ticket_management_system.config;

import com.ruhuna.event_ticket_management_system.contracts.EventManager;
import com.ruhuna.event_ticket_management_system.contracts.TicketNFT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.net.ConnectException;
import java.net.URISyntaxException;

@Configuration
public class EthereumConfig {

    @Value("${ethereum.node-url}")
    private String nodeUrl;

    @Value("${ethereum.operator-private-key}")
    private String operatorPrivateKey;

    @Value("${ethereum.chain-id}")
    private Long chainId;

    @Value("${ethereum.gas-price}")
    private BigInteger gasPrice;

    @Value("${ethereum.gas-limit}")
    private BigInteger gasLimit;

    @Value("${ethereum.event-manager-address}")
    private String eventManagerAddress;

    @Value("${ethereum.ticket-manager-address}")
    private String ticketManagerAddress;

    @Bean
    public Web3j web3j() throws ConnectException, URISyntaxException {
        if (nodeUrl.startsWith("ws")) {
            WebSocketService webSocketService = new WebSocketService(nodeUrl, true);
            webSocketService.connect();
            return Web3j.build(webSocketService);
        } else {
            return Web3j.build(new HttpService(nodeUrl));
        }
    }

    @Bean
    public Credentials credentials() {
        return Credentials.create(operatorPrivateKey);
    }

    @Bean
    public TransactionManager fabricTransactionManager(Web3j web3j, Credentials credentials) {
        return new RawTransactionManager(web3j, credentials, chainId);
    }

    @Bean
    public StaticGasProvider gasProvider() {
        return new StaticGasProvider(gasPrice, gasLimit);
    }

    @Bean
    public EventManager eventManager(Web3j web3j, TransactionManager transactionManager, StaticGasProvider gasProvider) {
        return EventManager.load(
                eventManagerAddress,
                web3j,
                transactionManager,
                gasProvider
        );
    }

    @Bean
    public TicketNFT ticketNFT(Web3j web3j, TransactionManager transactionManager, StaticGasProvider gasProvider) {
        return TicketNFT.load(
                ticketManagerAddress,
                web3j,
                transactionManager,
                gasProvider
        );
    }
}
