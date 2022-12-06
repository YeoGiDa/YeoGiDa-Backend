package com.Udemy.YeoGiDa.domain.place.service;



import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.exception.PlaceNotFoundException;
import com.Udemy.YeoGiDa.domain.place.repository.PlaceRepository;
import com.Udemy.YeoGiDa.domain.place.request.PlaceSaveRequestDto;
import com.Udemy.YeoGiDa.domain.place.response.PlaceDetailResponseDto;
import com.Udemy.YeoGiDa.domain.place.response.PlaceListResponseDto;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.Udemy.YeoGiDa.domain.trip.exception.TripNotFoundException;
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
public class PlaceService {

    private final PlaceRepository placeRepository;

    @Transactional(readOnly = true)
    public List<PlaceListResponseDto> getPlaceListOrderById(Long tripId){
        return placeRepository.findAllByTripIdOrderById(tripId)
                .stream()
                .map(PlaceListResponseDto::new)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<PlaceListResponseDto> getPlaceListOrderByStar(Long tripId){
        return placeRepository.findAllByTripIdOrderByStar(tripId)
                .stream()
                .map(PlaceListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlaceDetailResponseDto getPlaceDetail(Long placeId) {
        Place place = Optional.ofNullable(placeRepository.findById(placeId)
                .orElseThrow(() -> new TripNotFoundException())).get();
        return new PlaceDetailResponseDto(place);
    }

    public PlaceDetailResponseDto save(PlaceSaveRequestDto placeSaveRequestDto, Trip trip) {
        if(trip == null) {
            throw new TripNotFoundException();
        }

        Place place = Place.builder()
                .title(placeSaveRequestDto.getTitle())
                .address(placeSaveRequestDto.getAddress())
                .content(placeSaveRequestDto.getContent())
                .star(placeSaveRequestDto.getStar())
                .trip(trip)
                .build();

        Place savePlace = placeRepository.save(place);

        return new PlaceDetailResponseDto(savePlace);
    }


    public Long delete(Long placeId, Trip trip) {
        if(trip == null) {
            throw new ForbiddenException();
        }

        Place place = Optional.ofNullable(placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException())).get();

        placeRepository.delete(place);

        return place.getId();
    }

    public Long update(Long placeId, PlaceSaveRequestDto placeSaveRequestDto, Trip trip ) {
        if(trip == null) {
            throw new TripNotFoundException();
        }

        Place place = Optional.ofNullable(placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException())).get();


        place.update(placeSaveRequestDto.getTitle(), placeSaveRequestDto.getAddress(),
                placeSaveRequestDto.getContent(),placeSaveRequestDto.getStar(),placeSaveRequestDto.getImgUrl());

        return place.getId();
    }



}
