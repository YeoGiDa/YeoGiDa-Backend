//package com.Udemy.YeoGiDa.domain.follow.controller;
//
//import com.Udemy.YeoGiDa.domain.follow.response.FollowResponseDto;
//import com.Udemy.YeoGiDa.domain.follow.service.FollowService;
//import com.Udemy.YeoGiDa.global.response.DefaultResult;
//import com.Udemy.YeoGiDa.global.response.StatusCode;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1")
//
//public class FollowController {
//
//    private final FollowService followService;
//
//    @PostMapping("/follow/{toMemberNickname}/{fromMemberNickname}")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity followPlus(@PathVariable String toMemberNickname,
//                                     @PathVariable String fromMemberNickname){
//        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
//                "팔로잉 추가 성공"), HttpStatus.OK);
//    }
//
//    @PostMapping("/follow/{toMemberNickname}/{fromMemberNickname}")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity unfollowPlus(@PathVariable String toMemberNickname,
//                                     @PathVariable String fromMemberNickname){
//        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
//                "팔로잉 삭제 성공"), HttpStatus.OK);
//    }
//
//    @GetMapping("/follower/{memberNickname}/{followerMembers}")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity getFollowerList(@PathVariable String memberNickname,
//                                          @PathVariable String followerMembers){
//        List<FollowResponseDto> result = followService.getFollowerList(memberNickname, followerMembers);
//
//        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
//                "팔로워 목록 조회 성공", result), HttpStatus.OK);
//    }
//
//    @GetMapping("/{memberId}/following")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity getFollowingList(@PathVariable Long memberId){
//        List<FollowResponseDto> result = followService.getFollowingList(memberId);
//
//        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
//                "팔로잉 목록 조회 성공", result), HttpStatus.OK);
//    }
//
//
//
//}
