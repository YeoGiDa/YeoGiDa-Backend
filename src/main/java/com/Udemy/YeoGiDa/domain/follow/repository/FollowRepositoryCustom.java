package com.Udemy.YeoGiDa.domain.follow.repository;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.querydsl.core.Tuple;

import java.util.List;

public interface FollowRepositoryCustom {

    List<Tuple> findAllByToMember(Long memberId);
    List<Tuple> findAllByFromMember(Long memberId);


}
