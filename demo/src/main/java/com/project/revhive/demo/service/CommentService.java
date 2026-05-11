package com.project.revhive.demo.service;

import com.project.revhive.demo.dto.request.CommentRequest;
import com.project.revhive.demo.model.Comment;
import com.project.revhive.demo.model.Post;
import com.project.revhive.demo.model.User;
import com.project.revhive.demo.repository.CommentRepository;
import com.project.revhive.demo.repository.PostRepository;
import com.project.revhive.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment addComment(CommentRequest request) {
        log.info("Adding comment to post: {} by user: {}", request.getPostId(), request.getUserId());

        // Get the post
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + request.getPostId()));

        // Get the user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        // Create comment
        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .user(user)
                .isActive(true)
                .likeCount(0)
                .build();

        Comment savedComment = commentRepository.save(comment);

        // Update post comment count
        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        log.info("Comment added successfully. Post now has {} comments", post.getCommentCount());

        return savedComment;
    }

    public Page<Comment> getCommentsByPost(Long postId, Pageable pageable) {
        log.info("Fetching comments for post: {}", postId);
        return commentRepository.findByPostIdAndIsActiveTrue(postId, pageable);
    }

    public long getCommentCount(Long postId) {
        log.info("Getting comment count for post: {}", postId);
        return commentRepository.countByPostIdAndIsActiveTrue(postId);
    }

    @Transactional
    public void deleteComment(Long commentId, String userEmail) {
        log.info("Deleting comment: {} by user: {}", commentId, userEmail);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));

        // Check if user owns the comment
        if (!comment.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only delete your own comments");
        }

        // Soft delete
        commentRepository.softDeleteComment(commentId);

        // Update post comment count
        Post post = comment.getPost();
        post.setCommentCount(Math.max(0, post.getCommentCount() - 1));
        postRepository.save(post);

        log.info("Comment deleted successfully");
    }

    @Transactional
    public Comment updateComment(Long commentId, String content, String userEmail) {
        log.info("Updating comment: {} by user: {}", commentId, userEmail);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));

        if (!comment.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only edit your own comments");
        }

        comment.setContent(content);
        Comment updatedComment = commentRepository.save(comment);

        log.info("Comment updated successfully");

        return updatedComment;
    }
}
