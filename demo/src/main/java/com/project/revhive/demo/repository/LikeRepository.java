package com.project.revhive.demo.repository;

import com.project.revhive.demo.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndPostId(Long userId, String postId);

    List<Like> findByPostId(String postId);
}