package com.Udemy.YeoGiDa.domain.place.repository;

import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.entity.QPlace;
import com.Udemy.YeoGiDa.domain.trip.entity.QTrip;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.Udemy.YeoGiDa.domain.place.entity.QPlace.*;
import static com.Udemy.YeoGiDa.domain.trip.entity.QTrip.*;

@RequiredArgsConstructor
public class PlaceRepositoryImpl implements PlaceRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Place> findAllByTripIdOrderById(Long tripId) {
        return queryFactory.selectFrom(place)
                .where(place.trip.id.eq(tripId))
                .orderBy(place.id.desc())
                .fetch();
    }

    @Override
    public List<Place> findAllByTripIdOrderByStar(Long tripId) {
        return queryFactory.selectFrom(place)
                .where(place.trip.id.eq(tripId))
                .orderBy(place.star.desc(),place.id.desc())
                .fetch();
    }
}