package com.Udemy.YeoGiDa.domain.follow.service;

import com.Udemy.YeoGiDa.domain.follow.entity.Follow;
import com.Udemy.YeoGiDa.domain.follow.entity.FollowStatus;
import com.Udemy.YeoGiDa.domain.follow.exception.FollowException;
import com.Udemy.YeoGiDa.domain.follow.repository.FollowRepository;
import com.Udemy.YeoGiDa.domain.follow.response.FollowResponseDto;
import com.Udemy.YeoGiDa.domain.follow.response.UserSimpleInfoDto;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.member.repository.MemberRepository;
import com.mysema.commons.lang.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public Boolean addFollow(String toMemberNickName, String fromMemberNickName){
        checkSameMember(toMemberNickName, fromMemberNickName);

        Member toMember = memberRepository.findByNickname(toMemberNickName);
        Member fromMember = memberRepository.findByNickname(fromMemberNickName);


        Optional<Follow> relation = getFollowRelation(toMember.getId(), fromMember.getId());
        if(relation.isPresent())
            throw new FollowException();
        followRepository.save(new Follow(toMember.getId(),  fromMember.getId()));
        return true;
    }

    public Boolean unFollow(String toMemberNickName, String fromMemberNickName) {
        checkSameMember(toMemberNickName, fromMemberNickName);

        Member toMember = memberRepository.findByNickname(toMemberNickName);
        Member fromMember = memberRepository.findByNickname(fromMemberNickName);

        Optional<Follow> relation = getFollowRelation(toMember.getId(), fromMember.getId());
        if(relation.isEmpty())
            throw new FollowException();
        followRepository.delete(relation.get());
        return true;
    }

    private Optional<Follow> getFollowRelation(Long toMemberId, Long fromMemberId) {
        return followRepository.findByToMemberAndFromMember(toMemberId,fromMemberId);
    }

    private void checkSameMember(String toMemberNickName, String fromMemberNickName) {

        if(toMemberNickName.equals(fromMemberNickName)) throw new FollowException();
    }


    public List<UserSimpleInfoDto> getFollowerList(String memberNickname, String requestingMemberNickname){
        Member member = memberRepository.findByNickname(memberNickname);
        Member requestingMember;
        if(requestingMemberNickname.equals(memberNickname)) requestingMember = member;
        else requestingMember = memberRepository.findByNickname(requestingMemberNickname);

        List<UserSimpleInfoDto> members = getAllByToMember(member.getId());
        if(memberNickname.equals(requestingMemberNickname)) {
            Set<String> usernameFollowedByMember = getAllMemberNicknameFollowedByMember(member.getId());
            for(UserSimpleInfoDto memberInfo : members) {
                setFollowStatus(memberInfo.getNickname(), member.getNickname(), usernameFollowedByMember, memberInfo);
            }
        }
        else {
            Set<String> memberNicknameFollowedByRequestingMember = getAllMemberNicknameFollowedByMember(requestingMember.getId());
            for(UserSimpleInfoDto memberInfo : members) {
                memberInfo.setNickname(requestingMemberNickname);
                setFollowStatus(memberInfo.getNickname(), requestingMember.getNickname(), memberNicknameFollowedByRequestingMember, memberInfo);
            }
        }
        return members;

    }

    public List<UserSimpleInfoDto> getFollowingList(String memberNickname,String requestingMemberNickname){
        Member member = memberRepository.findByNickname(memberNickname);
        Member requestingMember;
        if(requestingMemberNickname.equals(memberNickname)) requestingMember = member;
        else requestingMember = memberRepository.findByNickname(requestingMemberNickname);

        List<UserSimpleInfoDto> members = getAllByFromMember(member.getId());
        if(memberNickname.equals(requestingMemberNickname)) {
            for(UserSimpleInfoDto memberInfo : members) {
                memberInfo.setNickname(memberNickname);
                memberInfo.setFollowStatus(FollowStatus.FOLLOWING);
            }
        }
        else {
            Set<String> memberNicknameFollowedByRequestingMember = getAllUsernameFollowedByUser(requestingMember.getId());
            for(UserSimpleInfoDto memberInfo : members) {
                memberInfo.setRequestingUsername(requestingMemberNickname);
                setFollowStatus(memberInfo.getNickname(), requestingMemberNickname, memberNicknameFollowedByRequestingMember, memberInfo);
            }
        }
        return members;
    }



    public Long getFollowingCount(String memberNickName){
        Member member = memberRepository.findByNickname(memberNickName);
        return followRepository.countByFromMember(member.getId());
    }

    public Long getFollowerCount(String memberNickName){
        Member member = memberRepository.findByNickname(memberNickName);
        return followRepository.countByToMember(member.getId());
    }
    public Pair<Long, Long> getProfileFollowCount(String memberNickName){
        return Pair.of(getFollowerCount(memberNickName), getFollowingCount(memberNickName));
    }

    public void deleteFollowRelation(Long memberId){
        followRepository.deleteAllByFromMember(memberId);
        followRepository.deleteAllByToMember(memberId);
    }

    public boolean isFollowingById(Long toMemberId, Long fromMemberId) {
        return followRepository.findByToMemberAndFromMember(toMemberId, fromMemberId).isPresent();
    }

    public boolean isFollowingByUsername(String toMemberNickname, String fromMemberNickname) {
        Member toMember = memberRepository.findByNickname(toMemberNickname);
        Member fromMember = memberRepository.findByNickname(fromMemberNickname);
        return isFollowingById(toMember.getId(), fromMember.getId());
    }

    public void setFollowStatus(String targetUsername, String requestingUsername, Set<String> usernameFollowedByRequestingUser, UserSimpleInfoDto memberInfo) {
        if(requestingUsername.equals(targetUsername))
            memberInfo.setFollowStatus(FollowStatus.ONESELF);
        else if(usernameFollowedByRequestingUser.contains(targetUsername))
            memberInfo.setFollowStatus(FollowStatus.FOLLOWING);
        else
            memberInfo.setFollowStatus(FollowStatus.UNFOLLOW);
    }

    public Set<String> getAllMemberNicknameFollowedByMember(Long memberId) {
        List<Member> members = followRepository.findAllByFromMember(memberId);
        Set<String> MemberNicknameSet = new HashSet<>();
        for(Member member : members) {
            MemberNicknameSet.add(member.getNickname());
        }
        return MemberNicknameSet;
    }

    public List<UserSimpleInfoDto> getAllByToMember(Long memberId) {
        List<Member> members = followRepository.findAllByToMember(memberId);
        List<UserSimpleInfoDto> followerList = new ArrayList<>();
        for(Member member : members) {
            followerList.add(new UserSimpleInfoDto(member.getId(), member.getNickname()));
        }
        return followerList;
    }

    public List<UserSimpleInfoDto> getAllByFromMember(Long memberId) {
        List<Member> members = followRepository.findAllByFromMember(memberId);
        List<UserSimpleInfoDto> followingList = new ArrayList<>();
        for(Member member : members) {
            followingList.add(new UserSimpleInfoDto(member.getId(), member.getNickname()));
        }
        return followingList;
    }

    public Set<String> getAllUsernameFollowedByUser(Long memberId) {
        List<Member> members = followRepository.findAllByFromMember(memberId);
        Set<String> memberNicknameSet = new HashSet<>();
        for(Member member : members) {
            memberNicknameSet.add(member.getNickname());
        }
        return memberNicknameSet;
    }
}
