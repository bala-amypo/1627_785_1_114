package com.example.demo.util;

public class TokenNumberGenerator {
    public static String generate(String prefix) {
        return prefix + "-" + System.currentTimeMillis() % 1000;
    }
}