package com.sparta.webfluxchat.service;

import com.sparta.webfluxchat.dto.FriendDto;
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
        log.info("getFriendList method started");
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
        User user = userRepository.findById(id).orElseThrow();
        user.setImageUrl(storedFileName);
        userRepository.save(user);
    }

    @Transactional
    public void setFriend(Long friendId, User user) {
        user = userRepository.findById(user.getId()).orElseThrow();

        Optional<User> userFriendOptional = userRepository.findById(friendId);

        User userFriend;
        if (userFriendOptional.isPresent()) {
            userFriend = userFriendOptional.get();
        } else {
            throw new IllegalArgumentException("유효하지 않은 친구 번호 입니다.");
        }

        Friend newFriend = new Friend(user, userFriend.getId());

        if(user.getFriends() == null){ // 이 코드의 필요성 다시 확인해보기.
            user.setFriends(new ArrayList<>());
        }

        user.addFriend(newFriend);

        friendRepository.save(newFriend);
        userRepository.save(user);

        for (Friend friend : user.getFriends()) {
            log.info("setfriend {}", friend.getFriendId());
        }
    }
}
