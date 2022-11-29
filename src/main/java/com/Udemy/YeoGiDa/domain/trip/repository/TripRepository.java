package com.Udemy.YeoGiDa.domain.trip.repository;

import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    //모든 여행지 목록 - 최신순
    @Query("SELECT t FROM Trip t ORDER BY t.id DESC")
    List<Trip> findAllDesc();


    Optional<Trip> findById(Long tripId);
}
