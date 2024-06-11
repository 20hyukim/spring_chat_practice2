package com.sparta.webfluxchat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendDto {
    private Long userId;
    private Long friendId;

    public FriendDto(Long friendId, Long myId) {
        this.userId = myId;
        this.friendId = friendId;
    }
}
