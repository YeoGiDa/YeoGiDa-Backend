package com.Udemy.YeoGiDa.domain.follow.controller;

import com.Udemy.YeoGiDa.domain.follow.service.FollowService;
import com.Udemy.YeoGiDa.domain.place.response.PlaceListResponseDto;
import com.Udemy.YeoGiDa.global.response.DefaultResult;
import com.Udemy.YeoGiDa.global.response.StatusCode;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follow")
public class FollowController {

    private final FollowService followService;


    @GetMapping("/{memberId}/following")
    public ResponseEntity getFollowingListOrderById(@PathVariable Long memberId){
        List<Tuple> followingList = followService.getFollowingList(memberId);

        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로잉 목록 조회 성공", followingList), HttpStatus.OK);
    }



    @GetMapping("/{memberId}/follower")
    public ResponseEntity getFollowerListOrderById(@PathVariable Long memberId){
        List<Tuple> followerList = followService.getFollowerList(memberId);

        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로워 목록 조회 성공", followerList), HttpStatus.OK);
    }


    @PostMapping("/{toMemberId}/follows/{fromMemberId}")
    public ResponseEntity addFollowing(@PathVariable Long toMemberId,
                                       @PathVariable Long fromMemberId){

        boolean result = followService.addFollow(toMemberId, fromMemberId);

        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로우 성공", result), HttpStatus.OK);

    }

    @DeleteMapping("/{toMemberId}/follows/{fromMemberId}")
    public ResponseEntity deleteFollowing(@PathVariable Long toMemberId,
                                       @PathVariable Long fromMemberId){

        boolean result = followService.unFollow(toMemberId, fromMemberId);

        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로우 취소 성공", result), HttpStatus.OK);

    }

}
