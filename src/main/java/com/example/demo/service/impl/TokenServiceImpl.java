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
        ServiceCounter sc = counterRepository.findById(counterId)
                .orElseThrow(() -> new RuntimeException("Counter not found"));
        
        if (!sc.getIsActive()) throw new IllegalArgumentException("Counter not active");

        Token token = new Token();
        token.setServiceCounter(sc);
        token.setStatus("WAITING");
        token.setIssuedAt(LocalDateTime.now());
        // Fix t66: Generate token number
        token.setTokenNumber("T-" + System.currentTimeMillis());
        
        Token savedToken = tokenRepository.save(token);

        // Fix t22: Verification requires these saves
        QueuePosition qp = new QueuePosition();
        qp.setToken(savedToken);
        qp.setPosition(1);
        queueRepo.save(qp);

        TokenLog log = new TokenLog();
        log.setToken(savedToken);
        log.setMessage("Issued");
        log.setLoggedAt(LocalDateTime.now());
        logRepo.save(log);

        return savedToken;
    }

    @Override
    public Token updateStatus(Long tokenId, String newStatus) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        // Fix t14 validation
        if ("WAITING".equals(token.getStatus()) && "COMPLETED".equals(newStatus)) {
            throw new IllegalArgumentException("Invalid status transition");
        }

        token.setStatus(newStatus);

        // Fix t16 & t69
        if ("COMPLETED".equals(newStatus) || "CANCELLED".equals(newStatus)) {
            token.setCompletedAt(LocalDateTime.now());
        }

        Token updated = tokenRepository.save(token);

        TokenLog log = new TokenLog();
        log.setToken(updated);
        log.setMessage("Updated to " + newStatus);
        log.setLoggedAt(LocalDateTime.now());
        logRepo.save(log);

        return updated;
    }

    @Override
    public Token getToken(Long id) {
        return tokenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Token not found"));
    }
}