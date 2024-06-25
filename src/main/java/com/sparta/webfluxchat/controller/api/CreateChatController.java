package com.sparta.webfluxchat.controller.api;

import com.sparta.webfluxchat.dto.FriendDto;
import com.sparta.webfluxchat.security.UserDetailsImpl;
import com.sparta.webfluxchat.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CreateChatController {

    private final MyPageService myPageService;

    @GetMapping("/user/chat")
    public String myPage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails){
        model.addAttribute("name", userDetails.getUser().getName());
        model.addAttribute("image", userDetails.getUser().getImageUrl());
        List<FriendDto> friendList = myPageService.getFriendList(userDetails.getUser().getId());
        model.addAttribute("friends", friendList);
        return "createChat";
    }
}
