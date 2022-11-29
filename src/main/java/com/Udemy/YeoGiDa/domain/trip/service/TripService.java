package com.Udemy.YeoGiDa.domain.trip.service;

import com.Udemy.YeoGiDa.domain.heart.repository.HeartRepository;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.Udemy.YeoGiDa.domain.trip.exception.TripNotFoundException;
import com.Udemy.YeoGiDa.domain.trip.repository.TripRepository;
import com.Udemy.YeoGiDa.domain.trip.request.TripSaveRequestDto;
import com.Udemy.YeoGiDa.domain.trip.response.TripDetailResponseDto;
import com.Udemy.YeoGiDa.domain.trip.response.TripListResponseDto;
import com.Udemy.YeoGiDa.global.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TripService {

    private final TripRepository tripRepository;
    private final HeartRepository heartRepository;

    @Transactional(readOnly = true)
    public List<TripListResponseDto> getTripList() {
        return tripRepository.findAllDesc()
                .stream()
                .map(TripListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TripDetailResponseDto getTripDetail(Long tripId) {
        Trip trip = Optional.ofNullable(tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException())).get();

        return new TripDetailResponseDto(trip);
    }

    public TripDetailResponseDto save(TripSaveRequestDto tripSaveRequestDto, Member member) {
        if(member == null) {
            throw new ForbiddenException();
        }

        Trip trip = Trip.builder()
                .region(tripSaveRequestDto.getRegion())
                .title(tripSaveRequestDto.getTitle())
                .subTitle(tripSaveRequestDto.getSubTitle())
                .member(member)
                .imgUrl(tripSaveRequestDto.getImgUrl())
                .build();

        Trip saveTrip = tripRepository.save(trip);

        return new TripDetailResponseDto(saveTrip);
    }

    public TripDetailResponseDto update(Long tripId, TripSaveRequestDto tripSaveRequestDto, Member member) {
        if(member == null) {
            throw new ForbiddenException();
        }

        Trip trip = Optional.ofNullable(tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException())).get();

        if(trip.getMember().getId() != member.getId()) {
            throw new ForbiddenException();
        }

        trip.update(tripSaveRequestDto.getRegion(), tripSaveRequestDto.getTitle(),
                tripSaveRequestDto.getSubTitle(), tripSaveRequestDto.getImgUrl());

        return new TripDetailResponseDto(trip);
    }

    public Long delete(Long tripId, Member member) {
        if(member == null) {
            throw new ForbiddenException();
        }

        Trip trip = Optional.ofNullable(tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException())).get();

        if(trip.getMember().getId() != member.getId()) {
            throw new ForbiddenException();
        }

        tripRepository.delete(trip);
        return tripId;
    }
}
