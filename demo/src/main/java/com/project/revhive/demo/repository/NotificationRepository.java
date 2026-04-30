package com.project.revhive.demo.repository;

import com.project.revhive.demo.model.Notification;
import com.project.revhive.demo.model.User;
import com.project.revhive.demo.enums.NotificationType;
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
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Get all notifications for a user
    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    // Get notifications for a user with pagination
    List<Notification> findByUser(User user, Pageable pageable);

    // Get unread notifications for a user
    List<Notification> findByUserAndIsReadFalseOrderByCreatedAtDesc(User user);

    // Get unread notifications count for a user
    long countByUserAndIsReadFalse(User user);

    // Get notifications by type
    List<Notification> findByUserAndTypeOrderByCreatedAtDesc(User user, NotificationType type);

    // Get recent notifications
    List<Notification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    // Mark notification as read
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :notificationId AND n.user.id = :userId")
    int markAsRead(@Param("notificationId") Long notificationId, @Param("userId") Long userId);

    // Mark all notifications as read for a user
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.user.id = :userId AND n.isRead = false")
    int markAllAsRead(@Param("userId") Long userId);

    // Delete all notifications for a user
    @Modifying
    @Transactional
    void deleteByUser(User user);

    // Delete old notifications (older than specified timestamp)
    @Modifying
    @Transactional
    void deleteByCreatedAtLessThan(Long timestamp);

    // Check if user has unread notifications
    boolean existsByUserAndIsReadFalse(User user);

    // Get latest notification by type for a user
    Optional<Notification> findTopByUserAndTypeOrderByCreatedAtDesc(User user, NotificationType type);

    // Get notifications where actor is specific user
    List<Notification> findByActorAndUserOrderByCreatedAtDesc(User actor, User user);

    // Get notifications for multiple users
    List<Notification> findByUserInOrderByCreatedAtDesc(List<User> users, Pageable pageable);

    // Count notifications by type for a user
    @Query("SELECT n.type, COUNT(n) FROM Notification n WHERE n.user.id = :userId GROUP BY n.type")
    List<Object[]> countNotificationsByType(@Param("userId") Long userId);

    // Get notifications with specific entity
    List<Notification> findByUserAndEntityIdOrderByCreatedAtDesc(User user, Long entityId);

    // Delete notifications related to specific entity
    @Modifying
    @Transactional
    void deleteByEntityId(Long entityId);

    // Batch mark notifications as read
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.user.id = :userId AND n.id IN :notificationIds")
    void batchMarkAsRead(@Param("userId") Long userId, @Param("notificationIds") List<Long> notificationIds);

}