package com.project.revhive.demo.service;

import com.project.revhive.demo.model.Comment;
import com.project.revhive.demo.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment addComment(Long userId, String postId, String content) {

        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setPostId(postId);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now()); // ✅ now works
        comment.setParentCommentId(null);

        return commentRepository.save(comment);
    }

    public Comment replyToComment(Long userId, String postId,
                                  Long parentCommentId, String content) {

        Comment reply = new Comment();
        reply.setUserId(userId);
        reply.setPostId(postId);
        reply.setContent(content);
        reply.setCreatedAt(LocalDateTime.now());
        reply.setParentCommentId(parentCommentId);

        return commentRepository.save(reply);
    }

    public List<Comment> getCommentsByPost(String postId) {
        return commentRepository.findByPostId(postId);
    }

    public long getCommentCount(String postId) {
        return commentRepository.countByPostId(postId);
    }

    public String editComment(Long commentId, String newContent) {

        return commentRepository.findById(commentId)
                .map(comment -> {
                    comment.setContent(newContent);
                    commentRepository.save(comment);
                    return "Comment updated";
                })
                .orElse("Comment not found");
    }
}