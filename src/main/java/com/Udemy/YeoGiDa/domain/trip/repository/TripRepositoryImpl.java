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
}
