package com.example.demo.service;

import com.example.demo.entity.QueuePosition;

public interface QueueService {
    QueuePosition updateQueuePosition(Long tokenId, Integer position);
    QueuePosition getPosition(Long tokenId);
}






package com.example.demo.service;

import com.example.demo.entity.ServiceCounter;
import java.util.List;

public interface ServiceCounterService {
    ServiceCounter addCounter(ServiceCounter counter);
    List<ServiceCounter> getActiveCounters();
}

   














package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService {
    User register(User user);
    User findByEmail(String email);
}






