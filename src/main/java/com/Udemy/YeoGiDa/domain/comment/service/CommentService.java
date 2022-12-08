
package com.Udemy.YeoGiDa.domain.comment.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final PlaceRepository placeRepository;
    private final CommentRepository commentRepository;

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

    public CommentListResponseDto save(CommentSaveRequestDto commentSaveRequestDto, Long placeId, Member member) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(PlaceNotFoundException::new);

        if(member == null){
            throw new MemberNotFoundException();
        }

        if(place.getTrip().getMember() != member){
            throw new ForbiddenException();
        }

        Comment comment = Comment.builder() 
                .content(commentSaveRequestDto.getContent())
                .member(member)
                .place(place)
                .build();

        Comment saveComment = commentRepository.save(comment);

        return new CommentListResponseDto(saveComment);
    }

    public void delete(Long commentId, Member member) {

        if(member == null){
            throw new MemberNotFoundException();
        }

        Comment comment = Optional.ofNullable(commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException())).get();

        Place place = comment.getPlace();

        if(place==null){
            throw new PlaceNotFoundException();
        }

        if(place.getTrip().getMember() != member){
            throw new ForbiddenException();
        }

        commentRepository.delete(comment);

    }
}
