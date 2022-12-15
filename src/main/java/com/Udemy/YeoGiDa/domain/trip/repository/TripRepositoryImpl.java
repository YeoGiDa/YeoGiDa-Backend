package com.Udemy.YeoGiDa.domain.trip.repository;

import com.Udemy.YeoGiDa.domain.heart.entity.QHeart;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.entity.QMember;
import com.Udemy.YeoGiDa.domain.member.entity.QMemberImg;
import com.Udemy.YeoGiDa.domain.place.entity.QPlace;
import com.Udemy.YeoGiDa.domain.place.entity.QPlaceImg;
import com.Udemy.YeoGiDa.domain.trip.entity.QTripImg;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Udemy.YeoGiDa.domain.member.entity.QMember.member;
import static com.Udemy.YeoGiDa.domain.place.entity.QPlace.place;
import static com.Udemy.YeoGiDa.domain.trip.entity.QTrip.trip;

@RequiredArgsConstructor
public class TripRepositoryImpl implements TripRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Trip> findAllByConditionFetch(String condition) {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(trip.member, member).fetchJoin()
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .orderBy(conditionParam(condition))
                .fetch();
    }

    @Override
    public List<Trip> findAllByRegionAndConditionFetch(String region, String condition) {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(trip.member, member).fetchJoin()
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .where(trip.region.eq(region))
                .orderBy(conditionParam(condition))
                .fetch();
    }

    private OrderSpecifier conditionParam(String condition) {
        if (StringUtils.isNullOrEmpty(condition)) {
            return trip.id.asc();
        } else if (condition.equals("id")) {
            return trip.id.desc();
        } else if (condition.equals("heart")) {
            return trip.hearts.size().desc();
        } throw new IllegalArgumentException();
    }

    @Override
    public List<Trip> findAllOrderByChangeHeartCountBasicFetch() {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(trip.member, member).fetchJoin()
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .orderBy(trip.changeHeartCount.desc(), trip.id.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<Trip> findAllOrderByChangeHeartCountMoreFetch() {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(trip.member, member).fetchJoin()
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .orderBy(trip.changeHeartCount.desc(), trip.id.desc())
                .fetch();
    }

    @Override
    public List<Trip> findAllByMemberFetch(Member m) {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(trip.member, member).fetchJoin()
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .where(trip.member.id.eq(m.getId()))
                .orderBy(trip.id.desc())
                .fetch();
    }
}
