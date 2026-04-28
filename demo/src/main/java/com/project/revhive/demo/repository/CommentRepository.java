package com.project.revhive.demo.repository;

import com.project.revhive.demo.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(String postId);

    long countByPostId(String postId);
}