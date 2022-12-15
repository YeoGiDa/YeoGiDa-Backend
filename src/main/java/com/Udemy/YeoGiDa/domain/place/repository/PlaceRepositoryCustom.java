package com.Udemy.YeoGiDa.domain.place.repository;

import com.Udemy.YeoGiDa.domain.place.entity.Place;

import java.util.List;
import java.util.Optional;

public interface PlaceRepositoryCustom {

    Optional<Place> findByIdFetch(Long placeId);

    List<Place> findAllByTripIdOrderById(Long tripId);

    List<Place> findAllByTripIdOrderByStar(Long tripId);

    //TODO - 댓글 완성 후 댓글 정렬
    List<Place> findAllByTripIdOrderByComment(Long tripId);

    List<Place> findAllByTag(String tag);
}
