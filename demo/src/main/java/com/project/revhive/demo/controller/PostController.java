package com.project.revhive.demo.controller;

import com.project.revhive.demo.dto.request.PostRequest;
import com.project.revhive.demo.dto.response.PageResponse;
import com.project.revhive.demo.dto.response.PostResponse;
import com.project.revhive.demo.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class PostController {

    private final PostService postService;

    // Create post
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPost(
            @RequestParam Long userId,
            @Valid @RequestBody PostRequest postRequest) {

        PostResponse post = postService.createPost(userId, postRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Post created successfully");
        response.put("data", post);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get post by ID
    @GetMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> getPostById(
            @PathVariable Long postId,
            @RequestParam Long currentUserId) {

        PostResponse post = postService.getPostById(postId, currentUserId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", post);

        return ResponseEntity.ok(response);
    }

    // Get user's posts
    @GetMapping("/users/{userId}")
    public ResponseEntity<Map<String, Object>> getUserPosts(
            @PathVariable Long userId,
            @RequestParam Long currentUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PostResponse> posts = postService.getUserPosts(userId, currentUserId, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", posts.getContent());
        response.put("currentPage", posts.getNumber());
        response.put("pageSize", posts.getSize());
        response.put("totalElements", posts.getTotalElements());
        response.put("totalPages", posts.getTotalPages());
        response.put("isFirst", posts.isFirst());
        response.put("isLast", posts.isLast());

        return ResponseEntity.ok(response);
    }

    // Get feed posts
    @GetMapping("/feed")
    public ResponseEntity<Map<String, Object>> getFeedPosts(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PostResponse> posts = postService.getFeedPosts(userId, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", posts.getContent());
        response.put("currentPage", posts.getNumber());
        response.put("pageSize", posts.getSize());
        response.put("totalElements", posts.getTotalElements());
        response.put("totalPages", posts.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // Get all posts (explore)
    @GetMapping("/explore")
    public ResponseEntity<Map<String, Object>> getAllPosts(
            @RequestParam Long currentUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PostResponse> posts = postService.getAllPosts(currentUserId, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", posts.getContent());
        response.put("currentPage", posts.getNumber());
        response.put("pageSize", posts.getSize());
        response.put("totalElements", posts.getTotalElements());
        response.put("totalPages", posts.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // Update post
    @PutMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> updatePost(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @Valid @RequestBody PostRequest postRequest) {

        PostResponse updatedPost = postService.updatePost(postId, userId, postRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Post updated successfully");
        response.put("data", updatedPost);

        return ResponseEntity.ok(response);
    }

    // Delete post (soft delete)
    @DeleteMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> deletePost(
            @PathVariable Long postId,
            @RequestParam Long userId) {

        postService.deletePost(postId, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Post deleted successfully");

        return ResponseEntity.ok(response);
    }

    // Like post
    @PostMapping("/{postId}/like")
    public ResponseEntity<Map<String, Object>> likePost(
            @PathVariable Long postId,
            @RequestParam Long userId) {

        postService.likePost(postId, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Post liked successfully");

        return ResponseEntity.ok(response);
    }

    // Unlike post
    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Map<String, Object>> unlikePost(
            @PathVariable Long postId,
            @RequestParam Long userId) {

        postService.unlikePost(postId, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Post unliked successfully");

        return ResponseEntity.ok(response);
    }

    // Get trending posts
    @GetMapping("/trending")
    public ResponseEntity<Map<String, Object>> getTrendingPosts(
            @RequestParam Long currentUserId,
            @RequestParam(defaultValue = "10") int limit) {

        var trendingPosts = postService.getTrendingPosts(currentUserId, limit);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", trendingPosts);
        response.put("count", trendingPosts.size());

        return ResponseEntity.ok(response);
    }

    // Get user post count
    @GetMapping("/users/{userId}/count")
    public ResponseEntity<Map<String, Object>> getUserPostCount(@PathVariable Long userId) {

        long count = postService.getUserPostCount(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("userId", userId);
        response.put("postCount", count);

        return ResponseEntity.ok(response);
    }

    // Search posts
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchPosts(
            @RequestParam String keyword,
            @RequestParam Long currentUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PostResponse> posts = postService.searchPosts(keyword, currentUserId, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", posts.getContent());
        response.put("keyword", keyword);
        response.put("currentPage", posts.getNumber());
        response.put("pageSize", posts.getSize());
        response.put("totalElements", posts.getTotalElements());
        response.put("totalPages", posts.getTotalPages());

        return ResponseEntity.ok(response);
    }
}