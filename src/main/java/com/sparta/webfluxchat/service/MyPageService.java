package com.sparta.webfluxchat.service;

import com.sparta.webfluxchat.dto.FriendDto;
import com.sparta.webfluxchat.dto.PageMyRequestDto;
import com.sparta.webfluxchat.entity.ErrorEnum;
import com.sparta.webfluxchat.entity.Friend;
import com.sparta.webfluxchat.entity.User;
import com.sparta.webfluxchat.repository.FriendRepository;
import com.sparta.webfluxchat.repository.UserRepository;
import com.sparta.webfluxchat.service.S3.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(readOnly = true)
    public List<FriendDto> getFriendList(Long id) {
        User user = findUserByIdAndCheckPresent(id, false);

        List<FriendDto> friendDtos = new ArrayList<>();
        for (Friend friend : user.getFriends()) {
            User friendUser = findUserByIdAndCheckPresent(friend.getFriendId(), true);
            String friendName = getSettedName(friend, friendUser);
            FriendDto friendDto = new FriendDto(friendUser.getId(), friendName, friendUser.getImageUrl());
            friendDtos.add(friendDto);
        }
        return friendDtos;
    }

    private String getSettedName(Friend friend, User friendUser) {
        if (friend.getFriendName()==null) {
            return friendUser.getName();
        }
        return friend.getFriendName();
    }

    @Transactional
    public void setProfile(MultipartFile image, PageMyRequestDto pageMyRequestDto, Long id) throws IOException {
        User user = findUserByIdAndCheckPresent(id, false);
        if (!image.isEmpty()) {
            String storedFileName = s3Uploader.upload(image, "image");
            user.setImageUrl(storedFileName);
        }
        if (!pageMyRequestDto.getName().isEmpty()) {
            user.setName(pageMyRequestDto.getName());
        }
        userRepository.save(user);
    }

    @Transactional
    public void setFriend(Long friendId, User user) {
        user = findUserByIdAndCheckPresent(user.getId(), false);
        User userFriend = findUserByIdAndCheckPresent(friendId, true);

        checkFriendId(friendId, user);

        Friend newFriend = new Friend(user, userFriend.getId());
        user.addFriend(newFriend);

        friendRepository.save(newFriend);
        userRepository.save(user);
    }


    private User findUserByIdAndCheckPresent(Long id, boolean friend) {
        Optional<User> UserOptional= userRepository.findById(id);
        if (UserOptional.isPresent()) {
            return UserOptional.get();
        }
        if (friend) {
            throw new IllegalArgumentException(ErrorEnum.NOT_FOUND_FRIEND.getMessage());
        }
        throw new IllegalArgumentException(ErrorEnum.NOT_FOUND_USER.getMessage());
    }


    private static void checkFriendId(Long friendId, User user) {
        if (Objects.equals(user.getId(), friendId)) {
            throw new IllegalArgumentException(ErrorEnum.BAD_OWN_REQUEST.getMessage());
        }

        for (Friend friend : user.getFriends()) {
            if (Objects.equals(friend.getFriendId(), friendId)) {
                throw new IllegalArgumentException(ErrorEnum.COMPLETED_REQUEST.getMessage());
            }
        }
    }
}
