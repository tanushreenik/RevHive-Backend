package com.project.revhive.demo.service;

import com.project.revhive.demo.dto.request.PostRequest;
import com.project.revhive.demo.dto.response.PostResponse;
import com.project.revhive.demo.model.Post;
import com.project.revhive.demo.model.User;
import com.project.revhive.demo.repository.PostRepository;
import com.project.revhive.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeService likeService;
    private final NotificationService notificationService;
    private final FollowService followService;

    // Create post
    public PostResponse createPost(Long userId, PostRequest postRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Post post = Post.builder()
                .user(user)
                .content(postRequest.getContent())
                .imageUrl(postRequest.getImageUrl())
                .videoUrl(postRequest.getVideoUrl())
                .likeCount(0)
                .commentCount(0)
                .shareCount(0)
                .isActive(true)
                .build();

        Post savedPost = postRepository.save(post);
        log.info("User {} created new post with id: {}", userId, savedPost.getId());

        return PostResponse.fromPost(savedPost, false);
    }

    // Get post by ID
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        if (!post.isActive()) {
            throw new RuntimeException("Post has been deleted");
        }

        boolean isLiked = likeService.isPostLikedByUser(currentUserId, postId);
        return PostResponse.fromPost(post, isLiked);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getUserPosts(Long userId, Long currentUserId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = postRepository.findByUserAndIsActiveTrueOrderByCreatedAtDesc(user, pageable);

        return posts.map(post -> PostResponse.fromPost(
                post,
                likeService.isPostLikedByUser(currentUserId, post.getId())
        ));
    }


    @Transactional(readOnly = true)
    public Page<PostResponse> getFeedPosts(Long userId, int page, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Get users that the current user follows
        List<User> followingUsers = followService.getFollowingUsers(userId);

        // Add current user to include their own posts
        User currentUser = userRepository.findById(userId).orElseThrow();
        followingUsers.add(currentUser);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = postRepository.findPostsByUsers(followingUsers, pageable);

        return posts.map(post -> PostResponse.fromPost(
                post,
                likeService.isPostLikedByUser(userId, post.getId())
        ));
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts(Long currentUserId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = postRepository.findByIsActiveTrueOrderByCreatedAtDesc(pageable);

        return posts.map(post -> PostResponse.fromPost(
                post,
                likeService.isPostLikedByUser(currentUserId, post.getId())
        ));
    }

    // Update post
    public PostResponse updatePost(Long postId, Long userId, PostRequest postRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));


        if (!post.isActive()) {
            throw new RuntimeException("Cannot update deleted post");
        }

        post.setContent(postRequest.getContent());
        post.setImageUrl(postRequest.getImageUrl());
        post.setVideoUrl(postRequest.getVideoUrl());

        Post updatedPost = postRepository.save(post);
        log.info("Post {} updated by user {}", postId, userId);

        boolean isLiked = likeService.isPostLikedByUser(userId, postId);
        return PostResponse.fromPost(updatedPost, isLiked);
    }

    // Delete post
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));


        int deleted = postRepository.softDeletePost(postId, userId, System.currentTimeMillis());
        if (deleted == 0) {
            throw new RuntimeException("Failed to delete post");
        }

        log.info("Post {} soft deleted by user {}", postId, userId);
    }

    // Like post
    @Transactional
    public void likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (!post.isActive()) {
            throw new RuntimeException("Cannot like deleted post");
        }


        if (likeService.isPostLikedByUser(userId, postId)) {
            throw new RuntimeException("Already liked this post");
        }


        likeService.createLike(user, post);


        postRepository.updateLikeCount(postId, 1);

        log.info("User {} liked post {}", userId, postId);
    }

    // Unlike post
    @Transactional
    public void unlikePost(Long postId, Long userId) {
        if (!likeService.isPostLikedByUser(userId, postId)) {
            throw new RuntimeException("Post not liked yet");
        }

        // Delete like record
        likeService.deleteLike(userId, postId);

        // Update post like count
        postRepository.updateLikeCount(postId, -1);

        log.info("User {} unliked post {}", userId, postId);
    }

    // Get trending posts
    @Transactional(readOnly = true)
    public List<PostResponse> getTrendingPosts(Long currentUserId, int limit) {
        Long sevenDaysAgo = System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000);
        List<Post> trendingPosts = postRepository.findTrendingPosts(sevenDaysAgo, limit);

        return trendingPosts.stream()
                .map(post -> PostResponse.fromPost(
                        post,
                        likeService.isPostLikedByUser(currentUserId, post.getId())
                ))
                .collect(Collectors.toList());
    }

    // Get post count by user
    @Transactional(readOnly = true)
    public long getUserPostCount(Long userId) {
        return postRepository.countByUserIdAndIsActiveTrue(userId);
    }

    // Search posts by content
    @Transactional(readOnly = true)
    public Page<PostResponse> searchPosts(String keyword, Long currentUserId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = postRepository.findByContentContainingIgnoreCaseAndIsActiveTrue(keyword, pageable);

        return posts.map(post -> PostResponse.fromPost(
                post,
                likeService.isPostLikedByUser(currentUserId, post.getId())
        ));
    }

    // Update comment count
    @Transactional
    public void updateCommentCount(Long postId, int increment) {
        postRepository.updateCommentCount(postId, increment);
    }

    // Update share count
    @Transactional
    public void updateShareCount(Long postId, int increment) {
        postRepository.updateShareCount(postId, increment);
    }
}