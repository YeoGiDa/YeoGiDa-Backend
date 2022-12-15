package com.Udemy.YeoGiDa.domain.trip.repository;

import com.Udemy.YeoGiDa.domain.heart.entity.QHeart;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.entity.QMember;
import com.Udemy.YeoGiDa.domain.member.entity.QMemberImg;
import com.Udemy.YeoGiDa.domain.trip.entity.QTripImg;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Udemy.YeoGiDa.domain.member.entity.QMember.member;
import static com.Udemy.YeoGiDa.domain.trip.entity.QTrip.trip;

@RequiredArgsConstructor
public class TripRepositoryImpl implements TripRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Trip> findAllOrderByIdDescFetch() {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .orderBy(trip.id.desc())
                .fetch();
    }

    @Override
    public List<Trip> findAllOrderByHeartCountFetch() {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .orderBy(trip.hearts.size().desc(), trip.id.desc())
                .fetch();
    }

    @Override
    public List<Trip> findAllByRegionDescFetch(String region) {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .where(trip.region.eq(region))
                .orderBy(trip.id.desc())
                .fetch();
    }

    @Override
    public List<Trip> findAllByRegionOrderByHeartCountFetch(String region) {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(trip, QHeart.heart.trip).fetchJoin()
                .where(trip.region.eq(region))
                .orderBy(trip.hearts.size().desc(), trip.id.desc())
                .fetch();
    }

    @Override
    public List<Trip> findAllOrderByChangeHeartCountBasicFetch() {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.member, member).fetchJoin()
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .orderBy(trip.changeHeartCount.desc(), trip.id.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<Trip> findAllOrderByChangeHeartCountMoreFetch() {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.member, member).fetchJoin()
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .orderBy(trip.changeHeartCount.desc(), trip.id.desc())
                .fetch();
    }

    @Override
    public List<Trip> findAllByMemberFetch(Member m) {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.member, member).fetchJoin()
                .where(trip.member.id.eq(m.getId()))
                .orderBy(trip.id.desc())
                .fetch();
    }
}
