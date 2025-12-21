package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "service_counters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Used by findByIsActiveTrue()
    private boolean isActive;

    // Parameterized constructor without id
    public ServiceCounter(String name, boolean isActive) {
        this.name = name;
        this.isActive = isActive;
    }
}