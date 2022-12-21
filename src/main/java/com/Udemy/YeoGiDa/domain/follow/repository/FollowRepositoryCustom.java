package com.Udemy.YeoGiDa.domain.follow.repository;

import com.Udemy.YeoGiDa.domain.follow.entity.Follow;
import com.querydsl.core.Tuple;

import java.util.List;

public interface FollowRepositoryCustom {

    List<Tuple> findAllByToMember(Long memberId);
    List<Follow> findAllByFromMember(Long memberId);
}
