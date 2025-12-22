package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.ColdRoom;
import com.example.demo.repository.ColdRoomRepository;
import com.example.demo.service.ColdRoomService;

@Service
public class ColdRoomServiceImpl implements ColdRoomService {

    private final ColdRoomRepository coldRoomRepository;

    // Constructor injection
    public ColdRoomServiceImpl(ColdRoomRepository coldRoomRepository) {
        this.coldRoomRepository = coldRoomRepository;
    }

    @Override
    public ColdRoom addColdRoom(ColdRoom coldRoom) {

        // Validation: thresholds must not be null
        if (coldRoom.getMinAllowed() == null || coldRoom.getMaxAllowed() == null) {
            throw new IllegalArgumentException("range");
        }

        // Validation: minAllowed < maxAllowed
        if (coldRoom.getMinAllowed() >= coldRoom.getMaxAllowed()) {
            throw new IllegalArgumentException("range");
        }

        return coldRoomRepository.save(coldRoom);
    }

    @Override
    public List<ColdRoom> getAllColdRooms() {
        return coldRoomRepository.findAll();
    }
}