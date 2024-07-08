package com.sparta.webfluxchat.controller;

import com.sparta.webfluxchat.entity.Greeting;
import com.sparta.webfluxchat.entity.HelloMessage;
import com.sparta.webfluxchat.entity.Message;
import com.sparta.webfluxchat.security.UserDetailsImpl;
import com.sparta.webfluxchat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RestController
public class GreetingController {

    private final ChatService chatService;
    private final Sinks.Many<Message> sink;

    @Autowired
    public GreetingController(ChatService chatService) {
        this.chatService = chatService;
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Mono<Message> greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000);
        Long roomNumber = 111L;
        return chatService.saveSocketMessage(message.getMessage(), roomNumber, 11L);
        //return new Greeting(HtmlUtils.htmlEscape(message.getName()) + ": " + HtmlUtils.htmlEscape(message.getMessage()));
    }
}
