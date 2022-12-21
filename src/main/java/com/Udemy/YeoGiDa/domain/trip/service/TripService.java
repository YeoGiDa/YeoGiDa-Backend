package com.Udemy.YeoGiDa.domain.trip.service;

import com.Udemy.YeoGiDa.domain.alarm.entity.Alarm;
import com.Udemy.YeoGiDa.domain.alarm.entity.AlarmType;
import com.Udemy.YeoGiDa.domain.alarm.exception.AlarmNotFoundException;
import com.Udemy.YeoGiDa.domain.alarm.repository.AlarmRepository;
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
import com.Udemy.YeoGiDa.domain.trip.exception.HeartTripNotFoundException;
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

import java.util.ArrayList;
import java.util.Collections;
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
    private final AlarmRepository alarmRepository;

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

    public List<TripListResponseDto> getTripListSearch(String keyword) {
        return tripRepository.findAllSearch(keyword)
                .stream()
                .map(TripListResponseDto::new)
                .collect(Collectors.toList());
    }

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

    public List<TripMonthBestListResponseDto> getMonthBestTripMore() {
        return tripRepository.findAllOrderByChangeHeartCountMoreFetch()
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
                .orElseThrow(TripNotFoundException::new)).get();

        trip.plusChangeHeartCount();
        trip.getMember().plusHeartCount();

        heartRepository.findByMemberAndTrip(member, trip).ifPresent(it -> {
            throw new AlreadyHeartException();
        });

        heartRepository.save(Heart.builder()
                .member(member)
                .trip(trip)
                .build());

        //알람 추가
        alarmRepository.save(Alarm.builder()
                .member(trip.getMember())
                .alarmType(AlarmType.NEW_HEART)
                .makeAlarmMemberId(member.getId())
                .placeId(null)
                .targetId(trip.getId())
                .build());
    }

    @Transactional
    public void deleteHeart(Long tripId, Member member) {
        if(member == null) {
            throw new MemberNotFoundException();
        }

        Trip trip = Optional.ofNullable(tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new)).get();

        trip.minusChangeHeartCount();
        trip.getMember().minusHeartCount();

        Heart heart = heartRepository.findByMemberAndTrip(member, trip)
                .orElseThrow(HeartNotFoundException::new);

        heartRepository.delete(heart);

        //알람 삭제
        Alarm findAlarm = alarmRepository.findHeartAlarmByMemberAndMakeMemberId(trip.getMember(), member.getId());
        if(findAlarm == null) {
            throw new AlarmNotFoundException();
        }
        alarmRepository.delete(findAlarm);
    }

    //내가 작성한 여행지
    public List<TripListResponseDto> getMyTripList(Member member) {
        return tripRepository.findAllByMemberFetch(member)
                .stream()
                .map(TripListResponseDto::new)
                .collect(Collectors.toList());
    }

    //내가 좋아요한 여행지
    public List<TripListResponseDto> getMyHeartTripList(Member member) {
        List<Heart> findHeart = heartRepository.findAllByMemberAndHeartFetch(member);
        if(findHeart.isEmpty()) {
            throw new HeartTripNotFoundException();
        }
        List<TripListResponseDto> tripListResponseDtos = new ArrayList<>();
        for (Heart heart : findHeart) {
            Long tripId = heart.getTrip().getId();
            Trip trip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);
            tripListResponseDtos.add(new TripListResponseDto(trip));
        }
        Collections.reverse(tripListResponseDtos);
        return tripListResponseDtos;
    }

    //@Scheduler에서 매달 1일마다 하트 변화량을 0으로 수정
    public void initTripChangeHeartCount() {
        List<Trip> tripList = tripRepository.findAll();
        for (Trip trip : tripList) {
            trip.initChangeHeartCount();
        }
    }

    public Optional<Trip> findById(Long tripId) {
        return tripRepository.findById(tripId);
    }
}
