package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tokens")
public class TokenController {
    
    @Autowired
    private TokenServiceImpl tokenService;
    
    @Autowired
    private ServiceCounterServiceImpl counterService;
    
    @Autowired
    private QueueServiceImpl queueService;
    
    @Autowired
    private TokenLogServiceImpl logService;
    
    @PostMapping("/issue/{counterId}")
    public ResponseEntity<Token> issueToken(@PathVariable Long counterId) {
        Token token = tokenService.issueToken(counterId);
        return ResponseEntity.ok(token);
    }
    
    @GetMapping("/{tokenId}")
    public ResponseEntity<Token> getToken(@PathVariable Long tokenId) {
        Token token = tokenService.getToken(tokenId);
        return ResponseEntity.ok(token);
    }
    
    @PutMapping("/{tokenId}/status")
    public ResponseEntity<Token> updateStatus(@PathVariable Long tokenId, @RequestParam String status) {
        Token token = tokenService.updateStatus(tokenId, status);
        return ResponseEntity.ok(token);
    }
    
    @GetMapping("/counters")
    public ResponseEntity<List<ServiceCounter>> getActiveCounters() {
        List<ServiceCounter> counters = counterService.getActiveCounters();
        return ResponseEntity.ok(counters);
    }
    
    @PostMapping("/counters")
    public ResponseEntity<ServiceCounter> addCounter(@RequestBody ServiceCounter counter) {
        ServiceCounter created = counterService.addCounter(counter);
        return ResponseEntity.ok(created);
    }
    
    @GetMapping("/{tokenId}/logs")
    public ResponseEntity<List<TokenLog>> getTokenLogs(@PathVariable Long tokenId) {
        List<TokenLog> logs = logService.getLogs(tokenId);
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/{tokenId}/position")
    public ResponseEntity<QueuePosition> getQueuePosition(@PathVariable Long tokenId) {
        QueuePosition position = queueService.getPosition(tokenId);
        return ResponseEntity.ok(position);
    }
}