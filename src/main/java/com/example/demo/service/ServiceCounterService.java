ServiceCounterService.java
package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.ServiceCounter;

public interface ServiceCounterService {

    ServiceCounter addCounter(ServiceCounter counter);

    List<ServiceCounter> getActiveCounters();
}