package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sensor_devices", uniqueConstraints = @UniqueConstraint(columnNames = "identifier"))
public class SensorDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identifier;
    private Boolean isActive;

    @ManyToOne
    private ColdRoom coldRoom;

    public SensorDevice() {}

    public SensorDevice(String identifier, ColdRoom coldRoom, Boolean isActive) {
        this.identifier = identifier;
        this.coldRoom = coldRoom;
        this.isActive = isActive;
    }
}
