package com.sparta.webfluxchat.service;

import com.amazonaws.Response;
import com.sparta.webfluxchat.entity.User;
import com.sparta.webfluxchat.repository.UserRepository;
import com.sparta.webfluxchat.service.S3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class MyPageService {
    @Autowired
    private S3Uploader s3Uploader;
    @Autowired
    private UserRepository userRepository;
    public void setImage(MultipartFile image, Long id) throws IOException {
        String storedFileName = s3Uploader.upload(image, "image");
        User user = userRepository.findById(id).orElseThrow();
        user.setImageUrl(storedFileName);
        userRepository.save(user);
    }
}
