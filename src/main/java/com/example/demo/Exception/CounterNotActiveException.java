package com.example.demo.exception;

public class CounterNotActiveException extends RuntimeException {
    public CounterNotActiveException(String message) {
        super(message);
    }
}