package com.Udemy.YeoGiDa.domain.place.controller;

import com.Udemy.YeoGiDa.domain.common.service.S3Service;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.place.request.PlaceSaveRequestDto;
import com.Udemy.YeoGiDa.domain.place.request.PlaceUpdateRequestDto;
import com.Udemy.YeoGiDa.domain.place.response.PlaceDetailResponseDto;
import com.Udemy.YeoGiDa.domain.place.response.PlaceListInTripResponseDto;
import com.Udemy.YeoGiDa.domain.place.response.PlaceListResponseDto;
import com.Udemy.YeoGiDa.domain.place.service.PlaceService;

import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.Udemy.YeoGiDa.domain.trip.service.TripService;
import com.Udemy.YeoGiDa.global.response.DefaultResult;
import com.Udemy.YeoGiDa.global.response.StatusCode;
import com.Udemy.YeoGiDa.global.security.annotation.LoginMember;
import io.swagger.annotations.*;

import io.swagger.annotations.ApiOperation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class PlaceController {

    private final PlaceService placeService;
    private final TripService tripService;
    private final S3Service s3Service;

//    @GetMapping("/{tripId}/places")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity getPlaceList(@PathVariable Long tripId,
//                                       @RequestParam("condition") String condition){
//        List<PlaceListResponseDto> places = placeService.getPlaceList(tripId,condition);
//        Optional<Trip> trip = tripService.findById(tripId);
//        Map<String, Object> result = new LinkedHashMap<>();
//        result.put("memberId", trip.get().getMember().getId());
//        result.put("placeList", places);
//        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
//                "장소 목록 조회 성공 "+condition, result), HttpStatus.OK);
//    }


    @GetMapping("/{tripId}/places/{tag}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getPlaceListByTagTest(@PathVariable Long tripId,
                                                @PathVariable String tag,
                                                @RequestParam("condition") String condition){
        List<PlaceListResponseDto> places = placeService.getPlaceListByTagDefault(tripId,tag,condition);
        Optional<Trip> trip = tripService.findById(tripId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("memberId", trip.get().getMember().getId());
        result.put("placeList", places);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                String.format("장소 목록 조회 성공 " + condition), result), HttpStatus.OK);
    }

    @GetMapping("/{tripId}/places/tripInfo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getTripInfoInPlaceList(@PathVariable Long tripId){
        PlaceListInTripResponseDto result = placeService.getTripDataInPlaceList(tripId);


        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 정보 조회 성공 ", result), HttpStatus.OK);
    }


    @GetMapping("/places/commented")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getPlaceListByComment(@LoginMember Member member){
        List<PlaceListResponseDto> places = placeService.getPlaceByComment(member.getId());
        Map<String, Object> result = new HashMap<>();
        result.put("placeList", places);

        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "내가 댓글 단 장소 목록 조회 성공", result), HttpStatus.OK);
    }
//
//    @ApiOperation("여행지 별 장소 목록 조회 - 별점순")
//    @GetMapping("/{tripId}/places/star")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity getPlaceListOrderByStar(@PathVariable Long tripId){
//        List<PlaceListResponseDto> places = placeService.getPlaceListOrderByStar(tripId);
//        Map<String, Object> result = new HashMap<>();
//        result.put("placeList", places);
//        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
//                "장소 목록 조회 성공 - 별점순", result), HttpStatus.OK);
//    }
//
//    @GetMapping("/{tripId}/places/comments")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity getPlaceListOrderByComments(@PathVariable Long tripId){
//        List<PlaceListResponseDto> places = placeService.getPlaceListByComments(tripId);
//        Map<String, Object> result = new HashMap<>();
//        result.put("placeList", places);
//        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
//                "장소 목록 조회 성공 - 댓글순", result), HttpStatus.OK);
//    }

//    @ApiOperation("여행지 별 장소 목록 조회 - 키워드")
//    @GetMapping("/{tripId}/places/{tag}")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity getPlaceListByTag(@PathVariable Long tripId,
//                                            @PathVariable String tag){
//        List<PlaceListResponseDto> places = placeService.getPlaceListByTagAsc(tripId,tag);
//        Map<String, Object> result = new HashMap<>();
//        result.put("placeList", places);
//        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
//                "장소 목록 조회 성공 - 키워드", result), HttpStatus.OK);
//    }
//
//    @ApiOperation("여행지 별 장소 목록 조회 - 키워드&최신순")
//    @GetMapping("/{tripId}/places/{tag}/latest")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity getPlaceListByTagOrderById(@PathVariable Long tripId,
//                                                       @PathVariable String tag){
//        List<PlaceListResponseDto> places = placeService.getPlaceListByTagDesc(tripId,tag);
//        Map<String, Object> result = new HashMap<>();
//        result.put("placeList", places);
//        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
//                "장소 목록 조회 성공 - 키워드&최신순", result), HttpStatus.OK);
//    }
//
//    @ApiOperation("여행지 별 장소 목록 조회 - 키워드&별점순")
//    @GetMapping("/{tripId}/places/{tag}/star")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity getPlaceListByTagOrderByStar(@PathVariable Long tripId,
//                                                       @PathVariable String tag){
//        List<PlaceListResponseDto> places = placeService.getPlaceListByTagStar(tripId,tag);
//        Map<String, Object> result = new HashMap<>();
//        result.put("placeList", places);
//        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
//                "장소 목록 조회 성공 - 키워드&별점", result), HttpStatus.OK);
//    }
//
//    @ApiOperation("여행지 별 장소 목록 조회 - 키워드&댓글순")
//    @GetMapping("/{tripId}/places/{tag}/comments")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity getPlaceListByTagOrderByComment(@PathVariable Long tripId,
//                                                          @PathVariable String tag){
//        List<PlaceListResponseDto> places = placeService.getPlaceListByTagComment(tripId,tag);
//        Map<String, Object> result = new HashMap<>();
//        result.put("placeList", places);
//        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
//                "장소 목록 조회 성공 - 키워드&댓글순", result), HttpStatus.OK);
//    }

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
    public ResponseEntity save(PlaceSaveRequestDto placeSaveRequestDto,
                               @PathVariable Long tripId,
                               @RequestPart(name = "imgUrls", required = false) List<MultipartFile> multipartFiles,
                               @LoginMember Member member) {

        PlaceDetailResponseDto result;
        if(multipartFiles.size() == 1 && multipartFiles.get(0).isEmpty()) {
            result = placeService.saveNoPicture(placeSaveRequestDto, tripId, member);
        }
        else {
            List<String> imgPaths = s3Service.upload(multipartFiles);
            result = placeService.saveWithPictures(placeSaveRequestDto,tripId, member, imgPaths);
        }

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
        List<String> imgPaths = new ArrayList<>();
        if(multipartFiles.size() == 1 && multipartFiles.get(0).isEmpty()) {
            imgPaths = null;
        }
        else {
            imgPaths = s3Service.upload(multipartFiles);
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
