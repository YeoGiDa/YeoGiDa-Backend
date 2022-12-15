package com.Udemy.YeoGiDa.domain.member.repository;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.entity.QMember;
import com.Udemy.YeoGiDa.domain.member.entity.QMemberImg;
import com.Udemy.YeoGiDa.domain.member.response.MemberDto;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Udemy.YeoGiDa.domain.member.entity.QMember.*;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Member findByEmailFetch(String email) {
        return queryFactory.selectFrom(member)
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .where(member.email.eq(email))
                .fetchOne();
    }

    @Override
    public List<Member> findAllByMemberOrderByHeartCountBasicFetch() {
        return queryFactory.selectFrom(member)
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .orderBy(member.heartCount.desc(), member.id.desc())
                .where(member.heartCount.goe(0))
                .limit(10)
                .fetch();
    }

    @Override
    public List<Member> findAllByMemberOrderByHeartCountMoreFetch() {
        return queryFactory.selectFrom(member)
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .orderBy(member.heartCount.desc(), member.id.desc())
                .where(member.heartCount.goe(0))
                .fetch();
    }
}
