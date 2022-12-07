package com.Udemy.YeoGiDa.domain.member.controller;

import com.Udemy.YeoGiDa.domain.common.service.S3Service;
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
import com.Udemy.YeoGiDa.global.security.annotation.LoginMember;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final S3Service s3Service;

    @ApiOperation("이메일로 회원가입된 유저인지 확인 (ADMIN용)")
    @ApiResponse(code = 200, message = "조회 완료")
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
    @ApiResponses({
            @ApiResponse(code = 200, message = "로그인 성공"),
            @ApiResponse(code = 403, message = "비밀번호(kakoId) 불일치"),
            @ApiResponse(code = 404, message = "가입되지 않은 멤버")
    })
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

    @ApiOperation("회원목록 (ADMIN용)")
    @ApiResponse(code = 200, message = "조회 완료")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity memberList() {
        List<Member> result =  memberService.memberList();
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "회원 목록 조회 성공", result), HttpStatus.OK);
    }

    @ApiOperation("회원상세 (ADMIN용)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 상세 조회 완료"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재하지 않는 회원")
    })
    @GetMapping("/detail")
    @ResponseStatus(HttpStatus.OK)
//    @ApiImplicitParam(name = "Authorization", value = "사용자 인증을 위한 accessToken", paramType = "header", required = true, dataTypeClass = String.class)
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
    @ApiResponses({
            @ApiResponse(code = 201, message = "가입 완료"),
            @ApiResponse(code = 400, message = "가입 실패" +
                    "닉네임 중복 - AlreadyExistEmail," +
                    "회원 중복 - MemberDuplicated")
    })
    @PostMapping(value = "/join")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity join(@Validated @ModelAttribute MemberJoinRequest memberJoinRequest,
                               @RequestPart(name = "imgUrl") MultipartFile multipartFile) {
        //TODO: defaultImg 설정
//        if(multipartFile == null) {
//
//        }

        String imgPath = s3Service.upload(multipartFile);
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
    @ApiResponses({
            @ApiResponse(code = 200, message = "수정 성공"),
            @ApiResponse(code = 400, message = "이미 존재하는 닉네임"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재하지않는 회원")
    })
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
//    @ApiImplicitParam(name = "Authorization", value = "사용자 인증을 위한 accessToken", paramType = "header", required = true, dataTypeClass = String.class)
    public ResponseEntity update(@ModelAttribute MemberUpdateRequest memberUpdateRequest,
                                 @RequestPart(name = "imgUrl") MultipartFile multipartFile,
                                 @LoginMember Member member) {
        s3Service.deleteFile(member.getImg().getImgUrl());

        //TODO: defaultImg 설정
//        if(multipartFile == null) {
//
//        }
        String imgPath = s3Service.upload(multipartFile);
        memberService.update(member, memberUpdateRequest, imgPath);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                ResponseMessage.UPDATE_USER), HttpStatus.OK);
    }

    @ApiOperation("회원탈퇴")
    @ApiResponses({
            @ApiResponse(code = 200, message = "탈퇴 성공"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재하지않는 회원")
    })
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
//    @ApiImplicitParam(name = "Authorization", value = "사용자 인증을 위한 accessToken", paramType = "header", required = true, dataTypeClass = String.class)
    public ResponseEntity delete(@LoginMember Member member) {

        s3Service.deleteFile(member.getImg().getImgUrl());
        memberService.delete(member);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                ResponseMessage.DELETE_USER), HttpStatus.OK);
    }
}