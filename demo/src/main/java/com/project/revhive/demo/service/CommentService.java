package com.project.revhive.demo.service;

import com.project.revhive.demo.model.Comment;
import com.project.revhive.demo.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository repo;

    public CommentService(CommentRepository repo) {
        this.repo = repo;
    }

    public Comment addComment(Long userId, String postId, String content, Long parentCommentId) {

        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment cannot be empty");
        }

        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setPostId(postId);
        comment.setContent(content);

        comment.setParentCommentId(parentCommentId);

        return repo.save(comment);
    }

    public List<Comment> getComments(String postId) {
        return repo.findByPostId(postId);
    }

    public void deleteComment(Long id) {
        repo.deleteById(id);
    }

    public long countComments(String postId) {
        return repo.countByPostId(postId);
    }

    public List<Comment> getReplies(Long parentCommentId) {
        return repo.findByParentCommentId(parentCommentId);
    }
}