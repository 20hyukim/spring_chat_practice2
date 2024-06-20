package com.sparta.webfluxchat.controller.api;

import com.sparta.webfluxchat.dto.FriendDto;
import com.sparta.webfluxchat.dto.PageFriendRequestDto;
import com.sparta.webfluxchat.dto.PageMyRequestDto;
import com.sparta.webfluxchat.security.UserDetailsImpl;
import com.sparta.webfluxchat.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    @GetMapping("/user/page")
    public String myPage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails){
        model.addAttribute("name", userDetails.getUser().getName());
        model.addAttribute("image", userDetails.getUser().getImageUrl());
        List<FriendDto> friendList = myPageService.getFriendList(userDetails.getUser().getId());
        model.addAttribute("friends", friendList);
        return "page";
    }

    @PostMapping("/user/profile")
    public String setMyProfile(@ModelAttribute PageMyRequestDto pageMyRequestDto,
                               @RequestParam(value="image") MultipartFile image,
                               @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        myPageService.setProfile(image, pageMyRequestDto, userDetails.getUser().getId());
        return "redirect:/user/page";
    }

    @PostMapping("/friend/profile")
    public String setFriendProfile(@ModelAttribute PageFriendRequestDto pageFriendRequestDto,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        myPageService.setFriendProfile(pageFriendRequestDto, userDetails.getUser().getId());
        return "redirect:/user/page";
    }



    @PostMapping("/user/friend")
    public String setFriend(@RequestParam Long friendId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        myPageService.setFriend(friendId, userDetails.getUser());
        return "redirect:/user/page";
    }

}
