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

    // ➕ Add comment
    public Comment addComment(Long userId, String postId, String content) {

        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("Comment cannot be empty");
        }

        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setPostId(postId);
        comment.setContent(content);

        return repo.save(comment);
    }

    // 📄 Get comments
    public List<Comment> getComments(String postId) {
        return repo.findByPostId(postId);
    }

    // 🗑 Delete comment
    public void deleteComment(Long id) {
        repo.deleteById(id);
    }

    // 🔢 Count comments
    public long countComments(String postId) {
        return repo.countByPostId(postId);
    }
}