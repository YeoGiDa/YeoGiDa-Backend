package com.Udemy.YeoGiDa.domain.member.controller;

import com.Udemy.YeoGiDa.domain.member.request.MemberLoginRequest;
import com.Udemy.YeoGiDa.domain.member.response.MemberLoginResponse;
import com.Udemy.YeoGiDa.global.response.DefaultResult;
import com.Udemy.YeoGiDa.global.response.ResponseMessage;
import com.Udemy.YeoGiDa.global.response.StatusCode;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.request.MemberJoinRequest;
import com.Udemy.YeoGiDa.domain.member.request.MemberUpdateRequest;
import com.Udemy.YeoGiDa.domain.member.response.MemberJoinResponse;
import com.Udemy.YeoGiDa.domain.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation("이메일로 회원가입된 유저인지 확인")
    @GetMapping("/checkMember")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity isJoinMember(@RequestParam String email) {
        boolean result = memberService.isJoinMember(email);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                ResponseMessage.READ_SUCCESS, result), HttpStatus.OK);
    }

    @ApiOperation("로그인")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity login(@Validated @RequestBody MemberLoginRequest memberLoginRequest) {
        MemberLoginResponse result = memberService.login(memberLoginRequest);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                ResponseMessage.LOGIN_SUCCESS, result), HttpStatus.OK);
    }

    @ApiOperation("회원목록")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity memberList() {
        List<Member> result =  memberService.memberList();
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "회원 목록 조회 성공", result), HttpStatus.OK);
    }

    @ApiOperation("회원가입")
    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity join(@Validated @RequestBody MemberJoinRequest memberJoinRequest) {
        MemberJoinResponse result = memberService.join(memberJoinRequest);
        return new ResponseEntity(DefaultResult.res(StatusCode.CREATED,
                ResponseMessage.CREATED_USER, result), HttpStatus.CREATED);
    }

    @ApiOperation("회원수정")
    @PutMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@PathVariable Long memberId, @RequestBody MemberUpdateRequest memberUpdateRequest) {

        Long result = memberService.update(memberId, memberUpdateRequest);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                ResponseMessage.UPDATE_USER, result), HttpStatus.OK);
    }

    @ApiOperation("회원탈퇴")
    @DeleteMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@PathVariable Long memberId) {

        Long result =  memberService.delete(memberId);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                ResponseMessage.DELETE_USER, result), HttpStatus.OK);
    }
}