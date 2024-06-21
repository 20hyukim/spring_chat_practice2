package com.sparta.webfluxchat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {//수동 등록할 Bean을 넣기 위해 만듦. passwordConfig
    @Bean//Bean으로 등록하고자 함. 이게 등록되어 있는 class위에 configuration을 넣어준다.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}