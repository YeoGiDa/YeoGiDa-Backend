package com.Udemy.YeoGiDa.domain.place.service;



import com.Udemy.YeoGiDa.domain.common.exception.ImgNotFoundException;
import com.Udemy.YeoGiDa.domain.common.service.S3Service;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.entity.PlaceImg;
import com.Udemy.YeoGiDa.domain.place.exception.PlaceNotFoundException;
import com.Udemy.YeoGiDa.domain.place.repository.PlaceImgRepository;
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
import org.springframework.security.core.parameters.P;
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
    private final PlaceImgRepository placeImgRepository;
    private final S3Service s3Service;
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
    public PlaceDetailResponseDto getPlaceDetail(Long placeId) {
        Place place = Optional.ofNullable(placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException())).get();

        return new PlaceDetailResponseDto(place);
    }

    public PlaceDetailResponseDto save(PlaceSaveRequestDto placeSaveRequestDto,
                                       Long tripId, Member member, List<String> imgPaths) {

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

        String defaultImgPath = "";
        //장소 이미지 저장 로직
        if(imgPaths == null) {
            defaultImgPath = "https://s3.ap-northeast-2.amazonaws.com/yeogida-bucket/image/default_place_img.png";
            PlaceImg placeImg = new PlaceImg(defaultImgPath, place);
            placeImgRepository.save(placeImg);
        }
        else {
            for (String imgPath : imgPaths) {
                PlaceImg placeImg = new PlaceImg(imgPath, place);
                placeImgRepository.save(placeImg);
            }
        }

        return new PlaceDetailResponseDto(savePlace);
    }

    public void update(PlaceUpdateRequestDto placeUpdateRequestDto,
                       Long placeId, Member member, List<String> imgPaths) {

        Place place = Optional.ofNullable(placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException())).get();

        if(member == null){
            throw new MemberNotFoundException();
        }

        if(place.getTrip().getMember() != member){
            throw new ForbiddenException();
        }

        //여행지 이미지 수정 로직
        List<PlaceImg> findPlaceImgs = placeImgRepository.findPlaceImgsByPlace(place);
        //default_image일때
        String s3FileName = findPlaceImgs.get(0).getImgUrl().split("/")[3];
        if((findPlaceImgs.size() == 1) && (s3FileName.equals("default_place.png"))) {
            placeImgRepository.delete(findPlaceImgs.get(0));
            if(imgPaths == null) {
                throw new ImgNotFoundException();
            }
        }
        else {
            for (PlaceImg findPlaceImg : findPlaceImgs) {
                String fileName = findPlaceImg.getImgUrl().split("/")[3];
                s3Service.deleteFile(fileName);
                placeImgRepository.delete(findPlaceImg);
            }
        }

        //수정된 사진이 default일 때
        if(imgPaths == null) {
            String imgPath = "https://s3.ap-northeast-2.amazonaws.com/yeogida-bucket/image/default_place_img.png";
            PlaceImg placeImg = new PlaceImg(imgPath, place);
            placeImgRepository.save(placeImg);
        }
        else {
            for (String imgPath : imgPaths) {
                PlaceImg placeImg = new PlaceImg(imgPath, place);
                placeImgRepository.save(placeImg);
            }
        }

        place.update(placeUpdateRequestDto.getTitle(), placeUpdateRequestDto.getAddress(),
                placeUpdateRequestDto.getContent(),placeUpdateRequestDto.getLongitude(),
                placeUpdateRequestDto.getLatitude(), placeUpdateRequestDto.getStar(),
                placeUpdateRequestDto.getTag());
    }

    public void delete(Long placeId, Member member) {

        Place place = Optional.ofNullable(placeRepository.findById(placeId)
                .orElseThrow(PlaceNotFoundException::new)).get();

        if(member == null){
            throw new MemberNotFoundException();
        }

        if(place.getTrip().getMember() != member){
            throw new ForbiddenException();
        }

        List<PlaceImg> findPlaceImgs = placeImgRepository.findPlaceImgsByPlace(place);
        //default 이미지 한장일때
        String s3FileName = findPlaceImgs.get(0).getImgUrl().split("/")[3];
        if((findPlaceImgs.size() == 1) && (s3FileName.equals("default_place.png"))) {
            placeImgRepository.delete(findPlaceImgs.get(0));
        }
        else {
            for (PlaceImg findPlaceImg : findPlaceImgs) {
                String fileName = findPlaceImg.getImgUrl().split("/")[3];
                s3Service.deleteFile(fileName);
                placeImgRepository.delete(findPlaceImg);
            }
        }

        placeRepository.delete(place);
    }
}