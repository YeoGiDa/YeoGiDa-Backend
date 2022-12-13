package com.Udemy.YeoGiDa.domain.trip.repository;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
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
    public List<Trip> findAllOrderByIdDesc() {
        return queryFactory.selectFrom(trip)
                .orderBy(trip.id.desc())
                .fetch();
    }

    @Override
    public List<Trip> findAllOrderByHeartCount() {
        return queryFactory.selectFrom(trip)
                .orderBy(trip.heartCount.desc(), trip.id.desc())
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
                .orderBy(trip.heartCount.desc(), trip.id.desc())
                .fetch();
    }

    @Override
    public List<Trip> findAllOrderByChangeHeartCountBasic() {
        return queryFactory.selectFrom(trip)
                .orderBy(trip.changeHeartCount.desc(), trip.id.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<Trip> findAllOrderByChangeHeartCountMore() {
        return queryFactory.selectFrom(trip)
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
