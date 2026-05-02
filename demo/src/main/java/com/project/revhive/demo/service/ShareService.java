package com.project.revhive.demo.service;

import com.project.revhive.demo.model.Share;
import com.project.revhive.demo.repository.ShareRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShareService {

    private final ShareRepository shareRepository;

    public ShareService(ShareRepository shareRepository) {
        this.shareRepository = shareRepository;
    }

    public String sharePost(Long userId, String postId) {
        if (shareRepository.existsByUserIdAndPostId(userId, postId)) {
            return "Already shared";
        }

        Share share = new Share();
        share.setUserId(userId);
        share.setPostId(postId);
        share.setCreatedAt(LocalDateTime.now());

        shareRepository.save(share);
        return "Post shared successfully";
    }

    public long getShareCount(String postId) {
        return shareRepository.countByPostId(postId);
    }

    public List<Share> getShares(String postId) {
        return shareRepository.findByPostId(postId);
    }
}