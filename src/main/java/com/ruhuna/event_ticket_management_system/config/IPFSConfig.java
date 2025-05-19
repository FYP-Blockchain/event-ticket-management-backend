package com.ruhuna.event_ticket_management_system.config;

import io.ipfs.api.IPFS;
import io.ipfs.multiaddr.MultiAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IPFSConfig {

    @Value("${ipfs.rpc.api.address}")
    private String address;

    @Bean
    public IPFS ipfs() throws Exception {
        return new IPFS(new MultiAddress(address));
    }
}

