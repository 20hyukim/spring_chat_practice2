package com.sparta.webfluxchat.repository;

import com.sparta.webfluxchat.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    Friend findByUserIdAndFriendId(Long userId, Long friendId);
}
