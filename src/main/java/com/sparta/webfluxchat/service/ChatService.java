package com.sparta.webfluxchat.service;

import com.sparta.webfluxchat.entity.Message;
import com.sparta.webfluxchat.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Transactional
    public Mono<Message> saveMessage(String message, Long roomnumber, UserDetailsImpl userDetails) {
        Message msg = new Message(message, System.currentTimeMillis(), userDetails.getUser().getId(), userDetails.getUser().getUsername());
        String collectionName = "messages_room_" + roomnumber;
        return reactiveMongoTemplate.save(msg, collectionName);
    }

    @Transactional
    public Flux<Message> findMessagesByRoomId(Long roomId) {
        String collectionName = "messages_room_" + roomId;
        logger.info("Finding messages in collection: {}", collectionName);
        return reactiveMongoTemplate.findAll(Message.class, collectionName);
    }
}