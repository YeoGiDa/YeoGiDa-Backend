//package com.Udemy.YeoGiDa.domain.comment.repository;
//
//import com.Udemy.YeoGiDa.domain.comment.entity.Comment;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface CommentRepository extends JpaRepository<Comment,Long> {
//
//    @Query("SELECT c FROM Comment c GROUP BY c.place ORDER BY c.id DESC")
//    List<Comment> CommentAllDesc();
//
//}
