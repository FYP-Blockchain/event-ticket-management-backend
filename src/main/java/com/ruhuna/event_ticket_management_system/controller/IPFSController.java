package com.ruhuna.event_ticket_management_system.controller;

import com.ruhuna.event_ticket_management_system.service.IPFSService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ipfs")
@RequiredArgsConstructor
public class IPFSController {

    private final IPFSService ipfsService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadJsonContent(@RequestParam String jsonContent) {
        String ipfsHash = ipfsService.addJson(jsonContent);
        return ResponseEntity.ok(ipfsHash);
    }

    @GetMapping(value = "/files/{hash}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getFile(@PathVariable String hash) {
        byte[] fileBytes = ipfsService.getFile(hash);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + hash + "\"")
                .body(fileBytes);
    }
}
