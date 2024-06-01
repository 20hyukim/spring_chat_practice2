package com.sparta.webfluxchat.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Greeting {
    private String content;

    public Greeting(String content) {
        this.content = content;
    }
}
