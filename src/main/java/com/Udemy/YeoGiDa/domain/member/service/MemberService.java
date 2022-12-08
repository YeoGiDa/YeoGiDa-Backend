package com.Udemy.YeoGiDa.domain.member.service;

import com.Udemy.YeoGiDa.domain.common.exception.ImgNotFoundException;
import com.Udemy.YeoGiDa.domain.common.service.S3Service;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.entity.MemberImg;
import com.Udemy.YeoGiDa.domain.member.exception.AlreadyExistsNicknameException;
import com.Udemy.YeoGiDa.domain.member.exception.MemberDuplicateException;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.member.exception.PasswordMismatchException;
import com.Udemy.YeoGiDa.domain.member.repository.MemberImgRepository;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberImgRepository memberImgRepository;
    private final S3Service s3Service;
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

    public MemberJoinResponse join(MemberJoinRequest memberJoinRequest, String imgPath) {
        String encodePw = passwordEncoder.encode(memberJoinRequest.getKakaoId());

        //회원 저장 로직
        Member member = Member.builder()
                .email(memberJoinRequest.getEmail())
                .password(encodePw)
                .nickname(memberJoinRequest.getNickname())
                .role("ROLE_USER")
                .build();

        isValidateDuplicateMember(member);
        isValidateDuplicateNickname(member);

        Member savedMember = memberRepository.save(member);

        //회원 이미지 저장 로직
        MemberImg memberImg = new MemberImg(imgPath, savedMember);
        if(imgPath == null) {
             throw new ImgNotFoundException();
        }
        memberImgRepository.save(memberImg);

        MemberDto memberDto = new MemberDto(savedMember);
        return new MemberJoinResponse(memberDto);
    }

    public void update(Member member, MemberUpdateRequest memberUpdateRequest, String imgPath) {

        if(member == null) {
            throw new MemberNotFoundException();
        }

        if(memberRepository.existsByNickname(memberUpdateRequest.getNickname()) == true &&
            member.getNickname() != memberUpdateRequest.getNickname()) {
            throw new AlreadyExistsNicknameException();
        }

        //회원 이미지 로직
        MemberImg findMemberImg = memberImgRepository.findMemberImgByMember(member);
        String fileName = findMemberImg.getImgUrl().split("/")[3];
        if(fileName != "default_member.png") {
            s3Service.deleteFile(fileName);
        }
        memberImgRepository.delete(findMemberImg);
        MemberImg memberImg = new MemberImg(imgPath, member);

        if(imgPath == null) {
            throw new ImgNotFoundException();
        }
        memberImgRepository.save(memberImg);

        member.update(memberUpdateRequest.getNickname());
    }

    public void delete(Member member) {

        if(member == null) {
            throw new MemberNotFoundException();
        }

        MemberImg findMemberImg = memberImgRepository.findMemberImgByMember(member);
        String fileName = findMemberImg.getImgUrl().split("/")[3];
        s3Service.deleteFile(fileName);
        memberImgRepository.delete(findMemberImg);

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
