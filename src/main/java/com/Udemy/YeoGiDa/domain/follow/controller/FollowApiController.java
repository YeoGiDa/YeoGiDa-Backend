package com.Udemy.YeoGiDa.domain.follow.controller;

import com.Udemy.YeoGiDa.domain.follow.response.ProfileDto;
import com.Udemy.YeoGiDa.domain.follow.response.UserSimpleInfoDto;
import com.Udemy.YeoGiDa.domain.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FollowApiController {

    private final FollowService followService;


    @PostMapping("/follow/{toMemberNickname}/{fromMemberNickname}")
    public ResponseEntity<?> addFollow(@PathVariable String toUsername, @PathVariable String fromUsername){
        Boolean result = followService.addFollow(toUsername, fromUsername);
        if(result) {

            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/unfollow/{toMemberNickname}/{fromMemberNickname}")
    public ResponseEntity<?> unFollow(@PathVariable String toUsername, @PathVariable String fromUsername){
        Boolean result = followService.unFollow(toUsername, fromUsername);
        if(result) {

            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/follower/{profileUsername}/{requestingUsername}")
    public ResponseEntity<?> getFollower(@PathVariable String profileUsername, @PathVariable String requestingUsername){
        List<UserSimpleInfoDto> followerList = followService.getFollowerList(profileUsername, requestingUsername);
        return new ResponseEntity<>(followerList, HttpStatus.OK);
    }

    @GetMapping("/following/{profileUsername}/{requestingUsername}")
    public ResponseEntity<?> getFollowing(@PathVariable String profileUsername, @PathVariable String requestingUsername){
        List<UserSimpleInfoDto> followingList = followService.getFollowingList(profileUsername, requestingUsername);
        return new ResponseEntity<>(followingList, HttpStatus.OK);
    }
}