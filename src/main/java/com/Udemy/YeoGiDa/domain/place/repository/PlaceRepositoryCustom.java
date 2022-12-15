package com.Udemy.YeoGiDa.domain.place.repository;

import com.Udemy.YeoGiDa.domain.place.entity.Place;

import java.util.List;
import java.util.Optional;

public interface PlaceRepositoryCustom {

    List<Place> findAllByTripId(Long tripId,String condition);

//    List<Place> findAllByTripIdOrderById(Long tripId);
//
//    List<Place> findAllByTripIdOrderByStar(Long tripId);
//
//    List<Place> findAllByTripIdOrderByComment(Long tripId);

//    List<Place> findAllByTagDefault(Long tripId,String tag);
//
//
//    List<Place> findAllByTagOrderById(Long tripId,String tag);
//
//    List<Place> findAllByTagOrderByStar(Long tripId,String tag);
//
//    List<Place> findAllByTagOrderByComment(Long tripId,String tag);

    List<Place> findAllByTagDefaultTest(Long tripId,String tag,String condition);


}
