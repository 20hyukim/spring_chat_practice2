package com.sparta.webfluxchat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendDto {
    private Long id;
    private String name;
    private String imageUrl;

    public FriendDto(Long id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
