package com.project.revhive.demo.controller;

import com.project.revhive.demo.dto.request.CreatePostRequest;
import com.project.revhive.demo.dto.request.UpdatePostRequest;
import com.project.revhive.demo.model.Post;
import com.project.revhive.demo.repository.UserRepository;
import com.project.revhive.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;
    private final UserRepository userRepository;

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        throw new RuntimeException("User not authenticated");
    }


    @PostMapping
    public ResponseEntity<Post> createPost(
            Authentication authentication,
            @Valid @RequestBody CreatePostRequest request) {

        Long userId = getUserIdFromAuthentication(authentication);

        logger.info("POST /api/posts - Create post for user ID: {}", userId);

        Post post = postService.createPost(
                userId,
                request.getContent(),
                request.getImageUrl(),
                request.getVideoUrl()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) {
        logger.info("GET /api/posts/{} - Fetch post by ID", postId);

        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/me")
    public ResponseEntity<Page<Post>> getMyPosts(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long userId = getUserIdFromAuthentication(authentication);
        logger.info("GET /api/posts/me - Fetch my posts - Page: {}, Size: {}", page, size);

        Page<Post> posts = postService.getUserPosts(userId, page, size);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Post>> getUserPosts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("GET /api/posts/user/{} - Fetch user posts - Page: {}, Size: {}", userId, page, size);

        Page<Post> posts = postService.getUserPosts(userId, page, size);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/feed")
    public ResponseEntity<Page<Post>> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("GET /api/posts/feed - Fetch feed - Page: {}, Size: {}", page, size);

        Page<Post> feed = postService.getFeed(page, size);
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/trending")
    public ResponseEntity<Page<Post>> getTrendingPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("GET /api/posts/trending - Fetch trending posts - Page: {}, Size: {}", page, size);

        Page<Post> trendingPosts = postService.getTrendingPosts(page, size);
        return ResponseEntity.ok(trendingPosts);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Post>> searchPosts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("GET /api/posts/search - Search posts with keyword: {} - Page: {}, Size: {}", q, page, size);

        Page<Post> searchResults = postService.searchPosts(q, page, size);
        return ResponseEntity.ok(searchResults);
    }


    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            Authentication authentication,
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest request) {

        Long userId = getUserIdFromAuthentication(authentication);
        logger.info("PUT /api/posts/{} - Update post by user ID: {}", postId, userId);

        Post updatedPost = postService.updatePost(
                postId,
                userId,
                request.getContent(),
                request.getImageUrl(),
                request.getVideoUrl()
        );

        return ResponseEntity.ok(updatedPost);
    }



    @PostMapping("/{postId}/like")
    public ResponseEntity<Map<String, Object>> likePost(@PathVariable Long postId) {
        logger.info("POST /api/posts/{}/like - Like post", postId);

        postService.likePost(postId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Post liked successfully");
        response.put("postId", postId);
        response.put("success", true);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Map<String, Object>> unlikePost(@PathVariable Long postId) {
        logger.info("DELETE /api/posts/{}/like - Unlike post", postId);

        postService.unlikePost(postId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Post unliked successfully");
        response.put("postId", postId);
        response.put("success", true);

        return ResponseEntity.ok(response);
    }



    @DeleteMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> deletePost(
            Authentication authentication,
            @PathVariable Long postId) {

        Long userId = getUserIdFromAuthentication(authentication);
        logger.info("DELETE /api/posts/{} - Delete post by user ID: {}", postId, userId);

        postService.deletePost(postId, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Post deleted successfully");
        response.put("postId", postId);
        response.put("success", true);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}/soft")
    public ResponseEntity<Map<String, Object>> softDeletePost(
            Authentication authentication,
            @PathVariable Long postId) {

        Long userId = getUserIdFromAuthentication(authentication);
        logger.info("DELETE /api/posts/{}/soft - Soft delete post by user ID: {}", postId, userId);

        postService.softDeletePost(postId, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Post soft deleted successfully");
        response.put("postId", postId);
        response.put("success", true);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{postId}/restore")
    public ResponseEntity<Map<String, Object>> restorePost(
            Authentication authentication,
            @PathVariable Long postId) {

        Long userId = getUserIdFromAuthentication(authentication);
        logger.info("POST /api/posts/{}/restore - Restore post by user ID: {}", postId, userId);

        postService.restorePost(postId, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Post restored successfully");
        response.put("postId", postId);
        response.put("success", true);

        return ResponseEntity.ok(response);
    }


    private Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();
        // You'll need to add a method to get user by email in UserRepository
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Map<String, Long>> getUserPostsCount(
            @PathVariable Long userId) {

        long count = postService.getUserPostsCount(userId);

        Map<String, Long> response = new HashMap<>();
        response.put("postsCount", count);

        return ResponseEntity.ok(response);
    }
}
