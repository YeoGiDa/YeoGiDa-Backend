package com.Udemy.YeoGiDa.domain.trip.controller;

import com.Udemy.YeoGiDa.domain.common.service.S3Service;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.trip.exception.TripImgEssentialException;
import com.Udemy.YeoGiDa.domain.trip.request.TripSaveRequestDto;
import com.Udemy.YeoGiDa.domain.trip.response.TripDetailResponseDto;
import com.Udemy.YeoGiDa.domain.trip.response.TripListResponseDto;
import com.Udemy.YeoGiDa.domain.trip.response.TripMonthBestListResponseDto;
import com.Udemy.YeoGiDa.domain.trip.service.TripService;
import com.Udemy.YeoGiDa.global.response.DefaultResult;
import com.Udemy.YeoGiDa.global.response.StatusCode;
import com.Udemy.YeoGiDa.global.security.annotation.LoginMember;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trips")
public class TripController {

    private final TripService tripService;
    private final S3Service s3Service;

    @ApiOperation("여행지 전체 조회 - 최신순 & 좋아요순")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getTripList(@RequestParam String condition) {
        List<TripListResponseDto> trips = tripService.getTripList(condition);
        Map<String, Object> result = new HashMap<>();
        result.put("tripList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                String.format("여행지 목록 조회 성공 - " + condition), result), HttpStatus.OK);
    }

    @ApiOperation("여행지 지역별 전체 조회 - 최신순 & 좋아요순")
    @GetMapping("/region/{region}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getTripListRegion(@PathVariable String region,
                                            @RequestParam String condition) {
        List<TripListResponseDto> trips = tripService.getTripListByRegion(region, condition);
        Map<String, Object> result = new HashMap<>();
        result.put("tripList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                String.format("여행지 목록 조회 성공 - " + region + " + " + condition), result), HttpStatus.OK);
    }

    @ApiOperation("여행지 상세 조회")
    @GetMapping("/{tripId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getTripDetail(@PathVariable Long tripId) {
        TripDetailResponseDto result = tripService.getTripDetail(tripId);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 상세 조회 성공", result), HttpStatus.OK);
    }

    @ApiOperation("여행지 작성")
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity save(@ModelAttribute TripSaveRequestDto tripSaveRequestDto,
                               @RequestPart(name = "imgUrl", required = false) MultipartFile multipartFile,
                               @LoginMember Member member) throws TripImgEssentialException {
        if(multipartFile.isEmpty()) {
            throw new TripImgEssentialException();
        }

        String imgPath = s3Service.upload(multipartFile);
        TripDetailResponseDto result = tripService.save(tripSaveRequestDto, member, imgPath);
        return new ResponseEntity(DefaultResult.res(StatusCode.CREATED,
                "여행지 작성 성공", result), HttpStatus.CREATED);
    }

    @ApiOperation("여행지 수정")
    @PutMapping("/{tripId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@PathVariable Long tripId,
                                 @ModelAttribute TripSaveRequestDto tripSaveRequestDto,
                                 @RequestPart(name = "imgUrl", required = true) MultipartFile multipartFile,
                                 @LoginMember Member member) throws TripImgEssentialException {
        if(multipartFile.isEmpty()) {
            throw new TripImgEssentialException();
        }
        String imgPath = s3Service.upload(multipartFile);
        tripService.update(tripId, tripSaveRequestDto, member, imgPath);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 수정 성공"), HttpStatus.OK);
    }

    @ApiOperation("여행지 삭제")
    @DeleteMapping("/{tripId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@PathVariable Long tripId,
                                 @LoginMember Member member) {
        tripService.delete(tripId, member);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 삭제 성공"), HttpStatus.OK);
    }

    @ApiOperation("월간 베스트 여행지 - 기본 10개")
    @GetMapping("/monthly-best/basic")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getMonthBestTripBasic() {
        List<TripMonthBestListResponseDto> trips = tripService.getMonthBestTripBasic();
        Map<String, Object> result = new HashMap<>();
        result.put("tripList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "목록 조회 성공 - 월간 베스트 여행지(기본 10개)", result), HttpStatus.OK);
    }

    @ApiOperation("월간 베스트 여행지 - 전부")
    @GetMapping("/monthly-best/more")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getMonthBestTripMore() {
        List<TripMonthBestListResponseDto> trips = tripService.getMonthBestTripMore();
        Map<String, Object> result = new HashMap<>();
        result.put("tripList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "목록 조회 성공 - 월간 베스트 여행지(전부)", result), HttpStatus.OK);
    }

    @ApiOperation("여행지 좋아요 누르기")
    @PostMapping("/{tripId}/heart")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity heart(@PathVariable Long tripId,
                               @LoginMember Member member) throws IOException {
        tripService.heart(tripId, member);
        return new ResponseEntity(DefaultResult.res(StatusCode.CREATED,
                "여행지 좋아요 성공"), HttpStatus.CREATED);
    }

    @ApiOperation("여행지 좋아요 취소하기")
    @DeleteMapping("/{tripId}/heart")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deleteHeart(@PathVariable Long tripId,
                                @LoginMember Member member) {
        tripService.deleteHeart(tripId, member);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 좋아요 취소 성공"), HttpStatus.OK);
    }

    @ApiOperation("내가 작성한 여행지")
    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getMyTripList(@LoginMember Member member) {
        List<TripListResponseDto> trips = tripService.getMyTripList(member);
        Map<String, Object> result = new HashMap<>();
        result.put("tripList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 목록 조회 성공 - 내가 작성한 ", result), HttpStatus.OK);
    }

    @ApiOperation("내가 좋아요한 여행지")
    @GetMapping("/my/heart")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getMyHeartTripList(@LoginMember Member member) {
        List<TripListResponseDto> trips = tripService.getMyHeartTripList(member);
        Map<String, Object> result = new HashMap<>();
        result.put("tripList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 목록 조회 성공 - 내가 좋아요한 ", result), HttpStatus.OK);
    }

    @ApiOperation("여행지 검색")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getTripListSearch(@RequestParam("keyword") String keyword,
                                            @LoginMember Member member) {
        List<TripListResponseDto> trips = tripService.getTripListSearch(keyword);
        Map<String, Object> result = new HashMap<>();
        result.put("tripList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 목록 검색 성공 ", result), HttpStatus.OK);
    }

    @ApiOperation("좋아요한 여행지 지역별 필터")
    @GetMapping("/my/heart/{region}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getMyHeartTripFilterList(@PathVariable String region,
                                                   @LoginMember Member member) {
        List<TripListResponseDto> trips = tripService.getMyHeartTripFilterList(member, region);
        Map<String, Object> result = new HashMap<>();
        result.put("tripList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "좋아요 여행지 필터 정렬 ", result), HttpStatus.OK);
    }

    @ApiOperation("좋아요한 여행지 검색")
    @GetMapping("/my/heart/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getMyHeartTripSearchList(@RequestParam("keyword") String keyword,
                                                 @LoginMember Member member) {
        List<TripListResponseDto> trips = tripService.getMyHeartTripSearchList(member,keyword);
        Map<String, Object> result = new HashMap<>();
        result.put("tripList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "좋아요 여행지 목록 검색 성공 ", result), HttpStatus.OK);
    }

    @ApiOperation("팔로잉의 최근 여행지 - 기본 10개")
    @GetMapping("/follow/basic")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getFollowingTripListBasic(@LoginMember Member member) {
        List<TripListResponseDto> trips = tripService.getFollowingsTripListBasic(member);
        Map<String, Object> result = new HashMap<>();
        result.put("tripList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로잉이 최근 올린 여행지 - 기본 10개 ", result), HttpStatus.OK);
    }

    @ApiOperation("팔로잉의 최근 여행지 - 전체")
    @GetMapping("/follow/more")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getFollowingsTripListMore(@LoginMember Member member) {
        List<TripListResponseDto> trips = tripService.getFollwingsTripListMore(member);
        Map<String, Object> result = new HashMap<>();
        result.put("tripList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로잉이 최근 올린 여행지 - 전체 ", result), HttpStatus.OK);
    }

}
