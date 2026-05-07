package com.project.revhive.demo.service;

import com.project.revhive.demo.model.Post;
import com.project.revhive.demo.model.User;
import com.project.revhive.demo.repository.PostRepository;
import com.project.revhive.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;


    @Transactional
    public Post createPost(Long userId, String content, String imageUrl, String videoUrl) {
        logger.info("Create post request received for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Post post = Post.builder()
                .user(user)
                .content(content)
                .imageUrl(imageUrl)
                .videoUrl(videoUrl)
                .likeCount(0)
                .commentCount(0)
                .shareCount(0)
                .isActive(true)
                .build();

        logger.info("Post created successfully for user ID: {}", userId);
        return postRepository.save(post);
    }


    public Post getPostById(Long postId) {
        logger.info("Fetch post request received for ID: {}", postId);

        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
    }

    public Page<Post> getUserPosts(Long userId, int page, int size) {
        logger.info("Fetch user posts request for user ID: {} - Page: {}, Size: {}", userId, page, size);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByUser(user, pageable);
    }

    public Page<Post> getFeed(int page, int size) {
        logger.info("Fetch feed request - Page: {}, Size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByIsActiveTrue(pageable);
    }

    public Page<Post> getTrendingPosts(int page, int size) {
        logger.info("Fetch trending posts request - Page: {}, Size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findTrendingPosts(pageable);
    }

    public Page<Post> searchPosts(String keyword, int page, int size) {
        logger.info("Search posts request with keyword: {} - Page: {}, Size: {}", keyword, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.searchByContent(keyword, pageable);
    }


    @Transactional
    public Post updatePost(Long postId, Long userId, String content, String imageUrl, String videoUrl) {
        logger.info("Update post request for post ID: {} by user ID: {}", postId, userId);

        Post post = validatePostOwnership(postId, userId);

        if (content != null) {
            post.setContent(content);
        }
        if (imageUrl != null) {
            post.setImageUrl(imageUrl);
        }
        if (videoUrl != null) {
            post.setVideoUrl(videoUrl);
        }

        post.setUpdatedAt(System.currentTimeMillis());

        logger.info("Post updated successfully for post ID: {}", postId);
        return postRepository.save(post);
    }


    @Transactional
    public void likePost(Long postId) {
        logger.info("Like post request for post ID: {}", postId);

        validatePostExists(postId);
        postRepository.incrementLikeCount(postId);

        logger.info("Post liked successfully for post ID: {}", postId);
    }

    @Transactional
    public void unlikePost(Long postId) {
        logger.info("Unlike post request for post ID: {}", postId);

        validatePostExists(postId);
        postRepository.decrementLikeCount(postId);

        logger.info("Post unliked successfully for post ID: {}", postId);
    }


    @Transactional
    public void deletePost(Long postId, Long userId) {
        logger.info("Delete post request for post ID: {} by user ID: {}", postId, userId);

        validatePostOwnership(postId, userId);
        postRepository.deleteById(postId);

        logger.info("Post deleted successfully for post ID: {}", postId);
    }

    @Transactional
    public void softDeletePost(Long postId, Long userId) {
        logger.info("Soft delete post request for post ID: {} by user ID: {}", postId, userId);

        validatePostOwnership(postId, userId);
        postRepository.softDeletePost(postId, System.currentTimeMillis());

        logger.info("Post soft deleted successfully for post ID: {}", postId);
    }

    @Transactional
    public void restorePost(Long postId, Long userId) {
        logger.info("Restore post request for post ID: {} by user ID: {}", postId, userId);

        validatePostOwnership(postId, userId);
        postRepository.restorePost(postId, System.currentTimeMillis());

        logger.info("Post restored successfully for post ID: {}", postId);
    }


    private Post validatePostOwnership(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        if (!Objects.equals(post.getUser().getId(), userId)) {
            logger.warn("User ID: {} not authorized to access post ID: {}", userId, postId);
            throw new RuntimeException("User not authorized to perform this action on post ID: " + postId);
        }

        return post;
    }

    public long getUserPostsCount(Long userId) {
        return postRepository.countByUser_Id(userId);
    }

    private void validatePostExists(Long postId) {
        if (!postRepository.existsById(postId)) {
            logger.warn("Post not found with ID: {}", postId);
            throw new RuntimeException("Post not found with ID: " + postId);
        }
    }

}
