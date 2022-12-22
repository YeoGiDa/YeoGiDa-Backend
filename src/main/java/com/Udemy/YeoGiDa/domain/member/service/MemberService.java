package com.Udemy.YeoGiDa.domain.member.service;

import com.Udemy.YeoGiDa.domain.common.exception.ImgNotFoundException;
import com.Udemy.YeoGiDa.domain.common.service.S3Service;
import com.Udemy.YeoGiDa.domain.follow.repository.FollowRepository;
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
import com.Udemy.YeoGiDa.domain.member.response.*;
import com.Udemy.YeoGiDa.global.jwt.Token;
import com.Udemy.YeoGiDa.global.jwt.service.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final FollowRepository followRepository;

    @Transactional(readOnly = true)
    public boolean isJoinMember(String email) {
        return memberRepository.existsByEmail(email);
    }

    public MemberLoginResponse login(MemberLoginRequest memberLoginRequest) {
        Member member = memberRepository.findByEmailFetch(memberLoginRequest.getEmail());
        if(member == null) {
            throw new MemberNotFoundException();
        }

        checkPassword(memberLoginRequest.getKakaoId(), member.getPassword());

        Token token = jwtProvider.generateToken(member);

        String refreshToken = token.getRefreshToken();

        member.setRefreshToken(refreshToken);

        return new MemberLoginResponse(member.getId(),token);
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
        if(imgPath == null) {
            imgPath = "https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/default_member.png";
        }
        MemberImg memberImg = new MemberImg(imgPath, savedMember);
        memberImgRepository.save(memberImg);
        savedMember.setMemberImg(memberImg);

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
        //원래 default_image일 때
        if(fileName.equals("default_member.png")) {
            memberImgRepository.delete(findMemberImg);
            if(imgPath == null) {
                throw new ImgNotFoundException();
            }
        }
        else {
            s3Service.deleteFile(fileName);
            memberImgRepository.delete(findMemberImg);
        }
        if(imgPath == null) {
            imgPath = "https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/default_member.png";
        }
        MemberImg memberImg = new MemberImg(imgPath, member);
        memberImgRepository.save(memberImg);
        member.setMemberImg(memberImg);

        member.update(memberUpdateRequest.getNickname());
    }

    public void delete(Member member) {

        if(member == null) {
            throw new MemberNotFoundException();
        }

        MemberImg findMemberImg = memberImgRepository.findMemberImgByMember(member);
        String fileName = findMemberImg.getImgUrl().split("/")[3];
        //이미지가 기본 이미지가 아닐때만
        if(!fileName.equals("default_member.png")) {
            s3Service.deleteFile(fileName);
        }
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

    //베스트 여행자 10명
    public List<BestTravlerListResponse> getBestTravelerBasic() {
        return memberRepository.findAllByMemberOrderByHeartCountBasicFetch()
                .stream().map(BestTravlerListResponse::new)
                .collect(Collectors.toList());
    }

    public List<BestTravlerListResponse> getBestTravelerMore() {
        return memberRepository.findAllByMemberOrderByHeartCountMoreFetch()
                .stream().map(BestTravlerListResponse::new)
                .collect(Collectors.toList());
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

    @Transactional(readOnly = true)
    public MemberDetailResponseDto getMemberDetail(Member member) {

        if(member == null){
            throw new MemberNotFoundException();
        }

        Member memberDetail = Optional.ofNullable(memberRepository.findById(member.getId())
                .orElseThrow(MemberNotFoundException::new)).get();

        MemberDetailResponseDto memberDetailResponseDto = new MemberDetailResponseDto(memberDetail);
        memberDetailResponseDto.setFollowerCount(followRepository.findSizeFollower(member.getId()));
        memberDetailResponseDto.setFollowingCount(followRepository.findSizeFollowing(member.getId()));

        return memberDetailResponseDto;
    }
}
