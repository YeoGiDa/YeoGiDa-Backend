package com.Udemy.YeoGiDa.domain.trip.service;

import com.Udemy.YeoGiDa.domain.common.exception.ImgNotFoundException;
import com.Udemy.YeoGiDa.domain.common.service.S3Service;
import com.Udemy.YeoGiDa.domain.heart.entity.Heart;
import com.Udemy.YeoGiDa.domain.heart.exception.AlreadyHeartException;
import com.Udemy.YeoGiDa.domain.heart.exception.HeartNotFoundException;
import com.Udemy.YeoGiDa.domain.heart.repository.HeartRepository;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.Udemy.YeoGiDa.domain.trip.entity.TripImg;
import com.Udemy.YeoGiDa.domain.trip.exception.TripNotFoundException;
import com.Udemy.YeoGiDa.domain.trip.repository.TripImgRepository;
import com.Udemy.YeoGiDa.domain.trip.repository.TripRepository;
import com.Udemy.YeoGiDa.domain.trip.request.TripSaveRequestDto;
import com.Udemy.YeoGiDa.domain.trip.response.TripDetailResponseDto;
import com.Udemy.YeoGiDa.domain.trip.response.TripListResponseDto;
import com.Udemy.YeoGiDa.domain.trip.response.TripMonthBestListResponseDto;
import com.Udemy.YeoGiDa.global.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TripService {

    private final TripRepository tripRepository;
    private final TripImgRepository tripImgRepository;
    private final S3Service s3Service;
    private final HeartRepository heartRepository;

    public List<TripListResponseDto> getTripList(String condition) {
        return tripRepository.findAllByConditionFetch(condition)
                .stream()
                .map(TripListResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<TripListResponseDto> getTripListByRegion(String region, String condition) {
        return tripRepository.findAllByRegionAndConditionFetch(region, condition)
                .stream()
                .map(TripListResponseDto::new)
                .collect(Collectors.toList());
    }

//    public List<TripListResponseDto> getTripListOrderByHeartDesc() {
//        return tripRepository.findAllOrderByHeartCountFetch()
//                .stream()
//                .map(TripListResponseDto::new)
//                .collect(Collectors.toList());
//    }
//
//    public List<TripListResponseDto> getTripListFindByRegionOrderByIdDesc(String region) {
//        return tripRepository.findAllByRegionAndConditionFetch(region)
//                .stream()
//                .map(TripListResponseDto::new)
//                .collect(Collectors.toList());
//    }
//
//    public List<TripListResponseDto> getTripListFindByRegionOrderByHeartDesc(String region) {
//        return tripRepository.findAllByRegionOrderByHeartCountFetch(region)
//                .stream()
//                .map(TripListResponseDto::new)
//                .collect(Collectors.toList());
//    }

    public TripDetailResponseDto getTripDetail(Long tripId) {
        Trip trip = Optional.ofNullable(tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException())).get();

        return new TripDetailResponseDto(trip);
    }

    @Transactional
    public TripDetailResponseDto save(TripSaveRequestDto tripSaveRequestDto, Member member, String imgPath) {
        if(member == null) {
            throw new MemberNotFoundException();
        }

        //여행지 저장 로직
        Trip trip = Trip.builder()
                .region(tripSaveRequestDto.getRegion())
                .title(tripSaveRequestDto.getTitle())
                .subTitle(tripSaveRequestDto.getSubTitle())
                .member(member)
                .build();

        Trip saveTrip = tripRepository.save(trip);

        //여행지 이미지 저장 로직
        TripImg tripImg = new TripImg(imgPath, trip);
        if(imgPath == null) {
            throw new ImgNotFoundException();
        }
        tripImgRepository.save(tripImg);
        trip.setTripImg(tripImg);
        return new TripDetailResponseDto(saveTrip);
    }

    @Transactional
    public void update(Long tripId, TripSaveRequestDto tripSaveRequestDto, Member member, String imgPath) {
        if(member == null) {
            throw new MemberNotFoundException();
        }

        Trip trip = Optional.ofNullable(tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new)).get();

        if(trip.getMember().getId() != member.getId()) {
            throw new ForbiddenException();
        }

        //여행지 이미지 수정 로직
        TripImg findTripImg = tripImgRepository.findTripImgByTrip(trip);
        String fileName = findTripImg.getImgUrl().split("/")[3];
        s3Service.deleteFile(fileName);
        tripImgRepository.delete(findTripImg);
        TripImg tripImg = new TripImg(imgPath, trip);
        if(imgPath == null) {
            throw new ImgNotFoundException();
        }
        tripImgRepository.save(tripImg);
        trip.setTripImg(tripImg);

        trip.update(tripSaveRequestDto.getRegion(), tripSaveRequestDto.getTitle(),
                tripSaveRequestDto.getSubTitle());
    }

    @Transactional
    public void delete(Long tripId, Member member) {
        if(member == null) {
            throw new ForbiddenException();
        }

        Trip trip = Optional.ofNullable(tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new)).get();

        if(trip.getMember().getId() != member.getId()) {
            throw new ForbiddenException();
        }

        TripImg findTripImg = tripImgRepository.findTripImgByTrip(trip);
        String fileName = findTripImg.getImgUrl().split("/")[3];
        s3Service.deleteFile(fileName);
        tripImgRepository.delete(findTripImg);

        tripRepository.delete(trip);
    }

    public List<TripMonthBestListResponseDto> getMonthBestTripBasic() {
        return tripRepository.findAllOrderByChangeHeartCountBasicFetch()
                .stream()
                .map(TripMonthBestListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void heart(Long tripId, Member member) {
        if(member == null) {
            throw new MemberNotFoundException();
        }

        Trip trip = Optional.ofNullable(tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException())).get();

        trip.plusChangeHeartCount();

        heartRepository.findByMemberAndTrip(member, trip).ifPresent(it -> {
            throw new AlreadyHeartException();
        });

        heartRepository.save(Heart.builder()
                .member(member)
                .trip(trip)
                .build());
    }

    @Transactional
    public void deleteHeart(Long tripId, Member member) {
        if(member == null) {
            throw new MemberNotFoundException();
        }

        Trip trip = Optional.ofNullable(tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException())).get();

        trip.minusChangeHeartCount();

        Heart heart = heartRepository.findByMemberAndTrip(member, trip)
                .orElseThrow(() -> new HeartNotFoundException());

        heartRepository.delete(heart);
    }

    public List<TripListResponseDto> getMyTripList(Member member) {
        return tripRepository.findAllByMemberFetch(member)
                .stream()
                .map(TripListResponseDto::new)
                .collect(Collectors.toList());
    }

    //@Scheduler에서 매달 1일마다 하트 변화량을 0으로 수정
    public void initTripChangeHeartCount() {
        List<Trip> tripList = tripRepository.findAll();
        for (Trip trip : tripList) {
            trip.initChangeHeartCount();
        }
    }
}
