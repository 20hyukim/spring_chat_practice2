package com.sparta.webfluxchat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendDto {
    private String name;
    private String imageUrl;

    public FriendDto(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
