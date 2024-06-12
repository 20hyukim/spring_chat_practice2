package com.sparta.webfluxchat.service;

import com.sparta.webfluxchat.dto.FriendDto;
import com.sparta.webfluxchat.entity.ErrorEnum;
import com.sparta.webfluxchat.entity.Friend;
import com.sparta.webfluxchat.entity.User;
import com.sparta.webfluxchat.repository.FriendRepository;
import com.sparta.webfluxchat.repository.UserRepository;
import com.sparta.webfluxchat.service.S3.S3Uploader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {
    private final S3Uploader s3Uploader;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @Transactional
    public List<FriendDto> getFriendList(Long id) {
        User user = userRepository.findById(id).orElseThrow();

        List<FriendDto> friendDtos = new ArrayList<>();
        for (Friend friend : user.getFriends()) {
            FriendDto friendDto = new FriendDto(friend.getFriendId(), friend.getUser().getId());
            friendDtos.add(friendDto);
            log.info("friendDto {}", friendDto.getFriendId());
        }
        return friendDtos;

    }


    @Transactional
    public void setImage(MultipartFile image, Long id) throws IOException {
        String storedFileName = s3Uploader.upload(image, "image");
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(ErrorEnum.NOT_FOUND_USER.getMessage()));
        user.setImageUrl(storedFileName);
        userRepository.save(user);
    }

    @Transactional
    public void setFriend(Long friendId, User user) {
        user = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException(ErrorEnum.NOT_FOUND_USER.getMessage())); // 사용자 정보 불러오기

        Optional<User> userFriendOptional = userRepository.findById(friendId); // 친구 정보 불러오기
        User userFriend = checkFriendId(friendId, user, userFriendOptional);

        Friend newFriend = new Friend(user, userFriend.getId());
        user.addFriend(newFriend);

        friendRepository.save(newFriend);
        userRepository.save(user);
    }

    private static User checkFriendId(Long friendId, User user, Optional<User> userFriendOptional) {
        User userFriend;
        if (userFriendOptional.isPresent()) {
            userFriend = userFriendOptional.get();
        } else {
            throw new IllegalArgumentException(ErrorEnum.NOT_FOUND_FRIEND.getMessage());
        }

        if (Objects.equals(user.getId(), friendId)) {
            throw new IllegalArgumentException(ErrorEnum.BAD_OWN_REQUEST.getMessage());
        }

        for (Friend friend : user.getFriends()) {
            if (Objects.equals(friend.getFriendId(), friendId)) {
                throw new IllegalArgumentException(ErrorEnum.COMPLETED_REQUEST.getMessage());
            }
        }
        return userFriend;
    }
}
