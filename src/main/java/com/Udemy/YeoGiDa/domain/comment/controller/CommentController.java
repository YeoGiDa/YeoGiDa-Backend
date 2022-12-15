package com.Udemy.YeoGiDa.domain.comment.controller;

import com.Udemy.YeoGiDa.domain.comment.request.CommentSaveRequestDto;
import com.Udemy.YeoGiDa.domain.comment.response.CommentListResponseDto;
import com.Udemy.YeoGiDa.domain.comment.service.CommentService;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.request.PlaceSaveRequestDto;
import com.Udemy.YeoGiDa.domain.place.response.PlaceDetailResponseDto;
import com.Udemy.YeoGiDa.domain.place.response.PlaceListResponseDto;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.Udemy.YeoGiDa.global.response.DefaultResult;
import com.Udemy.YeoGiDa.global.response.StatusCode;
import com.Udemy.YeoGiDa.global.security.annotation.LoginMember;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    @ApiOperation("댓글 목록 조회 - 최신순")
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회 성공"),
            @ApiResponse(code = 404, message = "조회 실패(존재 하지 않는 여행지)")
    })
    @GetMapping("/{placeId}/comments/idDesc")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getCommentListOrderByIdDesc(@PathVariable Long placeId){
        List<CommentListResponseDto> comments = commentService.getCommentListByDesc(placeId);
        Map<String, Object> result = new HashMap<>();
        result.put("commentCounts",comments.size());
        result.put("commentList", comments);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "댓글 목록 조회 성공 - 최신순", result), HttpStatus.OK);
    }


    @ApiOperation("댓글 목록 조회 - 작성순")
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회 성공"),
            @ApiResponse(code = 404, message = "조회 실패(존재 하지 않는 여행지)")
    })
    @GetMapping("/{placeId}/comments/idAsc")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getCommentListOrderByIdAsc(@PathVariable Long placeId){
        List<CommentListResponseDto> comments = commentService.getCommentListByAsc(placeId);
        Map<String, Object> result = new HashMap<>();
        result.put("commentCounts",comments.size());
        result.put("commentList", comments);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "댓글 목록 조회 성공 - 작성순", result), HttpStatus.OK);
    }

    @ApiOperation("댓글 작성")
    @PostMapping("/{placeId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity save(@PathVariable Long placeId,
                               @RequestBody CommentSaveRequestDto commentSaveRequestDto,
                               @LoginMember Member member) {
        CommentListResponseDto commentListResponseDto = commentService.save(commentSaveRequestDto, placeId, member);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("nickname", commentListResponseDto.getNickName());
        result.put("createdTime", commentListResponseDto.getCreatedTime());
        result.put("content", commentListResponseDto.getContent());
        return new ResponseEntity(DefaultResult.res(StatusCode.CREATED,
                "댓글 작성 성공",commentListResponseDto), HttpStatus.CREATED);
    }

    @ApiOperation("댓글 삭제")
    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@PathVariable Long commentId,
                                 @LoginMember Member member) {
        commentService.delete(commentId, member);

        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "댓글 삭제 성공"), HttpStatus.OK);
    }
}
