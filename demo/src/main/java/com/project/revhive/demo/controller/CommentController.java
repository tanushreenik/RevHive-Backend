package com.project.revhive.demo.controller;

import com.project.revhive.demo.model.Comment;
import com.project.revhive.demo.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public Comment addComment(@RequestParam Long userId,
                              @RequestParam String postId,
                              @RequestParam String content) {
        return commentService.addComment(userId, postId, content);
    }

    @PostMapping("/reply")
    public Comment replyToComment(@RequestParam Long userId,
                                  @RequestParam String postId,
                                  @RequestParam Long parentCommentId,
                                  @RequestParam String content) {
        return commentService.replyToComment(userId, postId, parentCommentId, content);
    }

    @GetMapping
    public List<Comment> getComments(@RequestParam String postId) {
        return commentService.getCommentsByPost(postId);
    }


    // Count comments

    @GetMapping("/count")
    public long getCommentCount(@RequestParam String postId) {
        return commentService.getCommentCount(postId);
    }


    @PutMapping("/edit")
    public String editComment(@RequestParam Long commentId,
                              @RequestParam String content) {
        return commentService.editComment(commentId, content);
    }
}