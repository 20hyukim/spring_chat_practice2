package com.sparta.webfluxchat.controller;

import com.sparta.webfluxchat.entity.Message;
import com.sparta.webfluxchat.security.UserDetailsImpl;
import com.sparta.webfluxchat.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
public class ChatController {
    //
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final Sinks.Many<Message> sink;
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> streamMessages(@RequestParam("roomId") Long roomId) {
        return chatService.findMessagesByRoomId(roomId).mergeWith(sink.asFlux().delayElements(Duration.ofMillis(200)));

    }

    @GetMapping("/send")
    public Mono<Void> sendMessage(@RequestParam String message,
                                  @RequestParam Long roomnumber,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.saveMessage(message, roomnumber, userDetails)
                .doOnNext(msg -> sink.tryEmitNext(msg))
                .then();
    }
}