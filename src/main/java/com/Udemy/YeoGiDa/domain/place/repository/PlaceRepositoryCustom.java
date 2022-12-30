package com.Udemy.YeoGiDa.domain.place.repository;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.place.entity.Place;

import java.util.List;

public interface PlaceRepositoryCustom {

//    List<Place> findAllByTripIdAndCondition(Long tripId, String condition);

    List<Place> findAllByTripIdAndTagAndCondition(Long tripId, String tag, String condition);

    List<Place> findAllByComment(Member member);

    List<Place> findAllByTripId(Long tripId);



//    List<Place> findAllByTripIdOrderByStar(Long tripId);

//    List<Place> findAllByTripIdOrderByComment(Long tripId);

//    List<Place> findAllByTagDefault(Long tripId,String tag);
//
//
//    List<Place> findAllByTagOrderById(Long tripId,String tag);
//
//    List<Place> findAllByTagOrderByStar(Long tripId,String tag);
//
//    List<Place> findAllByTagOrderByComment(Long tripId,String tag);
}
