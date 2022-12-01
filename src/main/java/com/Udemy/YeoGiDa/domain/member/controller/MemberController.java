package com.Udemy.YeoGiDa.domain.member.controller;

import com.Udemy.YeoGiDa.domain.member.request.MemberLoginRequest;
import com.Udemy.YeoGiDa.domain.member.response.MemberDto;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation("이메일로 회원가입된 유저인지 확인")
    @GetMapping("/checkMember")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity isJoinMember(@RequestParam String email) {
        boolean isMember = memberService.isJoinMember(email);
        Map<String, Object> result = new HashMap<>();
        result.put("isMember", isMember);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                ResponseMessage.READ_SUCCESS, result), HttpStatus.OK);
    }

    @ApiOperation("로그인")
    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity login(@Validated @RequestBody MemberLoginRequest memberLoginRequest) {
        MemberLoginResponse memberLoginResponse = memberService.login(memberLoginRequest);
        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", memberLoginResponse.getToken().getAccessToken());
        result.put("refreshToken", memberLoginResponse.getToken().getRefreshToken());
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

    @ApiOperation("회원상세")
    @GetMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity memberList(@PathVariable Long memberId) {
        MemberDto memberDto = memberService.memberDetail(memberId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", memberDto.getId());
        result.put("email", memberDto.getEmail());
        result.put("nickname", memberDto.getNickname());
        result.put("imgUrl", memberDto.getImgUrl());
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "회원 상세 조회 성공", result), HttpStatus.OK);
    }

    @ApiOperation("회원가입")
    @PostMapping(value = "/join")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity join(@Validated @RequestBody MemberJoinRequest memberJoinRequest) {
        MemberJoinResponse memberJoinResponse = memberService.join(memberJoinRequest);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", memberJoinResponse.getMemberDto().getId());
        result.put("email", memberJoinResponse.getMemberDto().getEmail());
        result.put("nickname", memberJoinResponse.getMemberDto().getNickname());
        result.put("imgUrl", memberJoinResponse.getMemberDto().getImgUrl());
        return new ResponseEntity(DefaultResult.res(StatusCode.CREATED,
                ResponseMessage.CREATED_USER, result), HttpStatus.CREATED);
    }

    @ApiOperation("회원수정")
    @PutMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@PathVariable Long memberId, @RequestBody MemberUpdateRequest memberUpdateRequest) {

        Long updateId = memberService.update(memberId, memberUpdateRequest);
        Map<String, Object> result = new HashMap<>();
        result.put("update memberId", updateId);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                ResponseMessage.UPDATE_USER, result), HttpStatus.OK);
    }

    @ApiOperation("회원탈퇴")
    @DeleteMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@PathVariable Long memberId) {

        Long deleteId =  memberService.delete(memberId);
        Map<String, Object> result = new HashMap<>();
        result.put("delete memberId", deleteId);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                ResponseMessage.DELETE_USER, result), HttpStatus.OK);
    }
}