package com.project.revhive.demo.repository;

import com.project.revhive.demo.model.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {

    boolean existsByUserIdAndPostId(Long userId, String postId);

    long countByPostId(String postId);

    List<Share> findByPostId(String postId);
}
