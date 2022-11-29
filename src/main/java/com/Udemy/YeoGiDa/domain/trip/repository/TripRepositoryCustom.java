package com.Udemy.YeoGiDa.domain.trip.repository;

import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepositoryCustom {

    //내가 작성한 글 목록
    List<Trip> findAllByMemberFetch();

}
