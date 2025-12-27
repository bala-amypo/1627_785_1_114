package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.TokenService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final ServiceCounterRepository counterRepository;
    private final TokenLogRepository logRepository;
    private final QueuePositionRepository queueRepository;

    public TokenServiceImpl(TokenRepository tokenRepository, ServiceCounterRepository counterRepository, 
                           TokenLogRepository logRepository, QueuePositionRepository queueRepository) {
        this.tokenRepository = tokenRepository;
        this.counterRepository = counterRepository;
        this.logRepository = logRepository;
        this.queueRepository = queueRepository;
    }

    public Token issueToken(Long counterId) {
        ServiceCounter counter = counterRepository.findById(counterId)
            .orElseThrow(() -> new RuntimeException("Counter not found"));
        
        if (!counter.getIsActive()) {
            throw new IllegalArgumentException("Counter is not active");
        }

        Token token = new Token();
        token.setServiceCounter(counter);
        token.setTokenNumber(generateTokenNumber(counter));
        token.setStatus("WAITING");
        token = tokenRepository.save(token);

        // Create queue position
        List<Token> waitingTokens = tokenRepository.findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(counterId, "WAITING");
        QueuePosition queuePosition = new QueuePosition();
        queuePosition.setToken(token);
        queuePosition.setPosition(waitingTokens.size());
        queueRepository.save(queuePosition);

        // Add log
        TokenLog log = new TokenLog();
        log.setToken(token);
        log.setMessage("Token issued");
        logRepository.save(log);

        return token;
    }

    public Token updateStatus(Long id, String status) {
    Token token = tokenRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Token not found"));

    token.setStatus(status);

    if ("COMPLETED".equals(status)) {
        token.setCompletedAt(LocalDateTime.now());
    }

    tokenRepository.save(token);

    TokenLog log = new TokenLog();
    log.setToken(token);
    log.setStatus(status);
    logRepository.save(log);

    return token;
}


    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
            .orElseThrow(() -> new RuntimeException("Token not found"));
    }

    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        if ("WAITING".equals(currentStatus)) {
            return "SERVING".equals(newStatus) || "CANCELLED".equals(newStatus);
        }
        if ("SERVING".equals(currentStatus)) {
            return "COMPLETED".equals(newStatus) || "CANCELLED".equals(newStatus);
        }
        return false;
    }

    private String generateTokenNumber(ServiceCounter counter) {
        return counter.getCounterName() + "-" + System.currentTimeMillis() % 1000;
    }
}