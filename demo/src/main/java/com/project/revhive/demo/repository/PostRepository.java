package com.project.revhive.demo.repository;

import com.project.revhive.demo.model.Post;
import com.project.revhive.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Find posts by user
    Page<Post> findByUserAndIsActiveTrueOrderByCreatedAtDesc(User user, Pageable pageable);

    // Find all active posts
    Page<Post> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);

    // Find posts by user ID
    Page<Post> findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // Find posts by multiple users (for feed)
    @Query("SELECT p FROM Post p WHERE p.user IN :users AND p.isActive = true ORDER BY p.createdAt DESC")
    Page<Post> findPostsByUsers(@Param("users") List<User> users, Pageable pageable);

    // Update like count
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + :increment WHERE p.id = :postId")
    void updateLikeCount(@Param("postId") Long postId, @Param("increment") int increment);

    // Update comment count
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.commentCount = p.commentCount + :increment WHERE p.id = :postId")
    void updateCommentCount(@Param("postId") Long postId, @Param("increment") int increment);

    // Update share count
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.shareCount = p.shareCount + :increment WHERE p.id = :postId")
    void updateShareCount(@Param("postId") Long postId, @Param("increment") int increment);

    // Soft delete post
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.isActive = false, p.updatedAt = :timestamp WHERE p.id = :postId AND p.user.id = :userId")
    int softDeletePost(@Param("postId") Long postId, @Param("userId") Long userId, @Param("timestamp") Long timestamp);

    // Count posts by user
    long countByUserIdAndIsActiveTrue(Long userId);

    // Get trending posts (most liked in last 7 days)
    @Query(value = "SELECT p.* FROM posts p WHERE p.is_active = true AND p.created_at > :sinceTimestamp ORDER BY p.like_count DESC LIMIT :limit",
            nativeQuery = true)
    List<Post> findTrendingPosts(@Param("sinceTimestamp") Long sinceTimestamp, @Param("limit") int limit);

    // FIXED: Add this method for search functionality
    Page<Post> findByContentContainingIgnoreCaseAndIsActiveTrue(String keyword, Pageable pageable);

    // Get posts by date range
    List<Post> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(Long userId, Long startDate, Long endDate);
}