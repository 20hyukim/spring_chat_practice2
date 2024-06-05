package com.sparta.webfluxchat.service;

import com.sparta.webfluxchat.entity.ChatRoom;
import com.sparta.webfluxchat.entity.ChatRoomUser;
import com.sparta.webfluxchat.repository.ChatRoomRepository;
import com.sparta.webfluxchat.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private static final Logger logger = LoggerFactory.getLogger(ChatRoomService.class);

    @Transactional
    public ResponseEntity<String> createChatRoom(String chatName) {
        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findByName(chatName);
        if (existingChatRoom.isPresent()) {
            throw new IllegalArgumentException("Error: 중복된 채팅방 이름이 존재합니다.");
        }
        ChatRoom chatRoom = new ChatRoom(chatName);
        chatRoomRepository.save(chatRoom);

        return ResponseEntity.ok("성공적으로 채팅방이 생성되었습니다.");
    }

    @Transactional
    public List<ChatRoom> chatLists(UserDetailsImpl userDetails) {
        /*logger.info("chatLists function started");
        List<ChatRoomUser> chatRoomUsers = userDetails.getUser().getChatRoomUsers();

        List<ChatRoom> chatRooms = new ArrayList<>();

        for (ChatRoomUser chatRoomUser : chatRoomUsers) {
            logger.info(String.valueOf(chatRoomUser.getId()));
            chatRooms.add(chatRoomUser.getChatRoom());
        }*/
        // 친구 entity 구현하고 나서 해야할 듯.
        return chatRoomRepository.findAll();
    }
}
