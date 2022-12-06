package com.Udemy.YeoGiDa.domain.place.repository;

import com.Udemy.YeoGiDa.domain.place.entity.Place;

import java.util.List;

public interface PlaceRepositoryCustom {

    List<Place> findAllByTripIdOrderById(Long tripId);

    List<Place> findAllByTripIdOrderByStar(Long tripId);

    //TODO - 댓글 완성 후 댓글 정렬
//    List<Place> findAllByTripIdOrderByComment(Long tripId);


}
