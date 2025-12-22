// package com.example.demo.service.impl;

// import java.util.List;

// import org.springframework.stereotype.Service;

// import com.example.demo.entity.ServiceCounter;
// import com.example.demo.repository.ServiceCounterRepository;
// import com.example.demo.service.ServiceCounterService;

// @Service
// public class ServiceCounterServiceImpl implements ServiceCounterService {

//     private final ServiceCounterRepository serviceCounterRepository;

//     // Constructor injection
//     public ServiceCounterServiceImpl(ServiceCounterRepository serviceCounterRepository) {
//         this.serviceCounterRepository = serviceCounterRepository;
//     }

//     @Override
//     public ServiceCounter addCounter(ServiceCounter counter) {
//         return serviceCounterRepository.save(counter);
//     }

//     @Override
//     public List<ServiceCounter> getActiveCounters() {
//         return serviceCounterRepository.findByIsActiveTrue();
//     }
// }