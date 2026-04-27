package com.project.revhive.demo.repository;

import com.project.revhive.demo.model.Follow;
import com.project.revhive.demo.model.User;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    boolean existsByFollowerAndFollowing(User follower, User following);
//
//    Page<Follow> findByFollower(User follower, Pageable pageable);
//
//    Page<Follow> findByFollowing(User following, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Follow f WHERE f.follower = :follower AND f.following = :following")
    void deleteFollow(@Param("follower") User follower, @Param("following") User following);

    long countByFollower(User follower);

    long countByFollowing(User following);

}
