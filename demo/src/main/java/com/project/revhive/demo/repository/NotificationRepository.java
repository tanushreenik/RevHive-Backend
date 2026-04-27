package com.project.revhive.demo.repository;


import com.project.revhive.demo.model.Notification;
import com.project.revhive.demo.model.User;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

//    Page<Notification> findByFollowerAndFollowing(User follower, User counfollowing);

    long countByUserAndReadFalse(User user);
    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.user = :user AND n.id = :notificationId")
    void markAsRead(@Param("user") User user, @Param("notificationId") Long notificationId);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.user = :user")
    void markAllAsRead(@Param("user") User user);
}
