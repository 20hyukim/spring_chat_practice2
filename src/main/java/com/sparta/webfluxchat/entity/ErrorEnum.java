package com.sparta.webfluxchat.entity;

import lombok.Getter;

@Getter
public enum ErrorEnum {
    NOT_FOUND_USER("사용자를 찾을 수 없습니다."),
    NOT_FOUND_FRIEND("유효하지 않은 친구 번호 입니다."),
    BAD_OWN_REQUEST("자기 자신을 친구 추가 할 수 없습니다."),
    COMPLETED_REQUEST("이미 친구 추가 완료된 아이디 입니다.");

    private final String message;

    ErrorEnum(String message) {
        this.message = message;
    }
}
