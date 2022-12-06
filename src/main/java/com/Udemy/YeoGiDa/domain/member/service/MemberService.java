package com.Udemy.YeoGiDa.domain.member.service;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.AlreadyExistsNicknameException;
import com.Udemy.YeoGiDa.domain.member.exception.MemberDuplicateException;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.member.exception.PasswordMismatchException;
import com.Udemy.YeoGiDa.domain.member.repository.MemberRepository;
import com.Udemy.YeoGiDa.domain.member.request.MemberJoinRequest;
import com.Udemy.YeoGiDa.domain.member.request.MemberLoginRequest;
import com.Udemy.YeoGiDa.domain.member.request.MemberUpdateRequest;
import com.Udemy.YeoGiDa.domain.member.response.MemberDto;
import com.Udemy.YeoGiDa.domain.member.response.MemberJoinResponse;
import com.Udemy.YeoGiDa.domain.member.response.MemberLoginResponse;
import com.Udemy.YeoGiDa.global.jwt.Token;
import com.Udemy.YeoGiDa.global.jwt.service.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public boolean isJoinMember(String email) {
        return memberRepository.existsByEmail(email);
    }

    public MemberLoginResponse login(MemberLoginRequest memberLoginRequest) {
        Member member = memberRepository.findByEmail(memberLoginRequest.getEmail());
        if(member == null) {
            throw new MemberNotFoundException();
        }

        checkPassword(memberLoginRequest.getKakaoId(), member.getPassword());

        Token token = jwtProvider.generateToken(member);

        String refreshToken = token.getRefreshToken();

        member.setRefreshToken(refreshToken);

        return new MemberLoginResponse(token);
    }

    public MemberJoinResponse join(MemberJoinRequest memberJoinRequest) {
        String encodePw = passwordEncoder.encode(memberJoinRequest.getKakaoId());

        Member member = Member.builder()
                .email(memberJoinRequest.getEmail())
                .password(encodePw)
                .nickname(memberJoinRequest.getNickname())
                .imgUrl(memberJoinRequest.getImgUrl())
                .role("ROLE_USER")
                .build();

        isValidateDuplicateMember(member);
        isValidateDuplicateNickname(member);

        Member savedMember = memberRepository.save(member);
        MemberDto memberDto = new MemberDto(savedMember);
        return new MemberJoinResponse(memberDto);
    }

    public void update(Member member, MemberUpdateRequest memberUpdateRequest) {

        if(member == null) {
            throw new MemberNotFoundException();
        }

        if(memberRepository.existsByNickname(memberUpdateRequest.getNickname()) == true &&
            member.getNickname() != memberUpdateRequest.getNickname()) {
            throw new AlreadyExistsNicknameException();
        }

        member.update(memberUpdateRequest.getNickname(), memberUpdateRequest.getImgUrl());
    }

    public void delete(Member member) {

        if(member == null) {
            throw new MemberNotFoundException();
        }

        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public List<Member> memberList() {
        return memberRepository.findAll();
    }

    private void isValidateDuplicateMember(Member member){
        if(memberRepository.existsByEmail(member.getEmail()) == true) {
            throw new MemberDuplicateException();
        }
    }

    public MemberDto memberDetail(Member member) {
        if(member == null) {
            throw new MemberNotFoundException();
        }

        return new MemberDto(member);
    }

    private void isValidateDuplicateNickname(Member member) {
        if(memberRepository.existsByNickname(member.getNickname()) == true) {
            throw new AlreadyExistsNicknameException();
        }
    }

    private void checkPassword(String loginPassword, String password) {
        if( !passwordEncoder.matches(loginPassword, password) ){
            throw new PasswordMismatchException();
        }
    }
}
