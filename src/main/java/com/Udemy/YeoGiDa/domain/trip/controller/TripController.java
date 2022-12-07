package com.Udemy.YeoGiDa.domain.trip.controller;

import com.Udemy.YeoGiDa.domain.common.service.S3Service;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.Udemy.YeoGiDa.domain.trip.request.TripSaveRequestDto;
import com.Udemy.YeoGiDa.domain.trip.response.TripDetailResponseDto;
import com.Udemy.YeoGiDa.domain.trip.response.TripListResponseDto;
import com.Udemy.YeoGiDa.domain.trip.service.TripService;
import com.Udemy.YeoGiDa.global.response.DefaultResult;
import com.Udemy.YeoGiDa.global.response.StatusCode;
import com.Udemy.YeoGiDa.global.security.annotation.LoginMember;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trips")
public class TripController {

    private final TripService tripService;
    private final S3Service s3Service;

    @ApiOperation("여행지 전체 조회 - 최신순")
    @ApiResponse(code = 200, message = "조회 완료")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getTripList() {
        List<TripListResponseDto> result = tripService.getTripList();
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 목록 조회 성공", result), HttpStatus.OK);
    }

    @ApiOperation("여행지 상세 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "상세 조회 완료"),
            @ApiResponse(code = 404, message = "존재하지않는 여행지")
    })
    @GetMapping("/{tripId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getTripDetail(@PathVariable Long tripId) {
        TripDetailResponseDto result = tripService.getTripDetail(tripId);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 상세 조회 성공", result), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @ApiOperation("여행지 작성")
    @ApiResponses({
            @ApiResponse(code = 201, message = "작성 성공"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "존재하지않는 회원")
    })
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
//    @ApiImplicitParam(name = "Authorization", value = "사용자 인증을 위한 accessToken", paramType = "header", required = true, dataTypeClass = String.class)
    public ResponseEntity save(@ModelAttribute TripSaveRequestDto tripSaveRequestDto,
                               @RequestPart(name = "imgUrl") MultipartFile multipartFile,
                               @LoginMember Member member) {
        if(multipartFile == null) {
            throw new RuntimeException();
        }

        String imgPath = s3Service.upload(multipartFile);
        TripDetailResponseDto result = tripService.save(tripSaveRequestDto, member, imgPath);
        return new ResponseEntity(DefaultResult.res(StatusCode.CREATED,
                "여행지 작성 성공", result), HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @ApiOperation("여행지 수정")
    @ApiResponses({
            @ApiResponse(code = 200, message = "수정 성공"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "수정 실패" +
                    "존재하지않는 회원 - Member Not Found," +
                    "존재하지않는 여행지 - Trip Not Found")
    })
    @PutMapping("/{tripId}")
    @ResponseStatus(HttpStatus.OK)
//    @ApiImplicitParam(name = "Authorization", value = "사용자 인증을 위한 accessToken", paramType = "header", required = true, dataTypeClass = String.class)
    public ResponseEntity update(@PathVariable Long tripId,
                                 @ModelAttribute TripSaveRequestDto tripSaveRequestDto,
                                 @RequestPart(name = "imgUrl") MultipartFile multipartFile,
                                 @LoginMember Member member) {
        Trip findTrip = tripService.findById(tripId);
        s3Service.deleteFile(findTrip.getTripImg().getImgUrl());
        if(multipartFile == null) {
            throw new RuntimeException();
        }
        String imgPath = s3Service.upload(multipartFile);
        Long updateId = tripService.update(tripId, tripSaveRequestDto, member, imgPath);
        Map<String, Object> result = new HashMap<>();
        result.put("updateId", updateId);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 수정 성공", result), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @ApiOperation("여행지 삭제")
    @ApiResponses({
            @ApiResponse(code = 200, message = "삭제 성공"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "수정 실패" +
                    "존재하지않는 회원 - Member Not Found," +
                    "존재하지않는 여행지 - Trip Not Found")
    })
    @DeleteMapping("/{tripId}")
    @ResponseStatus(HttpStatus.OK)
//    @ApiImplicitParam(name = "Authorization", value = "사용자 인증을 위한 accessToken", paramType = "header", required = true, dataTypeClass = String.class)
    public ResponseEntity delete(@PathVariable Long tripId,
                                 @LoginMember Member member) {
        Trip findTrip = tripService.findById(tripId);
        s3Service.deleteFile(findTrip.getTripImg().getImgUrl());
        Long deleteId = tripService.delete(tripId, member);
        Map<String, Object> result = new HashMap<>();
        result.put("deleteId", deleteId);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 삭제 성공", result), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @ApiOperation("여행지 좋아요 누르기")
    @ApiResponses({
            @ApiResponse(code = 200, message = "수정 성공"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "수정 실패" +
                    "존재하지않는 회원 - Member Not Found," +
                    "존재하지않는 여행지 - Trip Not Found")
    })
    @PostMapping("/{tripId}/heart")
    @ResponseStatus(HttpStatus.CREATED)
//    @ApiImplicitParam(name = "Authorization", value = "사용자 인증을 위한 accessToken", paramType = "header", required = true, dataTypeClass = String.class)
    public ResponseEntity heart(@PathVariable Long tripId,
                               @LoginMember Member member) {
        tripService.heart(tripId, member);
        return new ResponseEntity(DefaultResult.res(StatusCode.CREATED,
                "여행지 좋아요 성공"), HttpStatus.CREATED);
    }

    @ApiOperation("여행지 좋아요 개수 (ADMIN용)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회 성공"),
            @ApiResponse(code = 404, message = "존재하지않는 여행지")
    })
    @GetMapping("/{tripId}/heartCount")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity countHeart(@PathVariable Long tripId) {
        int heartCount = tripService.countHeart(tripId);
        Map<String, Object> result = new HashMap<>();
        result.put("heartCount", heartCount);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 좋아요 개수 세기 성공", result), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @ApiOperation("여행지 좋아요 취소하기")
    @ApiResponses({
            @ApiResponse(code = 200, message = "수정 성공"),
            @ApiResponse(code = 403, message = "권한 없음"),
            @ApiResponse(code = 404, message = "수정 실패" +
                    "존재하지않는 회원 - Member Not Found," +
                    "존재하지않는 여행지 - Trip Not Found")
    })
    @DeleteMapping("/{tripId}/heart")
    @ResponseStatus(HttpStatus.OK)
//    @ApiImplicitParam(name = "Authorization", value = "사용자 인증을 위한 accessToken", paramType = "header", required = true, dataTypeClass = String.class)
    public ResponseEntity deleteHeart(@PathVariable Long tripId,
                                @LoginMember Member member) {
        tripService.deleteHeart(tripId, member);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 좋아요 취소 성공"), HttpStatus.OK);
    }
}
