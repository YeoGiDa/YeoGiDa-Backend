package com.Udemy.YeoGiDa.domain.member.controller;

import com.Udemy.YeoGiDa.domain.common.exception.WrongImgFormatException;
import com.Udemy.YeoGiDa.domain.common.service.S3Service;
import com.Udemy.YeoGiDa.domain.member.request.MemberLoginRequest;
import com.Udemy.YeoGiDa.domain.member.response.BestTravlerListResponse;
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
import com.Udemy.YeoGiDa.global.security.annotation.LoginMember;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final S3Service s3Service;

    @ApiOperation("이메일로 회원가입된 유저인지 확인 (ADMIN용)")
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
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("memberId", memberLoginResponse.getMemberId());
        result.put("accessToken", memberLoginResponse.getToken().getAccessToken());
        result.put("refreshToken", memberLoginResponse.getToken().getRefreshToken());
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                ResponseMessage.LOGIN_SUCCESS, result), HttpStatus.OK);
    }

    @ApiOperation("회원목록 (ADMIN용)")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity memberList() {
        List<Member> members =  memberService.memberList();
        Map<String, Object> result = new HashMap<>();
        result.put("memberList", members);

        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "회원 목록 조회 성공", result), HttpStatus.OK);
    }

    @ApiOperation("베스트 여행자")
    @GetMapping("/best-traveler/basic")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getBestTraveler() {
        List<BestTravlerListResponse> members = memberService.getBestTravelerBasic();
        Map<String, Object> result = new HashMap<>();
        result.put("memberList", members);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "목록 조회 성공 - 베스트 여행자", result), HttpStatus.OK);
    }

    @ApiOperation("회원상세 (ADMIN용)")
    @GetMapping("/detail")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity memberList(@LoginMember Member member) {
        MemberDto memberDto = memberService.memberDetail(member);
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
    public ResponseEntity join(@Validated @ModelAttribute MemberJoinRequest memberJoinRequest,
                               @RequestPart(name = "imgUrl", required = false) MultipartFile multipartFile) throws WrongImgFormatException {
        String imgPath = "";
        if(multipartFile.isEmpty()) {
            imgPath = null;
        }
        else {
            imgPath = s3Service.upload(multipartFile);
        }
        MemberJoinResponse memberJoinResponse = memberService.join(memberJoinRequest, imgPath);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", memberJoinResponse.getMemberDto().getId());
        result.put("email", memberJoinResponse.getMemberDto().getEmail());
        result.put("nickname", memberJoinResponse.getMemberDto().getNickname());
        result.put("imgUrl", memberJoinResponse.getMemberDto().getImgUrl());
        return new ResponseEntity(DefaultResult.res(StatusCode.CREATED,
                ResponseMessage.CREATED_USER, result), HttpStatus.CREATED);
    }

    @ApiOperation("회원수정")
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@ModelAttribute MemberUpdateRequest memberUpdateRequest,
                                 @RequestPart(name = "imgUrl", required = false) MultipartFile multipartFile,
                                 @LoginMember Member member) throws WrongImgFormatException {
        String imgPath = "";
        if(multipartFile.isEmpty()) {
            imgPath = null;
        }
        else {
            imgPath = s3Service.upload(multipartFile);
        }
        memberService.update(member, memberUpdateRequest, imgPath);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                ResponseMessage.UPDATE_USER), HttpStatus.OK);
    }

    @ApiOperation("회원탈퇴")
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@LoginMember Member member) {

        memberService.delete(member);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                ResponseMessage.DELETE_USER), HttpStatus.OK);
    }
}