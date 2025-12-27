package com.example.demo.exception;

public class InvalidTokenStatusException extends RuntimeException {
    public InvalidTokenStatusException(String message) {
        super(message);
    }
}