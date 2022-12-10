package com.Udemy.YeoGiDa.domain.comment.repository;

import com.Udemy.YeoGiDa.domain.comment.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findAllByPlaceByIdDesc(Long placeId);

    List<Comment> findAllByPlaceByIdAsc(Long placeId);
}
