package com.project.revhive.demo.repository;

import com.project.revhive.demo.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPostIdAndIsActiveTrue(Long postId, Pageable pageable);

    long countByPostIdAndIsActiveTrue(Long postId);

    @Modifying
    @Transactional
    @Query("UPDATE Comment c SET c.isActive = false WHERE c.id = :commentId")
    void softDeleteComment(@Param("commentId") Long commentId);
}
