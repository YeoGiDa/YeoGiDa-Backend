package com.Udemy.YeoGiDa.domain.follow.repository;

import com.Udemy.YeoGiDa.domain.follow.entity.Follow;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Udemy.YeoGiDa.domain.follow.entity.QFollow.*;
import static com.Udemy.YeoGiDa.domain.member.entity.QMember.*;

@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom{

    private final JPAQueryFactory queryFactory;

//    @Override
//    public List<Tuple> findAllByFromMember(Long memberId) {
//        return queryFactory.select(follow, member)
//                .from(follow)
//                .leftJoin(member).on(follow.fromMemberId.eq(member.id)).fetchJoin()
//                .where(follow.fromMemberId.eq(memberId))
//                .fetch();
//    }

    @Override   //세타조인
    public List<Follow> findAllByFromMember(Long memberId) {
        return queryFactory.select(follow)
                .from(follow, member)
                .where(follow.fromMemberId.eq(memberId))
                .fetch();
    }


    @Override
    public List<Tuple> findAllByToMember(Long memberId) {
        return queryFactory.select(follow, member)
                .from(follow)
                .leftJoin(member).on(follow.toMemberId.eq(member.id)).fetchJoin()
                .where(follow.toMemberId.eq(memberId))
                .fetch();
    }
}
