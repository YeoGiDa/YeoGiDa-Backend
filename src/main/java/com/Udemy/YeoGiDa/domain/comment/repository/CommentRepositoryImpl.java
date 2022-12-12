package com.Udemy.YeoGiDa.domain.comment.repository;

import com.Udemy.YeoGiDa.domain.comment.entity.Comment;
import com.Udemy.YeoGiDa.domain.comment.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Udemy.YeoGiDa.domain.comment.entity.QComment.*;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findAllByPlaceByIdDesc(Long placeId) {
        return queryFactory.selectFrom(comment)
                .where(comment.place.id.eq(placeId))
                .orderBy(comment.id.desc())
                .fetch();
    }

    @Override
    public List<Comment> findAllByPlaceByIdAsc(Long placeId) {
        return queryFactory.selectFrom(comment)
                .where(comment.place.id.eq(placeId))
                .orderBy(comment.id.asc())
                .fetch();
    }
}
