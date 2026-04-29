package com.project.revhive.demo.repository;

import com.project.revhive.demo.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(String postId);

    long countByPostId(String postId);
    List<Comment> findByParentCommentId(Long parentCommentId);
}
