package com.ruhuna.event_ticket_management_system.service;

import com.ruhuna.event_ticket_management_system.config.IPFSConfig;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class IPFSService {

    private final IPFS ipfs;
    private final IPFSConfig ipfsConfig;

    public String addJson(String jsonContent) {
        try {
            InputStream inputStream = new ByteArrayInputStream(jsonContent.getBytes());
            NamedStreamable namedStreamable = new NamedStreamable.InputStreamWrapper(inputStream);
            MerkleNode response = ipfs.add(namedStreamable).get(0);

            log.info("Hash (base 58): {} - {}", response.name.orElse(""), response.hash.toBase58());
            return response.hash.toBase58();
        } catch (IOException ex) {
            throw new RuntimeException("Error whilst communicating with the IPFS node", ex);
        }
    }

    public byte[] getFile(String hash) {
        try {
            Multihash filePointer = Multihash.fromBase58(hash);
            return ipfs.cat(filePointer);
        } catch (IOException ex) {
            throw new RuntimeException("Error whilst communicating with the IPFS node", ex);
        }
    }

    public String getFullUrl(String ipfsUri) {
        if (ipfsUri == null || !ipfsUri.startsWith("ipfs://")) {
            return null;
        }
        String cid = ipfsUri.substring("ipfs://".length());
        return ipfsConfig.getIpfsGatewayUrl() + cid;
    }

    public String addFile(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            NamedStreamable namedStreamable = new NamedStreamable.InputStreamWrapper(inputStream);
            MerkleNode response = ipfs.add(namedStreamable).get(0);
            String cid = response.hash.toBase58();
            log.info("Uploaded file to IPFS. CID: {}", cid);
            return cid;
        } catch (IOException ex) {
            throw new RuntimeException("Error whilst communicating with the IPFS node", ex);
        }
    }
}
