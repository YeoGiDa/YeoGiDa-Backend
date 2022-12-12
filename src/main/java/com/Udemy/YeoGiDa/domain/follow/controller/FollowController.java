package com.Udemy.YeoGiDa.domain.follow.controller;
import com.Udemy.YeoGiDa.domain.follow.response.UserSimpleInfoDto;
import com.Udemy.YeoGiDa.domain.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")

public class FollowController {

    private final FollowService followService;


    @PostMapping("/follow/{toMemberNickname}/{fromMemberNickname}")
    public String addFollow(@PathVariable String toMemberNickname, @PathVariable String fromMemberNickname){
        Boolean result = followService.addFollow(toMemberNickname, fromMemberNickname);
        return "ok";
    }

    @PostMapping("/unfollow/{toUsername}/{fromUsername}")
    public String unFollow(@PathVariable String toUsername, @PathVariable String fromUsername){
        Boolean result = followService.unFollow(toUsername, fromUsername);
        return "redirect:/profile/{toUsername}/{fromUsername}";
    }

    @GetMapping("/follower/{username}/{requestingUsername}")
    public String getFollower(@PathVariable String username, @PathVariable String requestingUsername, Model model){
        List<UserSimpleInfoDto> followerList = followService.getFollowerList(username, requestingUsername);
        model.addAttribute("users", followerList);
        return "/account/userList";
    }

    @GetMapping("/following/{username}/{requestingUsername}")
    public String getFollowing(@PathVariable String username, @PathVariable String requestingUsername, Model model){
        List<UserSimpleInfoDto> followingList = followService.getFollowingList(username, requestingUsername);
        model.addAttribute("users", followingList);
        return "/account/userList";
    }



}
