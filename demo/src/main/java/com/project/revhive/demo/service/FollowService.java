package com.project.revhive.demo.service;

import com.project.revhive.demo.model.Follow;
import com.project.revhive.demo.model.User;
import com.project.revhive.demo.repository.FollowRepository;
import com.project.revhive.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    // Follow a user
    @Transactional
    public Follow followUser(Long followerId, Long followingId) {
        // Validate users exist
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower user not found with id: " + followerId));

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("Following user not found with id: " + followingId));

        // Check if already following
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new RuntimeException("Already following this user");
        }

        // Cannot follow self
        if (followerId.equals(followingId)) {
            throw new RuntimeException("Cannot follow yourself");
        }

        // Create follow relationship
        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        Follow savedFollow = followRepository.save(follow);

        // Update counts
//        userRepository.updateFollowersCount(followingId, 1);
//        userRepository.updateFollowingCount(followerId, 1);

        // Create notification
        notificationService.createFollowNotification(following, follower);

        log.info("User {} started following user {}", followerId, followingId);
        return savedFollow;
    }

    // Unfollow a user
    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower user not found with id: " + followerId));

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("Following user not found with id: " + followingId));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new RuntimeException("Follow relationship not found"));

        followRepository.delete(follow);

        // Update counts
//        userRepository.updateFollowersCount(followingId, -1);
//        userRepository.updateFollowingCount(followerId, -1);

        log.info("User {} unfollowed user {}", followerId, followingId);
    }

    // Check if user is following another user
    @Transactional(readOnly = true)
    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.isFollowing(followerId, followingId);
    }

    // Get all followers of a user
    @Transactional(readOnly = true)
    public List<User> getFollowers(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Pageable pageable = PageRequest.of(page, size);
        return followRepository.findByFollowing(user, pageable)
                .stream()
                .map(Follow::getFollower)
                .collect(Collectors.toList());
    }

    // Get all users that a user is following
    @Transactional(readOnly = true)
    public List<User> getFollowing(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Pageable pageable = PageRequest.of(page, size);
        return followRepository.findByFollower(user, pageable)
                .stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());
    }

    // Get followers count
    @Transactional(readOnly = true)
    public long getFollowersCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return followRepository.countByFollowing(user);
    }

    // Get following count
    @Transactional(readOnly = true)
    public long getFollowingCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return followRepository.countByFollower(user);
    }

    // Get follower IDs
    @Transactional(readOnly = true)
    public List<Long> getFollowerIds(Long userId) {
        return followRepository.findFollowerIdsByFollowingId(userId);
    }

    // Get following IDs
    @Transactional(readOnly = true)
    public List<Long> getFollowingIds(Long userId) {
        return followRepository.findFollowingIdsByFollowerId(userId);
    }

    // Get mutual followers
    @Transactional(readOnly = true)
    public List<User> getMutualFollowers(Long userId1, Long userId2) {
        return followRepository.findMutualFollowers(userId1, userId2);
    }

    // Get recent follows
    @Transactional(readOnly = true)
    public List<Follow> getRecentFollows(Long userId, int limit) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Pageable pageable = PageRequest.of(0, limit);
        return followRepository.findByFollowingOrderByCreatedAtDesc(user, pageable);
    }

    // Get follow suggestions based on followers of followed users
//    @Transactional(readOnly = true)
//    public List<User> getFollowSuggestions(Long userId, int limit) {
//        List<Long> followingIds = getFollowingIds(userId);
//
//        if (followingIds.isEmpty()) {
//            // If not following anyone, return random active users
//            return userRepository.findTopActiveUsers(limit, userId);
//        }
//
//        // Find users that the people you follow are following (excluding already followed and self)
//        return userRepository.findFollowSuggestions(userId, followingIds, PageRequest.of(0, limit));
//    }

    // Bulk unfollow
    @Transactional
    public void bulkUnfollow(Long followerId, List<Long> followingIds) {
        for (Long followingId : followingIds) {
            try {
                unfollowUser(followerId, followingId);
            } catch (Exception e) {
                log.error("Error unfollowing user {}: {}", followingId, e.getMessage());
            }
        }
    }
}