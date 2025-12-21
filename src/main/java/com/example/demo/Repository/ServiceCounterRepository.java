package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ServiceCounter;

@Repository
public interface ServiceCounterRepository
        extends JpaRepository<ServiceCounter, Long> {

    // Returns all active service counters
    List<ServiceCounter> findByIsActiveTrue();
}