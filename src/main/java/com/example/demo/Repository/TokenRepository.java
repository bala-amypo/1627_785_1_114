package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.BreachAlert;

public interface TokenRepository extends JpaRepository<BreachAlert, Long> {
    Optional<BreachAlert> findByTokenNumber(String tokenNumber);
}
