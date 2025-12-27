package com.example.demo.service;

import com.example.demo.entity.Token;

public interface TokenService {
    Token issueToken(Long counterId);
    Token updateStatus(Long tokenId, String newStatus);
    Token getToken(Long tokenId);
}