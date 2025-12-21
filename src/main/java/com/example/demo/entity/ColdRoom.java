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
@Table(name = "cold_rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColdRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location;

    private Double minAllowed;

    private Double maxAllowed;

    // Parameterized constructor WITHOUT id (as per requirement)
    public ColdRoom(String name, String location, Double minAllowed, Double maxAllowed) {
        this.name = name;
        this.location = location;
        this.minAllowed = minAllowed;
        this.maxAllowed = maxAllowed;
    }
}