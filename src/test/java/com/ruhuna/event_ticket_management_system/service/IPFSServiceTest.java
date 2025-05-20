package com.ruhuna.event_ticket_management_system.service;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class IPFSServiceTest {

    @InjectMocks
    private IPFSService ipfsService;

    @Mock
    private IPFS mockIpfs;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        var ipfsField = IPFSService.class.getDeclaredField("ipfs");
        ipfsField.setAccessible(true);
        ipfsField.set(ipfsService, mockIpfs);
    }

    @Test
    void testAddFile_Success() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        byte[] fileContent = "dummy content".getBytes();
        when(mockFile.getBytes()).thenReturn(fileContent);

        MerkleNode mockNode = new MerkleNode("QmT78zSuBmuS4z925W1pYvSGJoZAgjHgDBmTtT4DbDeZcB",
                Optional.of("test.txt"));
        when(mockIpfs.add(any(NamedStreamable.class))).thenReturn(Collections.singletonList(mockNode));

        String result = ipfsService.addFile(mockFile);

        assertEquals("QmT78zSuBmuS4z925W1pYvSGJoZAgjHgDBmTtT4DbDeZcB", result);
        verify(mockIpfs, times(1)).add(any(NamedStreamable.class));
    }

    @Test
    void testAddFile_ShouldThrowRuntimeException() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenThrow(new IOException("IPFS Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ipfsService.addFile(mockFile);
        });

        assertTrue(exception.getMessage().contains("Error whilst communicating with the IPFS node"));
    }

    @Test
    void testGetFile_Success() throws IOException {
        String testHash = "QmT78zSuBmuS4z925W1pYvSGJoZAgjHgDBmTtT4DbDeZcB";
        byte[] expectedContent = "file data".getBytes();
        Multihash multihash = Multihash.fromBase58(testHash);

        when(mockIpfs.cat(multihash)).thenReturn(expectedContent);

        byte[] actual = ipfsService.getFile(testHash);

        assertArrayEquals(expectedContent, actual);
        verify(mockIpfs, times(1)).cat(multihash);
    }

    @Test
    void testGetFile_ShouldThrowRuntimeException() throws IOException {
        String testHash = "QmT78zSuBmuS4z925W1pYvSGJoZAgjHgDBmTtT4DbDeZcB";
        Multihash multihash = Multihash.fromBase58(testHash);

        when(mockIpfs.cat(multihash)).thenThrow(new IOException("File not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ipfsService.getFile(testHash);
        });

        assertTrue(exception.getMessage().contains("Error whilst communicating with the IPFS node"));
    }
}