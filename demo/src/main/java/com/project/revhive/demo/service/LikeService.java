package com.project.revhive.demo.service;

import com.project.revhive.demo.model.Like;
import com.project.revhive.demo.model.Post;
import com.project.revhive.demo.model.User;
import com.project.revhive.demo.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final LikeRepository likeRepository;

    @Transactional
    public Like createLike(User user, Post post) {
        Like like = Like.builder()
                .user(user)
                .post(post)
                .build();

        return likeRepository.save(like);
    }

    @Transactional
    public void deleteLike(Long userId, Long postId) {
        likeRepository.deleteByUserIdAndPostId(userId, postId);
    }

    @Transactional(readOnly = true)
    public boolean isPostLikedByUser(Long userId, Long postId) {
        return likeRepository.existsByUserIdAndPostId(userId, postId);
    }

    @Transactional(readOnly = true)
    public long getPostLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}