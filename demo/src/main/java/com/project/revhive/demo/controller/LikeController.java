package com.project.revhive.demo.controller;

import com.project.revhive.demo.service.LikeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public String addLike(@RequestParam Long userId,
                          @RequestParam String postId) {
        return likeService.addLike(userId, postId);
    }

    @DeleteMapping
    public String removeLike(@RequestParam Long userId,
                             @RequestParam String postId) {
        return likeService.removeLike(userId, postId);
    }

    @GetMapping("/count")
    public long getLikeCount(@RequestParam String postId) {
        return likeService.getLikeCount(postId);
    }

    @GetMapping("/status")
    public boolean isLiked(@RequestParam Long userId,
                           @RequestParam String postId) {
        return likeService.isLiked(userId, postId);
    }
}