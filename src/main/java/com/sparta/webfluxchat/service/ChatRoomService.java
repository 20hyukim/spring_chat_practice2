package com.sparta.webfluxchat.service;

import com.sparta.webfluxchat.dto.FriendIdsDto;
import com.sparta.webfluxchat.entity.ChatRoom;
import com.sparta.webfluxchat.entity.ChatRoomUser;
import com.sparta.webfluxchat.entity.ErrorEnum;
import com.sparta.webfluxchat.entity.User;
import com.sparta.webfluxchat.repository.ChatRoomRepository;
import com.sparta.webfluxchat.repository.ChatRoomUserRepository;
import com.sparta.webfluxchat.repository.UserRepository;
import com.sparta.webfluxchat.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private static final Logger logger = LoggerFactory.getLogger(ChatRoomService.class);

    @Transactional
    public ResponseEntity<String> createChatRoom(String chatName, Long userId, FriendIdsDto friendIdsDto) {

        if (friendIdsDto.getIds().isEmpty()) {
            return ResponseEntity.ok("친구가 선택되지 않았습니다.");
        }

        ChatRoom chatRoom = new ChatRoom(chatName);
        chatRoomRepository.save(chatRoom);

        friendIdsDto.getIds().add(userId);
        for (Long id : friendIdsDto.getIds()) {
            User user = findUserByIdAndCheckPresent(id);
            ChatRoomUser chatRoomUser = new ChatRoomUser(user, chatRoom);
            chatRoomUserRepository.save(chatRoomUser);
        }

        return ResponseEntity.ok("성공적으로 채팅방이 생성되었습니다.");
    }

    @Transactional
    public List<ChatRoom> chatLists(UserDetailsImpl userDetails) {
        logger.info("chatLists function started");
        logger.info(String.valueOf(userDetails.getUser().getId()));
        List<ChatRoomUser> chatRoomUsers = chatRoomUserRepository.findByUser(userDetails.getUser());

        List<ChatRoom> chatRooms = new ArrayList<>();

        for (ChatRoomUser chatRoomUser : chatRoomUsers) {
            logger.info(String.valueOf(chatRoomUser.getId()));
            chatRooms.add(chatRoomUser.getChatRoom());
        }

        return chatRooms;
    }

    public void createChat() {
        Optional<User> userOptional1 = userRepository.findById(1L);
        Optional<User> userOptional2 = userRepository.findById(2L);
        Optional<User> userOptional3 = userRepository.findById(3L);

        User user1 = userOptional1.get();
        User user2 = userOptional2.get();
        User user3 = userOptional3.get();


        ChatRoom chatRoom = new ChatRoom("new");
        chatRoomRepository.save(chatRoom);

        ChatRoomUser chatRoomUser = new ChatRoomUser();
        chatRoomUser.setChatRoom(chatRoom);
        chatRoomUser.setUser(user1);
        chatRoomUserRepository.save(chatRoomUser);

        ChatRoomUser chatRoomUser1 = new ChatRoomUser();
        chatRoomUser1.setChatRoom(chatRoom);
        chatRoomUser1.setUser(user2);
        chatRoomUserRepository.save(chatRoomUser1);


        ChatRoomUser chatRoomUser2 = new ChatRoomUser();
        chatRoomUser2.setChatRoom(chatRoom);
        chatRoomUser2.setUser(user3);
        chatRoomUserRepository.save(chatRoomUser2);

    }

    private User findUserByIdAndCheckPresent(Long id) {
        Optional<User> UserOptional= userRepository.findById(id);
        if (UserOptional.isPresent()) {
            return UserOptional.get();
        }
        throw new IllegalArgumentException(ErrorEnum.NOT_FOUND_USER.getMessage());
    }
}
