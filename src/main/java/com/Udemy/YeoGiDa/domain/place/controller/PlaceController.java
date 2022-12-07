package com.Udemy.YeoGiDa.domain.place.controller;

import com.Udemy.YeoGiDa.domain.common.service.S3Service;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.place.repository.PlaceImgRepository;
import com.Udemy.YeoGiDa.domain.place.request.PlaceSaveRequestDto;
import com.Udemy.YeoGiDa.domain.place.response.PlaceDetailResponseDto;
import com.Udemy.YeoGiDa.domain.place.response.PlaceListResponseDto;
import com.Udemy.YeoGiDa.domain.place.service.PlaceService;

import com.Udemy.YeoGiDa.domain.trip.entity.Trip;

import com.Udemy.YeoGiDa.domain.trip.response.TripDetailResponseDto;
import com.Udemy.YeoGiDa.global.response.DefaultResult;
import com.Udemy.YeoGiDa.global.response.StatusCode;
import com.Udemy.YeoGiDa.global.security.annotation.LoginMember;
import io.swagger.annotations.*;

import io.swagger.annotations.ApiOperation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")

public class PlaceController {

    private final PlaceService placeService;
    private final PlaceImgRepository placeImgRepository;
    private final S3Service s3Service;


    @ApiOperation("여행지 별 장소 목록 조회 - 최신순")
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회 성공"),
            @ApiResponse(code = 404, message = "조회 실패(존재 하지 않는 여행지)")
    })
    @GetMapping("/{tripId}/places")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getPlaceListOrderById(@PathVariable Long tripId){
        List<PlaceListResponseDto> result = placeService.getPlaceListOrderById(tripId);

           return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "장소 목록 조회 성공 - 최신순", result), HttpStatus.OK);
    }



    @ApiOperation("여행지 별 장소 목록 조회 - 별점순")
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회 성공"),
            @ApiResponse(code = 404, message = "조회 실패(존재 하지 않는 여행지)")
    })
    @GetMapping("/{tripId}/places/star")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getPlaceListOrderByStar(@PathVariable Long tripId){
        List<PlaceListResponseDto> result = placeService.getPlaceListOrderByStar(tripId);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "장소 목록 조회 성공 - 별점순", result), HttpStatus.OK);
    }

    @ApiOperation("장소 상세 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "상세 조회 완료"),
            @ApiResponse(code = 404, message = "존재하지않는 장소")
    })
    @GetMapping("/places/{placeId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getPlaceDetail(@PathVariable Long placeId) {
        PlaceDetailResponseDto result = placeService.getPlaceDetail(placeId);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "장소 상세 조회 성공", result), HttpStatus.OK);
    }

    @ApiOperation("장소 작성")
    @PostMapping("/places/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity save(@ModelAttribute PlaceSaveRequestDto placeSaveRequestDto,
                               @RequestPart(name = "imgUrl") List<MultipartFile> multipartFiles,
                               Trip trip,
                               @LoginMember Member member) {
        if(multipartFiles == null) {
            throw new RuntimeException();
        }

        List<String> imgPaths = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String imgPath = s3Service.upload(multipartFile);
            imgPaths.add(imgPath);
        }

        PlaceDetailResponseDto result = placeService.save(placeSaveRequestDto, trip, imgPaths);
        return new ResponseEntity(DefaultResult.res(StatusCode.CREATED,
                "장소 작성 성공", result), HttpStatus.CREATED);
    }


    @ApiOperation("장소 수정")
    @PutMapping("/places/{placeId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@PathVariable Long placeId,
                                 @ModelAttribute PlaceSaveRequestDto placeSaveRequestDto,
                                 @RequestPart List<MultipartFile> multipartFiles,
                                 Trip trip,
                                 @LoginMember Member member) {
        if(multipartFiles == null) {
            throw new RuntimeException();
        }

        List<String> imgPaths = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String imgPath = s3Service.upload(multipartFile);
            imgPaths.add(imgPath);
        }

        //TODO: 수정해야함
        Long updateId = placeService.update(placeId, placeSaveRequestDto, trip, imgPaths);
        Map<String, Object> result = new HashMap<>();
        result.put("updateId", updateId);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 수정 성공", result), HttpStatus.OK);
    }

    @ApiOperation("장소 삭제")
    @DeleteMapping("/places/{placeId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@PathVariable Long placeId,
                                 Trip trip,
                                 @LoginMember Member member) {
        Long deleteId = placeService.delete(placeId, trip);
        Map<String, Object> result = new HashMap<>();
        result.put("deleteId", deleteId);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "장소 삭제 성공", result), HttpStatus.OK);
    }



}
