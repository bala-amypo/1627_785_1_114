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
import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final ServiceCounterRepository counterRepository;
    private final TokenLogRepository logRepository;
    private final QueuePositionRepository queueRepository;

    public TokenServiceImpl(TokenRepository tokenRepository,
                            ServiceCounterRepository counterRepository,
                            TokenLogRepository logRepository,
                            QueuePositionRepository queueRepository) {
        this.tokenRepository = tokenRepository;
        this.counterRepository = counterRepository;
        this.logRepository = logRepository;
        this.queueRepository = queueRepository;
    }

    @Override
    public Token issueToken(Long counterId) {

        ServiceCounter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new RuntimeException("Counter not found"));

        if (!Boolean.TRUE.equals(counter.getIsActive())) {
            throw new IllegalArgumentException("Counter not active");
        }

        List<Token> waiting =
                tokenRepository.findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(counterId, "WAITING");

        int seq = waiting.size() + 1;

        Token token = new Token();
        token.setServiceCounter(counter);
        token.setStatus("WAITING");
        token.setIssuedAt(LocalDateTime.now());
        token.setTokenNumber(counter.getCounterName() + "-" + seq);

        Token saved = tokenRepository.save(token);

        QueuePosition qp = new QueuePosition();
        qp.setToken(saved);
        qp.setPosition(seq);
        queueRepository.save(qp);

        TokenLog log = new TokenLog();
        log.setToken(saved);
        log.setMessage("Token issued");
        logRepository.save(log);

        return saved;
    }

    @Override
    public Token updateStatus(Long tokenId, String newStatus) {

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        String current = token.getStatus();

        if (newStatus.equals("SERVING") && !current.equals("WAITING"))
            throw new IllegalArgumentException("Invalid status");

        if (newStatus.equals("COMPLETED") && !current.equals("SERVING"))
            throw new IllegalArgumentException("Invalid status");

        if (newStatus.equals("CANCELLED") && !current.equals("WAITING"))
            throw new IllegalArgumentException("Invalid status");

        token.setStatus(newStatus);

        if (newStatus.equals("COMPLETED") || newStatus.equals("CANCELLED")) {
            token.setCompletedAt(LocalDateTime.now());
        }

        Token saved = tokenRepository.save(token);

        TokenLog log = new TokenLog();
        log.setToken(saved);
        log.setMessage("Status changed to " + newStatus);
        logRepository.save(log);

        return saved;
    }

    @Override
    public Token getToken(Long id) {
        return tokenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Token not found"));
    }
}
