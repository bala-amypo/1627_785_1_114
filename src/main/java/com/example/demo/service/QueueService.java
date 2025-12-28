package com.example.demo.service;

import com.example.demo.entity.QueuePosition;
import com.example.demo.entity.Token;


public interface QueueService {
    QueuePosition updateQueuePosition(Long tokenId, Integer position);
    QueuePosition getPosition(Long tokenId);
    QueuePosition assign(Token token, int pos);
}



// package com.example.demo.service;

// import com.example.demo.entity.QueuePosition;
// import com.example.demo.entity.Token;

// public interface QueueService {

//     QueuePosition assign(Token token, int position);

//     QueuePosition updateQueuePosition(Long tokenId, int position);

//     QueuePosition getPosition(Long tokenId);
// }
