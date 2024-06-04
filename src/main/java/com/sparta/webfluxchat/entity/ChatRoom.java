package com.sparta.webfluxchat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomUser> chatRoomUsers;

    public ChatRoom(String chatName) {
        this.name = chatName;
    }

}
