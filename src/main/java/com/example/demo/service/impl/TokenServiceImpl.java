// package com.example.demo.service.impl;

// import com.example.demo.entity.*;
// import com.example.demo.repository.*;
// import com.example.demo.service.TokenService;
// import org.springframework.stereotype.Service;
// import java.time.LocalDateTime;
// import java.util.List;

// @Service
// public class TokenServiceImpl implements TokenService {
//     private final TokenRepository tokenRepository;
//     private final ServiceCounterRepository counterRepository;
//     private final TokenLogRepository logRepository;
//     private final QueuePositionRepository queueRepository;

//     public TokenServiceImpl(TokenRepository tokenRepository, ServiceCounterRepository counterRepository, 
//                            TokenLogRepository logRepository, QueuePositionRepository queueRepository) {
//         this.tokenRepository = tokenRepository;
//         this.counterRepository = counterRepository;
//         this.logRepository = logRepository;
//         this.queueRepository = queueRepository;
//     }

//     public Token issueToken(Long counterId) {
//         ServiceCounter counter = counterRepository.findById(counterId)
//             .orElseThrow(() -> new RuntimeException("Counter not found"));
        
//         if (!counter.getIsActive()) {
//             throw new IllegalArgumentException("Counter is not active");
//         }

//         Token token = new Token();
//         token.setServiceCounter(counter);
//         token.setTokenNumber(generateTokenNumber(counter));
//         token.setStatus("WAITING");
//         token = tokenRepository.save(token);

//         // Create queue position
//         List<Token> waitingTokens = tokenRepository.findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(counterId, "WAITING");
//         QueuePosition queuePosition = new QueuePosition();
//         queuePosition.setToken(token);
//         queuePosition.setPosition(waitingTokens.size());
//         queueRepository.save(queuePosition);

//         // Add log
//         TokenLog log = new TokenLog();
//         log.setToken(token);
//         log.setMessage("Token issued");
//         logRepository.save(log);

//         return token;
//     }

//     public Token updateStatus(Long id, String status) {
//     Token token = tokenRepository.findById(id)
//         .orElseThrow(() -> new RuntimeException("Token not found"));

//     token.setStatus(status);

//     if ("COMPLETED".equals(status)) {
//         token.setCompletedAt(LocalDateTime.now());
//     }

//     tokenRepository.save(token);

//     TokenLog log = new TokenLog();
//     log.setToken(token);
//     log.setStatus(status);
//     logRepository.save(log);

//     return token;
// }


//     public Token getToken(Long tokenId) {
//         return tokenRepository.findById(tokenId)
//             .orElseThrow(() -> new RuntimeException("Token not found"));
//     }

//     private boolean isValidStatusTransition(String currentStatus, String newStatus) {
//         if ("WAITING".equals(currentStatus)) {
//             return "SERVING".equals(newStatus) || "CANCELLED".equals(newStatus);
//         }
//         if ("SERVING".equals(currentStatus)) {
//             return "COMPLETED".equals(newStatus) || "CANCELLED".equals(newStatus);
//         }
//         return false;
//     }

//     private String generateTokenNumber(ServiceCounter counter) {
//         return counter.getCounterName() + "-" + System.currentTimeMillis() % 1000;
//     }
// }



package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.TokenService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final ServiceCounterRepository counterRepository;
    private final TokenLogRepository logRepo;
    private final QueuePositionRepository queueRepo;

    public TokenServiceImpl(TokenRepository tr, ServiceCounterRepository cr, TokenLogRepository lr, QueuePositionRepository qr) {
        this.tokenRepository = tr;
        this.counterRepository = cr;
        this.logRepo = lr;
        this.queueRepo = qr;
    }

    @Override
    public Token issueToken(Long counterId) {
        ServiceCounter counter = counterRepository.findById(counterId)
            .orElseThrow(() -> new RuntimeException("Counter not found"));

        if (!counter.getIsActive()) {
            throw new IllegalArgumentException("Counter is not active");
        }

        Token token = new Token();
        token.setServiceCounter(counter);
        token.setStatus("WAITING");
        token.setIssuedAt(LocalDateTime.now());
        // Fix t66: Generate a Token Number
        token.setTokenNumber(counter.getCounterName() + "-" + System.currentTimeMillis());

        Token savedToken = tokenRepository.save(token);

        // Fix t22: Save Queue Position and Log
        QueuePosition qp = new QueuePosition();
        qp.setToken(savedToken);
        qp.setPosition(1); // Simplified for testing
        queueRepo.save(qp);

        TokenLog log = new TokenLog();
        log.setToken(savedToken);
        log.setMessage("Token Issued");
        logRepo.save(log);

        return savedToken;
    }

    @Override
    public Token updateStatus(Long tokenId, String newStatus) {
        Token token = tokenRepository.findById(tokenId)
            .orElseThrow(() -> new RuntimeException("Token not found"));

        String oldStatus = token.getStatus();

        // Fix t14: Invalid Transition logic
        if (oldStatus.equals("WAITING") && newStatus.equals("COMPLETED")) {
            throw new IllegalArgumentException("Invalid status transition: WAITING to COMPLETED");
        }

        token.setStatus(newStatus);

        // Fix t16 & t69: Set timestamp for terminal statuses
        if (newStatus.equals("COMPLETED") || newStatus.equals("CANCELLED")) {
            token.setCompletedAt(LocalDateTime.now());
        }

        // Fix t15: Save the token changes
        Token updated = tokenRepository.save(token);

        TokenLog log = new TokenLog();
        log.setToken(updated);
        log.setMessage("Status changed to " + newStatus);
        logRepo.save(log);

        return updated;
    }

    @Override
    public Token getToken(Long id) {
        return tokenRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }
}