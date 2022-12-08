package com.Udemy.YeoGiDa.domain.place.controller;

import com.Udemy.YeoGiDa.domain.common.service.S3Service;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.place.request.PlaceSaveRequestDto;
import com.Udemy.YeoGiDa.domain.place.request.PlaceUpdateRequestDto;
import com.Udemy.YeoGiDa.domain.place.response.PlaceDetailResponseDto;
import com.Udemy.YeoGiDa.domain.place.response.PlaceListResponseDto;
import com.Udemy.YeoGiDa.domain.place.service.PlaceService;

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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")

public class PlaceController {

    private final PlaceService placeService;
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
    @PostMapping("/{tripId}/places/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity save(@RequestBody PlaceSaveRequestDto placeSaveRequestDto,
                               @PathVariable Long tripId,
                               @RequestPart(name = "imgUrls", required = false) List<MultipartFile> multipartFiles,
                               @LoginMember Member member) {

        ArrayList<String> imgPaths = new ArrayList<>();
        if(multipartFiles == null) {
            imgPaths = null;
        }
        else {
            for (MultipartFile multipartFile : multipartFiles) {
                imgPaths.add(s3Service.upload(multipartFile));
            }
        }

        PlaceDetailResponseDto result = placeService.save(placeSaveRequestDto,tripId, member, imgPaths);
        return new ResponseEntity(DefaultResult.res(StatusCode.CREATED,
                "장소 작성 성공", result), HttpStatus.CREATED);
    }


    @ApiOperation("장소 수정")
    @PutMapping("/places/{placeId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@ModelAttribute PlaceUpdateRequestDto placeUpdateRequestDto,
                                 @PathVariable Long placeId,
                                 @RequestPart(name = "imgUrls", required = false) List<MultipartFile> multipartFiles,
                                 @LoginMember Member member) {
        ArrayList<String> imgPaths = new ArrayList<>();
        if(multipartFiles == null) {
            imgPaths = null;
        }
        else {
            for (MultipartFile multipartFile : multipartFiles) {
                imgPaths.add(s3Service.upload(multipartFile));
            }
        }
        placeService.update(placeUpdateRequestDto, placeId, member, imgPaths);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 수정 성공"), HttpStatus.OK);
    }

    @ApiOperation("장소 삭제")
    @DeleteMapping("/places/{placeId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@PathVariable Long placeId,
                                 @LoginMember Member member) {
        placeService.delete(placeId, member);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "장소 삭제 성공"), HttpStatus.OK);
    }
}
