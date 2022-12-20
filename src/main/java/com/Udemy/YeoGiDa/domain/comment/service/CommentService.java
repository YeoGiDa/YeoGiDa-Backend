
package com.Udemy.YeoGiDa.domain.comment.service;

import com.Udemy.YeoGiDa.domain.alarm.entity.Alarm;
import com.Udemy.YeoGiDa.domain.alarm.entity.AlarmType;
import com.Udemy.YeoGiDa.domain.alarm.exception.AlarmNotFoundException;
import com.Udemy.YeoGiDa.domain.alarm.repository.AlarmRepository;
import com.Udemy.YeoGiDa.domain.comment.entity.Comment;
import com.Udemy.YeoGiDa.domain.comment.exception.CommentNotFoundException;
import com.Udemy.YeoGiDa.domain.comment.repository.CommentRepository;
import com.Udemy.YeoGiDa.domain.comment.request.CommentSaveRequestDto;
import com.Udemy.YeoGiDa.domain.comment.response.CommentListResponseDto;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.exception.PlaceNotFoundException;
import com.Udemy.YeoGiDa.domain.place.repository.PlaceRepository;
import com.Udemy.YeoGiDa.global.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService {

    private final PlaceRepository placeRepository;
    private final CommentRepository commentRepository;
    private final AlarmRepository alarmRepository;

    @Transactional(readOnly = true)
    public List<CommentListResponseDto> getCommentListByDesc(Long placeId){

        return commentRepository.findAllByPlaceByIdDesc(placeId)
                .stream()
                .map(CommentListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentListResponseDto> getCommentListByAsc(Long placeId){

        return commentRepository.findAllByPlaceByIdAsc(placeId)
                .stream()
                .map(CommentListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Page<CommentListResponseDto> getTest(Long placeId,
                                                @RequestParam int page,
                                                @RequestParam int size,
                                                @RequestParam String condition){

        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.test(placeId,pageable,condition);

    }

    public CommentListResponseDto save(CommentSaveRequestDto commentSaveRequestDto, Long placeId, Member member) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(PlaceNotFoundException::new);

        if(member == null){
            throw new MemberNotFoundException();
        }

        Comment comment = Comment.builder() 
                .content(commentSaveRequestDto.getContent())
                .member(member)
                .place(place)
                .build();

        Comment saveComment = commentRepository.save(comment);

        //알람 추가
        alarmRepository.save(Alarm.builder()
                .member(place.getTrip().getMember())
                .alarmType(AlarmType.NEW_COMMENT)
                .makeAlarmMemberId(member.getId())
                .placeId(placeId)
                .targetId(comment.getId())
                .build());

        return new CommentListResponseDto(saveComment);
    }

    public void delete(Long commentId, Member member) {

        if(member == null){
            throw new MemberNotFoundException();
        }

        Comment comment = Optional.ofNullable(commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException())).get();

        if(comment.getMember().getId() != member.getId()) {
            throw new ForbiddenException();
        }

        Place place = comment.getPlace();

        if(place==null){
            throw new PlaceNotFoundException();
        }

        commentRepository.delete(comment);

        //알림 삭제
        Alarm findAlarm = alarmRepository.findCommentAlarmByMemberAndMakeMemberIdAndCommentId(
                place.getTrip().getMember(), member.getId(), place.getId(), commentId);
        if(findAlarm == null) {
            throw new AlarmNotFoundException();
        }
        alarmRepository.delete(findAlarm);
    }
}
