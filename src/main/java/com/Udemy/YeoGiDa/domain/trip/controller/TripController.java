package com.Udemy.YeoGiDa.domain.trip.controller;

import com.Udemy.YeoGiDa.domain.common.service.S3Service;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.response.MemberDto;
import com.Udemy.YeoGiDa.domain.trip.exception.TripImgEssentialException;
import com.Udemy.YeoGiDa.domain.trip.request.TripSaveRequestDto;
import com.Udemy.YeoGiDa.domain.trip.response.TripDetailResponseDto;
import com.Udemy.YeoGiDa.domain.trip.response.TripListResponseDto;
import com.Udemy.YeoGiDa.domain.trip.service.TripService;
import com.Udemy.YeoGiDa.global.exception.ForbiddenException;
import com.Udemy.YeoGiDa.global.response.DefaultResult;
import com.Udemy.YeoGiDa.global.response.StatusCode;
import com.Udemy.YeoGiDa.global.security.annotation.LoginMember;
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
    @ApiResponse(code = 200, message = "여행지 목록 조회 성공 - 최신순")
    @GetMapping("/newest")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getTripListOrderByIdDesc() {
        List<TripListResponseDto> trips = tripService.getTripListOrderByIdDesc();
        Map<String, Object> result = new HashMap<>();
        result.put("memberList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 목록 조회 성공 - 최신순", result), HttpStatus.OK);
    }

    @ApiOperation("여행지 전체 조회 - 좋아요순")
    @ApiResponse(code = 200, message = "여행지 목록 조회 성공 - 좋아요순")
    @GetMapping("/heart")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getTripListOrderByHeartDesc() {
        List<TripListResponseDto> trips = tripService.getTripListOrderByHeartDesc();
        Map<String, Object> result = new HashMap<>();
        result.put("memberList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 목록 조회 성공 - 좋아요순", result), HttpStatus.OK);
    }

    @ApiOperation("여행지 전체 조회 - 지역별로 + 최신순")
    @ApiResponse(code = 200, message = "여행지 목록 조회 성공 - 지역별로 + 최신순")
    @GetMapping("/{region}/newest")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getTripListFindByRegionOrderByIdDesc(@PathVariable String region) {
        List<TripListResponseDto> trips = tripService.getTripListFindByRegionOrderByIdDesc(region);
        Map<String, Object> result = new HashMap<>();
        result.put("memberList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 목록 조회 성공 - 지역별로 + 최신순", result), HttpStatus.OK);
    }

    @ApiOperation("여행지 전체 조회 - 지역별로 + 하트순")
    @ApiResponse(code = 200, message = "여행지 목록 조회 성공 - 지역별로 + 하트순")
    @GetMapping("/{region}/heart")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getTripListFindByRegionOrderByHeartDesc(@PathVariable String region) {
        List<TripListResponseDto> trips = tripService.getTripListFindByRegionOrderByHeartDesc(region);
        Map<String, Object> result = new HashMap<>();
        result.put("memberList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 목록 조회 성공 - 지역별로 + 하트순 ", result), HttpStatus.OK);
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
        tripService.delete(tripId, member);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 삭제 성공"), HttpStatus.OK);
    }

    @ApiOperation("월간 베스트 여행지")
    @ApiResponse(code = 200, message = "목록 조회 성공 - 월간 베스트 여행지")
    @GetMapping("/monthly-best/basic")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getMonthBestTripBasic() {
        List<TripListResponseDto> trips = tripService.getMonthBestTripBasic();
        Map<String, Object> result = new HashMap<>();
        result.put("tripList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "목록 조회 성공 - 월간 베스트 여행지", result), HttpStatus.OK);
    }

    @ApiOperation("베스트 여행자")
    @ApiResponse(code = 200, message = "목록 조회 성공 - 베스트 여행자")
    @GetMapping("/best-travler/basic")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getBestTravler() {
        List<MemberDto> members = tripService.getBestTravlerBasic();
        Map<String, Object> result = new HashMap<>();
        result.put("memberList", members);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "목록 조회 성공 - 베스트 여행자", result), HttpStatus.OK);
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

    @ApiOperation("내가 작성한 여행지")
    @ApiResponse(code = 200, message = "여행지 목록 조회 성공 - 내가 작성한")
    @GetMapping("/my/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getMyTripList(@LoginMember Member member) {
        List<TripListResponseDto> trips = tripService.getMyTripList(member);
        Map<String, Object> result = new HashMap<>();
        result.put("memberList", trips);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 목록 조회 성공 - 내가 작성한 ", result), HttpStatus.OK);
    }
}
