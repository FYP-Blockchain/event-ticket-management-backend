package com.ruhuna.event_ticket_management_system.config;

import io.ipfs.api.IPFS;
import io.ipfs.multiaddr.MultiAddress;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IPFSConfig {

    @Value("${ipfs.rpc.api.address}")
    private String address;

    @Getter
    @Value("${ipfs.gateway.url:http://localhost:8082/ipfs/}")
    private String ipfsGatewayUrl;

    @Bean
    public IPFS ipfs() throws Exception {
        return new IPFS(new MultiAddress(address));
    }
}

