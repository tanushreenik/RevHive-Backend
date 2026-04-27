package com.project.revhive.demo.service;

import com.project.revhive.demo.model.Like;
import com.project.revhive.demo.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    public String toggleLike(Long userId, String postId) {

        Optional<Like> existing = likeRepository.findByUserIdAndPostId(userId, postId);

        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
            return "Unliked";
        } else {
            Like like = new Like();
            like.setUserId(userId);
            like.setPostId(postId);
            likeRepository.save(like);
            return "Liked";
        }
    }

    public List<Like> getLikes(String postId) {
        return likeRepository.findByPostId(postId);
    }
}