package com.project.revhive.demo.repository;

import com.project.revhive.demo.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserIdAndPostId(Long userId, String postId);

    List<Bookmark> findByUserId(Long userId);

    void deleteByUserIdAndPostId(Long userId, String postId);
}