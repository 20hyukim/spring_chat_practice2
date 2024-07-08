package com.sparta.webfluxchat.service;

import com.sparta.webfluxchat.entity.Message;
import com.sparta.webfluxchat.entity.User;
import com.sparta.webfluxchat.repository.UserRepository;
import com.sparta.webfluxchat.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final UserRepository userRepository;
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
    public Mono<Message> saveSocketMessage(String message, Long roomnumber, Long id) {

        log.info("saveSocktMessage called with message: {}, roomnumber : {}, id: {}", message, roomnumber, id);
        User user = userRepository.findById(id).orElseThrow();

        Message msg = new Message(message, System.currentTimeMillis(), id, user.getUsername());
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