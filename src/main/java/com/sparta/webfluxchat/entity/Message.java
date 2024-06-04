package com.sparta.webfluxchat.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Message {

    @Id
    private String id;
    private String content;
    private long timestamp;
    private Long userId;
    private String username;

    public Message(String content, long timestamp, Long id, String username) {
        this.content = content;
        this.timestamp = timestamp;
        this.userId = id;
        this.username = username;
    }

}
