package com.project.revhive.demo.controller;

import com.project.revhive.demo.model.Comment;
import com.project.revhive.demo.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    // ➕ Add comment
    @PostMapping
    public Comment addComment(@RequestParam Long userId,
                              @RequestParam String postId,
                              @RequestParam String content) {
        return service.addComment(userId, postId, content);
    }

    // 📄 Get comments
    @GetMapping
    public List<Comment> getComments(@RequestParam String postId) {
        return service.getComments(postId);
    }

    // 🗑 Delete comment
    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable Long id) {
        service.deleteComment(id);
        return "Deleted successfully";
    }

    // Count comments
    @GetMapping("/count")
    public long count(@RequestParam String postId) {
        return service.countComments(postId);
    }
}
