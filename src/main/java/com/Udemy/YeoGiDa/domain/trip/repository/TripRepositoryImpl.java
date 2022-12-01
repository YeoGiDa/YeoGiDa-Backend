package com.Udemy.YeoGiDa.domain.trip.repository;

import com.Udemy.YeoGiDa.domain.member.entity.QMember;
import com.Udemy.YeoGiDa.domain.trip.entity.QTrip;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Udemy.YeoGiDa.domain.member.entity.QMember.*;
import static com.Udemy.YeoGiDa.domain.trip.entity.QTrip.*;

@RequiredArgsConstructor
public class TripRepositoryImpl implements TripRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Trip> findAllByMemberFetch() {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.member, member).fetchJoin()
                .fetch();
    }

    @Override
    public List<Trip> findAllOrderByHeartCount() {
        return queryFactory.selectFrom(trip)
                .orderBy(trip.heartCount.desc())
                .fetch();
    }

    @Override
    public List<Trip> findAllOrderByChangeHeartCount() {
        return queryFactory.selectFrom(trip)
                .orderBy(trip.changeHeartCount.desc())
                .fetch();
    }

    @Override
    public List<Trip> findAllByRegionDesc(String region) {
        return queryFactory.selectFrom(trip)
                .where(trip.region.eq(region))
                .orderBy(trip.id.desc())
                .fetch();
    }

    @Override
    public List<Trip> findAllByRegionOrderByHeartCount(String region) {
        return queryFactory.selectFrom(trip)
                .where(trip.region.eq(region))
                .orderBy(trip.heartCount.desc())
                .fetch();
    }
}
