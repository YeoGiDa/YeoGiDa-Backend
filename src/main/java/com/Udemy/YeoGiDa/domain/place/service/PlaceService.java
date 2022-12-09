package com.Udemy.YeoGiDa.domain.place.service;



import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.exception.PlaceNotFoundException;
import com.Udemy.YeoGiDa.domain.place.repository.PlaceRepository;
import com.Udemy.YeoGiDa.domain.place.request.PlaceSaveRequestDto;
import com.Udemy.YeoGiDa.domain.place.request.PlaceUpdateRequestDto;
import com.Udemy.YeoGiDa.domain.place.response.PlaceDetailResponseDto;
import com.Udemy.YeoGiDa.domain.place.response.PlaceListResponseDto;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.Udemy.YeoGiDa.domain.trip.exception.TripNotFoundException;
import com.Udemy.YeoGiDa.domain.trip.repository.TripRepository;
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
    private final TripRepository tripRepository;

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
    public List<PlaceListResponseDto> getPlaceListByTagDesc(String tag){
        return placeRepository.findAllByTag(tag)
                .stream()
                .map(PlaceListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlaceDetailResponseDto getPlaceDetail(Long placeId) {
        Place place = Optional.ofNullable(placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException())).get();

        return new PlaceDetailResponseDto(place);
    }

    public PlaceDetailResponseDto save(PlaceSaveRequestDto placeSaveRequestDto, Long tripId, Member member) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException());

        if(member == null){
            throw new MemberNotFoundException();
        }

        if(trip.getMember() != member){
            throw new ForbiddenException();
        }

        Place place = Place.builder()
                .title(placeSaveRequestDto.getTitle())
                .address(placeSaveRequestDto.getAddress())
                .longitude(placeSaveRequestDto.getLongitude())
                .latitude(placeSaveRequestDto.getLatitude())
                .content(placeSaveRequestDto.getContent())
                .star(placeSaveRequestDto.getStar())
                .trip(trip)
                .tag(placeSaveRequestDto.getTag())
                .build();

        Place savePlace = placeRepository.save(place);

        return new PlaceDetailResponseDto(savePlace);
    }

    public void update(PlaceUpdateRequestDto placeUpdateRequestDto,Long placeId, Member member) {

        Place place = Optional.ofNullable(placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException())).get();

        if(member == null){
            throw new MemberNotFoundException();
        }

        if(place.getTrip().getMember() != member){
            throw new ForbiddenException();
        }

        place.update(placeUpdateRequestDto.getTitle(), placeUpdateRequestDto.getAddress(),
                placeUpdateRequestDto.getContent(),placeUpdateRequestDto.getLongitude(),
                placeUpdateRequestDto.getLatitude(), placeUpdateRequestDto.getStar(),
                placeUpdateRequestDto.getTag());
    }

    public void delete(Long placeId,Member member) {

        Place place = Optional.ofNullable(placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException())).get();

        if(member == null){
            throw new MemberNotFoundException();
        }

        if(place.getTrip().getMember() != member){
            throw new ForbiddenException();
        }

        placeRepository.delete(place);
    }
}