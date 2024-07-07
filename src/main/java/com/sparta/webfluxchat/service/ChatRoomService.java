package com.sparta.webfluxchat.service;

import com.sparta.webfluxchat.dto.FriendDto;
import com.sparta.webfluxchat.dto.FriendIdsDto;
import com.sparta.webfluxchat.entity.*;
import com.sparta.webfluxchat.repository.ChatRoomRepository;
import com.sparta.webfluxchat.repository.ChatRoomUserRepository;
import com.sparta.webfluxchat.repository.FriendRepository;
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
    private final FriendRepository friendRepository;
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


    private User findUserByIdAndCheckPresent(Long id) {
        Optional<User> UserOptional= userRepository.findById(id);
        if (UserOptional.isPresent()) {
            return UserOptional.get();
        }
        throw new IllegalArgumentException(ErrorEnum.NOT_FOUND_USER.getMessage());
    }

    public List<FriendDto> findAttendants(Long roomnumber, Long userId) {
        List<ChatRoomUser> ChatRoomUsers = chatRoomUserRepository.findByChatRoomId(roomnumber);

        List<FriendDto> friendDtos = new ArrayList<>();
        for (ChatRoomUser chatRoomUser : ChatRoomUsers) {
            Long friendId = chatRoomUser.getUser().getId();
            Friend friend = friendRepository.findByUserIdAndFriendId(userId, friendId);
            User friendUser = findUserByIdAndCheckPresent(friendId);

            FriendDto friendDto = new FriendDto(friendId, friend.getFriendName(), friendUser.getName(), friendUser.getImageUrl());
            friendDtos.add(friendDto);
        }

        return friendDtos;
        // 유효성 검사하고 friendDto 에 담아서 보내줘야 함. 자기 자신을 빼고 보내주는 것이 낫나???? 모르겠네
    }
}
