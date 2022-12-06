package com.Udemy.YeoGiDa.domain.place.controller;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.place.request.PlaceSaveRequestDto;
import com.Udemy.YeoGiDa.domain.place.response.PlaceDetailResponseDto;
import com.Udemy.YeoGiDa.domain.place.response.PlaceListResponseDto;
import com.Udemy.YeoGiDa.domain.place.service.PlaceService;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.Udemy.YeoGiDa.domain.trip.request.TripSaveRequestDto;
import com.Udemy.YeoGiDa.domain.trip.response.TripDetailResponseDto;
import com.Udemy.YeoGiDa.global.response.DefaultResult;
import com.Udemy.YeoGiDa.global.response.StatusCode;
import com.Udemy.YeoGiDa.global.security.annotation.LoginMember;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trips")
public class PlaceController {

    private final PlaceService placeService;

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
    @GetMapping("/{tripId}/places/{placeId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getPlaceDetail(@PathVariable Long placeId) {
        PlaceDetailResponseDto result = placeService.getPlaceDetail(placeId);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 상세 조회 성공", result), HttpStatus.OK);
    }

    @ApiOperation("장소 작성")
    @PostMapping("{tripId}/places/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity save(@RequestBody PlaceSaveRequestDto placeSaveRequestDto,
                               Trip trip) {
        PlaceDetailResponseDto result = placeService.save(placeSaveRequestDto, trip);
        return new ResponseEntity(DefaultResult.res(StatusCode.CREATED,
                "장소 작성 성공", result), HttpStatus.CREATED);
    }


    @ApiOperation("장소 수정")
    @PutMapping("/{tripId}/places/{placeId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@PathVariable Long placeId,
                                 @RequestBody PlaceSaveRequestDto placeSaveRequestDto,
                                 Trip trip) {
        Long updateId = placeService.update(placeId, placeSaveRequestDto, trip);
        Map<String, Object> result = new HashMap<>();
        result.put("updateId", updateId);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 수정 성공", result), HttpStatus.OK);
    }

    @ApiOperation("여행지 삭제")
    @DeleteMapping("/{tripId}/places/{placeId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@PathVariable Long placeId,
                                 Trip trip) {
        Long deleteId = placeService.delete(placeId, trip);
        Map<String, Object> result = new HashMap<>();
        result.put("deleteId", deleteId);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "장소 삭제 성공", result), HttpStatus.OK);
    }



}
