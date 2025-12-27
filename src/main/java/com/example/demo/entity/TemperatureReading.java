package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "temperature_readings")
public class TemperatureReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double readingValue;
    private LocalDateTime recordedAt;

    @ManyToOne
    private SensorDevice sensor;

    @ManyToOne
    private ColdRoom coldRoom;
}
