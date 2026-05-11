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

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUser(User user, Pageable pageable);
    Page<Post> findByIsActiveTrue(Pageable pageable);


    @Query("SELECT p FROM Post p WHERE LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.isActive = true")
    Page<Post> searchByContent(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isActive = true ORDER BY p.likeCount DESC, p.createdAt DESC")
    Page<Post> findTrendingPosts(Pageable pageable);

    long countByUser_Id(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
    void incrementLikeCount(@Param("postId") Long postId);

    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.likeCount = p.likeCount - 1 WHERE p.id = :postId AND p.likeCount > 0")
    void decrementLikeCount(@Param("postId") Long postId);



    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.isActive = false, p.updatedAt = :updatedAt WHERE p.id = :postId")
    void softDeletePost(@Param("postId") Long postId, @Param("updatedAt") Long updatedAt);

    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.isActive = true, p.updatedAt = :updatedAt WHERE p.id = :postId")
    void restorePost(@Param("postId") Long postId, @Param("updatedAt") Long updatedAt);
}
