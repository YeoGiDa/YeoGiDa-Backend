
//package com.Udemy.YeoGiDa.domain.comment.service;
//
//import com.Udemy.YeoGiDa.domain.comment.repository.CommentRepository;
//import com.Udemy.YeoGiDa.domain.comment.response.CommentResponseDto;
//import com.Udemy.YeoGiDa.domain.place.response.PlaceListResponseDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class CommentService {
//
//    private final CommentRepository commentRepository;
//
//    @Transactional(readOnly = true)
//    public List<CommentResponseDto> getCommentListByDesc(){
//        return commentRepository.CommentAllDesc()
//                .stream()
//                .map(PlaceListResponseDto::new)
//                .collect(Collectors.toList());
//    }
//}
