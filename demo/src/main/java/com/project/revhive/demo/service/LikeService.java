package com.project.revhive.demo.service;


import com.project.revhive.demo.model.Like;
import com.project.revhive.demo.repository.LikeRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public String addLike(Long userId, String postId) {

        if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
            return "Already liked";
        }

        Like like = new Like();
        like.setUserId(userId);
        like.setPostId(postId);

        likeRepository.save(like);
        return "Liked successfully";
    }

    public String removeLike(Long userId, String postId) {

        return likeRepository.findAll()
                .stream()
                .filter(like -> like.getUserId().equals(userId)
                        && like.getPostId().equals(postId))
                .findFirst()
                .map(like -> {
                    likeRepository.delete(like);
                    return "Unliked successfully";
                })
                .orElse("Like not found");
    }
    public long getLikeCount(String postId) {
        return likeRepository.countByPostId(postId);
    }

    public boolean isLiked(Long userId, String postId) {
        return likeRepository.existsByUserIdAndPostId(userId, postId);
    }
}
