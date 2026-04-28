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

    @PostMapping
    public Comment addComment(@RequestParam Long userId,
                              @RequestParam String postId,
                              @RequestParam String content,
                              @RequestParam(required = false) Long parentCommentId) {

        return service.addComment(userId, postId, content, parentCommentId);
    }

    @GetMapping
    public List<Comment> getComments(@RequestParam String postId) {
        return service.getComments(postId);
    }

    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable Long id) {
        service.deleteComment(id);
        return "Deleted successfully";
    }

    @GetMapping("/count")
    public long count(@RequestParam String postId) {
        return service.countComments(postId);
    }

    @GetMapping("/replies")
    public List<Comment> getReplies(@RequestParam Long parentCommentId) {
        return service.getReplies(parentCommentId);
    }
}