package com.project.revhive.demo.controller;

import com.project.revhive.demo.dto.request.CommentRequest;
import com.project.revhive.demo.model.Comment;
import com.project.revhive.demo.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> addComment(@Valid @RequestBody CommentRequest request) {
        log.info("POST /api/comments - Adding comment to post: {}", request.getPostId());
        Comment comment = commentService.addComment(request);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Page<Comment>> getCommentsByPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET /api/comments/post/{} - Page: {}, Size: {}", postId, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> comments = commentService.getCommentsByPost(postId, pageable);

        return ResponseEntity.ok(comments);
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<Map<String, Object>> getCommentCount(@PathVariable Long postId) {
        log.info("GET /api/comments/count/{}", postId);

        long count = commentService.getCommentCount(postId);

        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        response.put("postId", postId);
        response.put("success", true);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(
            @PathVariable Long commentId,
            Authentication authentication) {

        log.info("DELETE /api/comments/{} by user: {}", commentId, authentication.getName());

        commentService.deleteComment(commentId, authentication.getName());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Comment deleted successfully");
        response.put("commentId", commentId);
        response.put("success", true);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long commentId,
            @RequestBody String content,
            Authentication authentication) {

        log.info("PUT /api/comments/{} by user: {}", commentId, authentication.getName());

        Comment updatedComment = commentService.updateComment(commentId, content, authentication.getName());

        return ResponseEntity.ok(updatedComment);
    }
}
