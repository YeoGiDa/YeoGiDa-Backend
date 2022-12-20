package com.Udemy.YeoGiDa.domain.follow.service;

import com.Udemy.YeoGiDa.domain.alarm.entity.Alarm;
import com.Udemy.YeoGiDa.domain.alarm.entity.AlarmType;
import com.Udemy.YeoGiDa.domain.alarm.exception.AlarmNotFoundException;
import com.Udemy.YeoGiDa.domain.alarm.repository.AlarmRepository;
import com.Udemy.YeoGiDa.domain.follow.entity.Follow;
import com.Udemy.YeoGiDa.domain.follow.exception.AlreadyFollowException;
import com.Udemy.YeoGiDa.domain.follow.exception.FollowNotFoundException;
import com.Udemy.YeoGiDa.domain.follow.repository.FollowRepository;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.member.repository.MemberRepository;
import com.Udemy.YeoGiDa.domain.member.response.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;

    public List<MemberDto> getFollowingList(Member member){
        return followRepository.findAllByFromMemberId(member.getId())
                .stream()
                .map(MemberDto::new)
                .collect(Collectors.toList());
    }

    public List<MemberDto> getFollowerList(Member member){
        return followRepository.findAllByToMemberId(member.getId())
                .stream()
                .map(MemberDto::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public boolean addFollow(Long toMemberId, Long fromMemberId){
        Member toMember = memberRepository.findById(toMemberId).orElseThrow(() -> new MemberNotFoundException());
        Member fromMember = memberRepository.findById(fromMemberId).orElseThrow(() -> new MemberNotFoundException());

        Optional<Follow> relation = getFollowRelation(toMember.getId(), fromMember.getId());

        if(relation.isPresent()) {
            throw new AlreadyFollowException();
        }

        followRepository.save(new Follow(toMemberId, fromMemberId));

        //알람 추가
        alarmRepository.save(Alarm.builder()
                .member(toMember)
                .alarmType(AlarmType.NEW_FOLLOW)
                .makeAlarmMemberId(fromMemberId)
                .placeId(null)
                .targetId(fromMemberId)
                .build());

        return true;
    }

    @Transactional
    public boolean unFollow(Long toMemberId, Long fromMemberId){
        Member toMember = memberRepository.findById(toMemberId).orElseThrow(() -> new MemberNotFoundException());
        Member fromMember = memberRepository.findById(fromMemberId).orElseThrow(() -> new MemberNotFoundException());

        Optional<Follow> relation = getFollowRelation(toMember.getId(), fromMember.getId());

        if(relation.isEmpty()) {
            throw new FollowNotFoundException();
        }

        followRepository.delete(relation.get());

        //알람 삭제
        Alarm findAlarm = alarmRepository.findFollowAlarmByMemberAndMakeMemberId(toMember, fromMemberId);
        if(findAlarm == null) {
            throw new AlarmNotFoundException();
        }
        alarmRepository.delete(findAlarm);
        return true;
    }


    private Optional<Follow> getFollowRelation(Long toMemberId, Long fromMemberId) {
        return followRepository.findByToMemberIdAndFromMemberId(toMemberId, fromMemberId);
    }

}
