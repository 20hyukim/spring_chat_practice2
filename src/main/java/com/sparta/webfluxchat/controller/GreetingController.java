package com.sparta.webfluxchat.controller;

import com.sparta.webfluxchat.entity.Greeting;
import com.sparta.webfluxchat.entity.HelloMessage;
import com.sparta.webfluxchat.entity.Message;
import com.sparta.webfluxchat.security.UserDetailsImpl;
import com.sparta.webfluxchat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.awt.*;
import java.time.Duration;

@RestController
@Slf4j
public class GreetingController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final Sinks.Many<Message> sink;

    @Autowired
    public GreetingController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping(  "/history")
    public Flux<Message> getChatHistory(@RequestParam Long roomId) {
        log.info("getChatHistory-Controller called!!!!");
        return chatService.getChatHistory(roomId).mergeWith(sink.asFlux().delayElements(Duration.ofMillis(200)));
    }

    @MessageMapping("/hello")
    public Mono<Message> greeting(HelloMessage message) {

        Long roomId = message.getRoomId();
        Long userId = message.getUserId();

        log.info("greeting called for message: {}", message);

        Mono<Message> savedMessage = chatService.saveSocketMessage(message.getMessage(), roomId, userId).cache();

        savedMessage.subscribe(msg -> {
            log.info("Sending message: {}", msg);
            messagingTemplate.convertAndSend("/topic/greetings/" + roomId, msg);
        });
        return savedMessage;
    }
}
