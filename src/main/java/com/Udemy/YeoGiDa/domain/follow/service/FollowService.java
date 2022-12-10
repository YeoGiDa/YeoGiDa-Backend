//package com.Udemy.YeoGiDa.domain.follow.service;
//
//import com.Udemy.YeoGiDa.domain.follow.entity.Follow;
//import com.Udemy.YeoGiDa.domain.follow.exception.FollowException;
//import com.Udemy.YeoGiDa.domain.follow.repository.FollowRepository;
//import com.Udemy.YeoGiDa.domain.follow.response.FollowResponseDto;
//import com.Udemy.YeoGiDa.domain.member.entity.Member;
//import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
//import com.Udemy.YeoGiDa.domain.member.repository.MemberRepository;
//import com.mysema.commons.lang.Pair;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class FollowService {
//
//    private final MemberRepository memberRepository;
//    private final FollowRepository followRepository;
//
//
//
//    public List<FollowResponseDto> getFollowerList(String memberNickname,String requestingMemberNickname){
//        Member member = userRepository.findByUsername(username).orElseThrow(UserException::new);
//        User requestingUser;
//        if(requestingUsername.equals(username)) requestingUser = user;
//        else requestingUser = userRepository.findByUsername(requestingUsername).orElseThrow(UserException::new);
//
//        List<UserSimpleInfoDto> users = getAllByToUser(user.getId());
//        if(username.equals(requestingUsername)) {
//            Set<String> usernameFollowedByUser = getAllUsernameFollowedByUser(user.getId());
//            for(UserSimpleInfoDto userInfo : users) {
//                setFollowStatus(userInfo.getUsername(), user.getUsername(), usernameFollowedByUser, userInfo);
//            }
//        }
//        else {
//            Set<String> usernameFollowedByRequestingUser = getAllUsernameFollowedByUser(requestingUser.getId());
//            for(UserSimpleInfoDto userInfo : users) {
//                userInfo.setRequestingUsername(requestingUsername);
//                setFollowStatus(userInfo.getUsername(), requestingUser.getUsername(), usernameFollowedByRequestingUser, userInfo);
//            }
//        }
//        return users;
//
//    }
//
//    public List<FollowResponseDto> getFollowingList(Long memberId){
//        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
//        return followRepository.findAllByFromMember(member.getId());
//    }
//
//
//    public Boolean addFollow(String toMemberNickName, String fromMemberNickName){
//        checkSameMember(toMemberNickName, fromMemberNickName);
//
//        Member toMember = memberRepository.findByNickname(toMemberNickName);
//        Member fromMember = memberRepository.findByNickname(fromMemberNickName);
//
//        Optional<Follow> relation = getFollowRelation(toMember.getId(), fromMember.getId());
//        if(relation.isPresent())
//            throw new FollowException();
//        followRepository.save(new Follow(toMember.getId(),  fromMember.getId()));
//        return true;
//    }
//
//    public Boolean unFollow(String toMemberNickName, String fromMemberNickName) {
//        checkSameMember(toMemberNickName, fromMemberNickName);
//
//        Member toMember = memberRepository.findByNickname(toMemberNickName);
//        Member fromMember = memberRepository.findByNickname(fromMemberNickName);
//
//        Optional<Follow> relation = getFollowRelation(toMember.getId(), fromMember.getId());
//        if(relation.isEmpty())
//            throw new FollowException();
//        followRepository.delete(relation.get());
//        return true;
//    }
//
//    private Optional<Follow> getFollowRelation(Long toMemberId, Long fromMemberId) {
//        return followRepository.findByToMemberAndFromMember(toMemberId,fromMemberId);
//    }
//
//    private void checkSameMember(String toMemberNickName, String fromMemberNickName) {
//
//        if(toMemberNickName.equals(fromMemberNickName)) throw new FollowException();
//    }
//
//    public Long getFollowingCount(String memberNickName){
//        Member member = memberRepository.findByNickname(memberNickName);
//        return followRepository.countByFromMember(member.getId());
//    }
//
//    public Long getFollowerCount(String memberNickName){
//        Member member = memberRepository.findByNickname(memberNickName);
//        return followRepository.countByToMember(member.getId());
//    }
//    public Pair<Long, Long> getProfileFollowCount(String memberNickName){
//        return Pair.of(getFollowerCount(memberNickName), getFollowingCount(memberNickName));
//    }
//
//    public void deleteFollowRelation(Long userId){
//        followRepository.deleteAllByFromUser(userId);
//        followRepository.deleteAllByToUser(userId);
//    }
//
//}
