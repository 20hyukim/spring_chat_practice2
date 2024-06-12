package com.sparta.webfluxchat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendDto {
    private String username;
    private String imageUrl;

    public FriendDto(String username, String imageUrl) {
        this.username = username;
        this.imageUrl = imageUrl;
    }
}
