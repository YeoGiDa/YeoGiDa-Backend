package com.Udemy.YeoGiDa.domain.trip.repository;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepositoryCustom {

    //모든 여행지 목록 최신순 - 페이징 필요
    List<Trip> findAllOrderByIdDesc();

    //모든 여행지 목록 - 좋아요 순 정렬 - 페이징 필요
    List<Trip> findAllOrderByHeartCount();

    //지역별 여행지 목록 - 최신순 정렬 - 페이징 필요
    List<Trip> findAllByRegionDesc(String region);

    //지역별 여행지 목록 - 좋아요 순 정렬 - 페이징 필요
    List<Trip> findAllByRegionOrderByHeartCount(String region);

    //월간 베스트 여행지 목록 기본 10개
    List<Trip> findAllOrderByChangeHeartCountBasic();

    //월간 베스트 여행지 목록 더 보기 - 페이징 필요
    List<Trip> findAllOrderByChangeHeartCountMore();

    //내가 작성한 글 목록
    List<Trip> findAllByMemberFetch(Member m);
}
