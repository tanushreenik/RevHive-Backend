package com.project.revhive.demo.repository;

import com.project.revhive.demo.model.Follow;
import com.project.revhive.demo.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    // Check if a follow relationship exists
    boolean existsByFollowerAndFollowing(User follower, User following);

    // Find follow relationship between two users
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    // Get all followers of a user
    List<Follow> findByFollowing(User following);

    // Get all users that a user is following
    List<Follow> findByFollower(User follower);

    // Get followers count for a user
    long countByFollowing(User following);

    // Get following count for a user
    long countByFollower(User follower);

    // Get followers with pagination
    List<Follow> findByFollowing(User following, Pageable pageable);

    // Get following with pagination
    List<Follow> findByFollower(User follower, Pageable pageable);

    // Get follower IDs for a user
    @Query("SELECT f.follower.id FROM Follow f WHERE f.following.id = :userId")
    List<Long> findFollowerIdsByFollowingId(@Param("userId") Long userId);

    // Get following IDs for a user
    @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId")
    List<Long> findFollowingIdsByFollowerId(@Param("userId") Long userId);

    // Delete follow relationship
    @Modifying
    @Transactional
    void deleteByFollowerAndFollowing(User follower, User following);

    // Check if user is following another user
    @Query("SELECT COUNT(f) > 0 FROM Follow f WHERE f.follower.id = :followerId AND f.following.id = :followingId")
    boolean isFollowing(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    // Get mutual followers between two users
    @Query("SELECT f1.follower FROM Follow f1 JOIN Follow f2 ON f1.follower = f2.follower " +
            "WHERE f1.following.id = :userId1 AND f2.following.id = :userId2")
    List<User> findMutualFollowers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    // Get recent follows
    List<Follow> findByFollowingOrderByCreatedAtDesc(User following, Pageable pageable);
}