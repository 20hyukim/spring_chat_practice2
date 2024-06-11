package com.sparta.webfluxchat.controller.api;

import com.sparta.webfluxchat.dto.FriendDto;
import com.sparta.webfluxchat.entity.User;
import com.sparta.webfluxchat.security.UserDetailsImpl;
import com.sparta.webfluxchat.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    @GetMapping("/user/page")
    public String myPage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails){
        model.addAttribute("username", userDetails.getUser().getUsername());
        model.addAttribute("image", userDetails.getUser().getImageUrl());
        List<FriendDto> friendList = myPageService.getFriendList(userDetails.getUser().getId());
        model.addAttribute("friends", friendList);
        return "page";
    }

    @PatchMapping("/user/image")
    public String myImage(@RequestParam(value="image") MultipartFile image, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        myPageService.setImage(image, userDetails.getUser().getId());
        return "redirect:/page";
    }

    @PostMapping("/user/friend")
    public String myFriend(@RequestParam Long friendId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        myPageService.setFriend(friendId, userDetails.getUser());
        return "redirect:/page";
    }

}
