package com.ruhuna.event_ticket_management_system.utils;

import org.springframework.stereotype.Component;
import org.web3j.crypto.Hash;

import java.nio.charset.StandardCharsets;

@Component
public class CryptoUtils {

    public static byte[] calculateCommitmentHash(String ipfsCid, String secretNonce) {
        byte[] cidHash = Hash.sha3(ipfsCid.getBytes(StandardCharsets.UTF_8));
        byte[] nonceHash = Hash.sha3(secretNonce.getBytes(StandardCharsets.UTF_8));
        byte[] combined = new byte[cidHash.length + nonceHash.length];
        System.arraycopy(cidHash, 0, combined, 0, cidHash.length);
        System.arraycopy(nonceHash, 0, combined, cidHash.length, nonceHash.length);
        return Hash.sha3(combined);
    }
}
