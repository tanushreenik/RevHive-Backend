package com.project.revhive.demo.controller;

import com.project.revhive.demo.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")  // Changed from "/likes" to "/api/likes"
@CrossOrigin(origins = "http://localhost:5173")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addLike(@RequestParam Long userId,
                                                       @RequestParam String postId) {
        String result = likeService.addLike(userId, postId);
        long likeCount = likeService.getLikeCount(postId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", result);
        response.put("liked", result.equals("Like added successfully"));
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> removeLike(@RequestParam Long userId,
                                                          @RequestParam String postId) {
        String result = likeService.removeLike(userId, postId);
        long likeCount = likeService.getLikeCount(postId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", result);
        response.put("liked", false);
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getLikeCount(@RequestParam String postId) {
        long count = likeService.getLikeCount(postId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> isLiked(@RequestParam Long userId,
                                           @RequestParam String postId) {
        boolean isLiked = likeService.isLiked(userId, postId);
        return ResponseEntity.ok(isLiked);
    }
}