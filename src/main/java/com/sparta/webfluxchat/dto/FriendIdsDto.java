package com.sparta.webfluxchat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FriendIdsDto {
    private List<Long> ids;
}
