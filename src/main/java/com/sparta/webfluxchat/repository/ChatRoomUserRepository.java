package com.sparta.webfluxchat.repository;

import com.sparta.webfluxchat.entity.ChatRoomUser;
import com.sparta.webfluxchat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
    List<ChatRoomUser> findByUser(User user);
}
