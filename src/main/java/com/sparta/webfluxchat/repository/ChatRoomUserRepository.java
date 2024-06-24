package com.sparta.webfluxchat.repository;

import com.sparta.webfluxchat.entity.ChatRoomUser;
import com.sparta.webfluxchat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
    List<ChatRoomUser> findByUser(User user);
}
