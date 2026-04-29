package com.project.revhive.demo.repository;

import com.project.revhive.demo.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    long countByPostId(String postId);

    boolean existsByUserIdAndPostId(Long userId, String postId);

    void deleteByUserIdAndPostId(Long userId, String postId);
}
