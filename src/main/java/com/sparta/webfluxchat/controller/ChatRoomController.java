package com.sparta.webfluxchat.controller;

import com.sparta.webfluxchat.dto.FriendIdsDto;
import com.sparta.webfluxchat.entity.Friend;
import com.sparta.webfluxchat.security.UserDetailsImpl;
import com.sparta.webfluxchat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/chatroom")
    public ResponseEntity<String> createChatRoom(@RequestParam String chatName,
                                                 @RequestBody FriendIdsDto friendIdsDto,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.createChatRoom(chatName, userDetails.getUser().getId(), friendIdsDto);
    }
}
