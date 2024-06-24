package com.sparta.webfluxchat.repository;



import com.sparta.webfluxchat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // 이걸 쿼리 메서드 라고 하는구나. DB랑 왔다갔다하니까

    Optional<User> findByEmail(String email);
}
