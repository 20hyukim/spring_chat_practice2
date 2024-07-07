package com.sparta.webfluxchat.controller.api;

import com.sparta.webfluxchat.dto.FriendDto;
import com.sparta.webfluxchat.entity.ChatRoom;
import com.sparta.webfluxchat.security.UserDetailsImpl;
import com.sparta.webfluxchat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/")
    public String chatRoomPage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails){
        List<ChatRoom> chatRooms = chatRoomService.chatLists(userDetails);
        model.addAttribute("chatRooms", chatRooms);
        model.addAttribute("name", userDetails.getUser().getUsername());
        return "chatRoom";
    }

    @GetMapping("/chatroom/{roomnumber}")
    public String chatPage(Model model, @PathVariable Long roomnumber, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("roomNumber", roomnumber);
        model.addAttribute("owner", userDetails.getUser().getId());
        //List<FriendDto> friendDtos = chatRoomService.findAttendants(roomnumber, userDetails.getUser().getId());
        //model.addAttribute("attendants", friendDtos);
        return "index";
    }

}
