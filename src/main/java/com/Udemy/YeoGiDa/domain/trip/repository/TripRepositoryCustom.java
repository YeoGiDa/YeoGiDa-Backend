package com.Udemy.YeoGiDa.domain.trip.repository;

import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepositoryCustom {

    //내가 작성한 글 목록
    List<Trip> findAllByMemberFetch();

    //지역별 여행지 목록 - 최신순 정렬
    List<Trip> findAllByRegionDesc(String region);

    //지역별 여행지 목록 - 좋아요 순 정렬
    List<Trip> findAllByRegionOrderByHeartCount(String region);

    //모든 여행지 목록 - 좋아요 순 정렬
    List<Trip> findAllOrderByHeartCount();

    //월간 베스트 여행지 목록
    List<Trip> findAllOrderByChangeHeartCount();
}
