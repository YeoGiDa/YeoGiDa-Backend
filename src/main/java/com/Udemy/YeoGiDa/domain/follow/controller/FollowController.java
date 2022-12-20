package com.Udemy.YeoGiDa.domain.follow.controller;

import com.Udemy.YeoGiDa.domain.follow.service.FollowService;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.response.MemberDto;
import com.Udemy.YeoGiDa.global.response.DefaultResult;
import com.Udemy.YeoGiDa.global.response.StatusCode;
import com.Udemy.YeoGiDa.global.security.annotation.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follow")
public class FollowController {

    private final FollowService followService;

    @GetMapping("/following")
    public ResponseEntity getFollowingListOrderById(@LoginMember Member member){
        List<MemberDto> followingList = followService.getFollowingList(member);
        Map<String, Object> result = new HashMap<>();
        result.put("followingList",followingList);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로잉 목록 조회 성공", followingList), HttpStatus.OK);
    }

    @GetMapping("/follower")
    public ResponseEntity getFollowerListOrderById(@LoginMember Member member){
        List<MemberDto> followerList = followService.getFollowerList(member);
        Map<String, Object> result = new HashMap<>();
        result.put("followerList",followerList);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로워 목록 조회 성공", result), HttpStatus.OK);
    }

    @GetMapping("/search/following")
    public ResponseEntity getFollowingByNickname(@LoginMember Member member,
                                                 @RequestParam("nickname") String nickname){
        List<MemberDto> followerList = followService.getFollowingListSearch(member.getId(),nickname);
        Map<String, Object> result = new HashMap<>();
        result.put("followerList",followerList);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로잉 검색 목록 조회 성공", followerList), HttpStatus.OK);
    }

    @GetMapping("/search/follower")
    public ResponseEntity getFollowerByNickname(@LoginMember Member member,
                                                 @RequestParam("nickname") String nickname){
        List<MemberDto> followerList = followService.getFollowerListSearch(member.getId(),nickname);
        Map<String, Object> result = new HashMap<>();
        result.put("followerList",followerList);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로워 검색 목록 조회 성공", followerList), HttpStatus.OK);
    }


    @PostMapping("/{toMemberId}")
    public ResponseEntity addFollowing(@PathVariable Long toMemberId,
                                       @LoginMember Member member){

        boolean result = followService.addFollow(toMemberId, member.getId());

        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로우 성공", result), HttpStatus.OK);
    }

    @DeleteMapping("/{toMemberId}")
    public ResponseEntity deleteFollowing(@PathVariable Long toMemberId,
                                       @LoginMember Member member){

        boolean result = followService.unFollow(toMemberId, member.getId());

        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로우 취소 성공", result), HttpStatus.OK);
    }
}
