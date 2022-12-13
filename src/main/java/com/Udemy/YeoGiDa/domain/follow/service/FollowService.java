package com.Udemy.YeoGiDa.domain.follow.service;

import com.Udemy.YeoGiDa.domain.follow.entity.Follow;
import com.Udemy.YeoGiDa.domain.follow.exception.AlreadyFollowException;
import com.Udemy.YeoGiDa.domain.follow.exception.FollowNotFoundException;
import com.Udemy.YeoGiDa.domain.follow.repository.FollowRepository;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.member.repository.MemberRepository;
import com.Udemy.YeoGiDa.domain.member.response.MemberDto;
import com.querydsl.core.Tuple;
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

    public List<MemberDto> getFollowingList(Long memberId){
        return followRepository.findAllByFromMemberId(memberId)
                .stream()
                .map(MemberDto::new)
                .collect(Collectors.toList());
    }

    public List<MemberDto> getFollowerList(Long memberId){
        return followRepository.findAllByToMemberId(memberId)
                .stream()
                .map(MemberDto::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public boolean addFollow(Long toMemberId, Long fromMemberId){
        Member toMember = memberRepository.findById(toMemberId).orElseThrow(() -> new MemberNotFoundException());
        Member fromMember = memberRepository.findById(fromMemberId).orElseThrow(() -> new MemberNotFoundException());

        Optional<Follow> relation = getFollowRelation(toMemberId, fromMemberId);

        if(relation.isPresent()) {
            throw new AlreadyFollowException();
        }

        followRepository.save(new Follow(toMemberId, fromMemberId));

        return true;
    }

    @Transactional
    public boolean unFollow(Long toMemberId, Long fromMemberId){
        Member toMember = memberRepository.findById(toMemberId).orElseThrow(() -> new MemberNotFoundException());
        Member fromMember = memberRepository.findById(fromMemberId).orElseThrow(() -> new MemberNotFoundException());

        Optional<Follow> relation = getFollowRelation(toMemberId, fromMemberId);

        if(relation.isEmpty()) {
            throw new FollowNotFoundException();
        }

        followRepository.delete(relation.get());

        return true;
    }


    private Optional<Follow> getFollowRelation(Long toMemberId, Long fromMemberId) {
        return followRepository.findByToMemberIdAndFromMemberId(toMemberId, fromMemberId);
    }

}
