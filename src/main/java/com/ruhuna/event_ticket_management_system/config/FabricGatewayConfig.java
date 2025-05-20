package com.ruhuna.event_ticket_management_system.config;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.TlsChannelCredentials;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.Hash;
import org.hyperledger.fabric.client.Network;
import org.hyperledger.fabric.client.identity.Identity;
import org.hyperledger.fabric.client.identity.X509Identity;
import org.hyperledger.fabric.client.identity.Identities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.hyperledger.fabric.client.identity.Signer;
import org.hyperledger.fabric.client.identity.Signers;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FabricGatewayConfig {

    @Value("${fabric.mspId}")
    private String mspId;

    @Value("${fabric.channelName}")
    private String channelName;

    @Value("${fabric.chaincodeName}")
    private String chaincodeName;

    @Value("${fabric.cryptoPath}")
    private String cryptoPath;

    @Value("${fabric.peerEndpoint}")
    private String peerEndpoint;

    @Value("${fabric.overrideAuth}")
    private String overrideAuth;

    @Bean
    public Gateway fabricGateway() throws Exception {
        Path cryptoBasePath = Paths.get(cryptoPath);
        Path certPath = Files.list(cryptoBasePath.resolve("users/User1@org1.example.com/msp/signcerts"))
                .findFirst().orElseThrow();
        Path keyPath = Files.list(cryptoBasePath.resolve("users/User1@org1.example.com/msp/keystore"))
                .findFirst().orElseThrow();
        Path tlsCertPath = cryptoBasePath.resolve("peers/peer0.org1.example.com/tls/ca.crt");

        Identity identity;
        try (Reader certReader = Files.newBufferedReader(certPath)) {
            identity = new X509Identity(mspId, Identities.readX509Certificate(certReader));
        }

        Signer signer;
        try (Reader keyReader = Files.newBufferedReader(keyPath)) {
            signer = Signers.newPrivateKeySigner(Identities.readPrivateKey(keyReader));
        }

        TlsChannelCredentials tlsCredentials = (TlsChannelCredentials) TlsChannelCredentials.newBuilder()
                .trustManager(tlsCertPath.toFile())
                .build();

        ManagedChannel channel = Grpc.newChannelBuilder(peerEndpoint, tlsCredentials)
                .overrideAuthority(overrideAuth)
                .build();

        return Gateway.newInstance()
                .identity(identity)
                .signer(signer)
                .connection(channel)
                .hash(Hash.SHA256)
                .connect();
    }

    @Bean
    public Contract fabricContract(Gateway gateway) {
        Network network = gateway.getNetwork(channelName);
        return network.getContract(chaincodeName);
    }
}

