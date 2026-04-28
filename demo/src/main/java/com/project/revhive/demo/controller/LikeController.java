package com.project.revhive.demo.controller;

import com.project.revhive.demo.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping
    public ResponseEntity<?> like(@RequestParam Long userId,
                                  @RequestParam String postId) {
        return ResponseEntity.ok(likeService.toggleLike(userId, postId));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getLikes(@PathVariable String postId) {
        return ResponseEntity.ok(likeService.getLikes(postId));
    }
}
