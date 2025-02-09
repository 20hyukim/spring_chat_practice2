package com.sparta.webfluxchat.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users") // users라고 설정했음.
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING) //enum type을 db에 저장하고자 할 떄 // Enum type이 만약 USER라고 할 때, USER 그대로 db에 저장되는
    private UserRoleEnum role;

    @Column
    private String imageUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // LazyInitializationException 의 이유로 EAGER 처리, 다른 해결 방법은 없을까
    private List<ChatRoomUser> chatRoomUsers;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> friends;


    public User(String username, String password, String email, String name, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public void addFriend(Friend friend) {
        this.friends.add(friend);
        friend.setUser(this);
    }
}