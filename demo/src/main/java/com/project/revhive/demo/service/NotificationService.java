package com.project.revhive.demo.service;

import com.project.revhive.demo.model.Notification;
import com.project.revhive.demo.model.User;
import com.project.revhive.demo.enums.NotificationType;
import com.project.revhive.demo.repository.NotificationRepository;
import com.project.revhive.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public Notification createFollowNotification(User recipient, User actor) {
        String content = actor.getUsername() + " started following you";

        Notification notification = Notification.builder()
                .user(recipient)
                .actor(actor)
                .type(NotificationType.FOLLOW)
                .content(content)
                .isRead(false)
                .build();

        return notificationRepository.save(notification);
    }

    // Create like notification
    public Notification createLikeNotification(User recipient, User actor, Long postId) {
        String content = actor.getUsername() + " liked your post";

        Notification notification = Notification.builder()
                .user(recipient)
                .actor(actor)
                .type(NotificationType.LIKE)
                .entityId(postId)
                .content(content)
                .isRead(false)
                .build();

        return notificationRepository.save(notification);
    }

    public Notification createCommentNotification(User recipient, User actor, Long postId) {
        String content = actor.getUsername() + " commented on your post";

        Notification notification = Notification.builder()
                .user(recipient)
                .actor(actor)
                .type(NotificationType.COMMENT)
                .entityId(postId)
                .content(content)
                .isRead(false)
                .build();

        return notificationRepository.save(notification);
    }

    public Notification createMentionNotification(User recipient, User actor, Long postId) {
        String content = actor.getUsername() + " mentioned you in a post";

        Notification notification = Notification.builder()
                .user(recipient)
                .actor(actor)
                .type(NotificationType.MENTION)
                .entityId(postId)
                .content(content)
                .isRead(false)
                .build();

        return notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return notificationRepository.findByUser(user, pageable);
    }

    // Get unread notifications
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user);
    }

    // Get unread count
    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return notificationRepository.countByUserAndIsReadFalse(user);
    }

    // Mark single notification as read - FIXED: Changed to void return type
    @Transactional
    public void markAsRead(Long userId, Long notificationId) {
        notificationRepository.markAsRead(notificationId, userId);
        log.info("Marked notification {} as read for user {}", notificationId, userId);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
        log.info("Marked all notifications as read for user {}", userId);
    }

    // Delete notification
    @Transactional
    public void deleteNotification(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));



        notificationRepository.delete(notification);
        log.info("Deleted notification {} for user {}", notificationId, userId);
    }

    // Delete all notifications for a user
    @Transactional
    public void deleteAllUserNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        notificationRepository.deleteByUser(user);
        log.info("Deleted all notifications for user {}", userId);
    }

    // Get notifications by type
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByType(Long userId, NotificationType type, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<Notification> notifications = notificationRepository.findByUserAndTypeOrderByCreatedAtDesc(user, type);

        // Apply pagination
        int start = page * size;
        int end = Math.min(start + size, notifications.size());

        if (start >= notifications.size()) {
            return List.of();
        }

        return notifications.subList(start, end);
    }

    // Get latest notification
    @Transactional(readOnly = true)
    public Notification getLatestNotification(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Pageable pageable = PageRequest.of(0, 1);
        List<Notification> notifications = notificationRepository.findByUser(user, pageable);
        return notifications.isEmpty() ? null : notifications.get(0);
    }

    // Get notification statistics
    @Transactional(readOnly = true)
    public Map<String, Object> getNotificationStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        long totalUnread = notificationRepository.countByUserAndIsReadFalse(user);
        List<Object[]> typeCounts = notificationRepository.countNotificationsByType(userId);

        Map<String, Long> notificationsByType = typeCounts.stream()
                .collect(Collectors.toMap(
                        arr -> ((NotificationType) arr[0]).toString(),
                        arr -> (Long) arr[1]
                ));

        return Map.of(
                "totalUnread", totalUnread,
                "notificationsByType", notificationsByType,
                "hasNotifications", notificationRepository.existsByUserAndIsReadFalse(user)
        );
    }

    // Batch mark notifications as read - FIXED: Changed to void return type
    @Transactional
    public void batchMarkAsRead(Long userId, List<Long> notificationIds) {
        if (notificationIds == null || notificationIds.isEmpty()) {
            return;
        }

        notificationRepository.batchMarkAsRead(userId, notificationIds);
        log.info("Batch marked {} notifications as read for user {}", notificationIds.size(), userId);
    }

    // Clean up old notifications - FIXED: Changed to void return type
    @Transactional
    public void cleanupOldNotifications(int daysOld) {
        long cutoffTime = System.currentTimeMillis() - (daysOld * 24L * 60 * 60 * 1000);
        notificationRepository.deleteByCreatedAtLessThan(cutoffTime);
        log.info("Cleaned up notifications older than {} days", daysOld);
    }

    // Delete notifications for specific entity
    @Transactional
    public void deleteNotificationsByEntityId(Long entityId) {
        notificationRepository.deleteByEntityId(entityId);
        log.info("Deleted notifications for entity {}", entityId);
    }

    @Transactional(readOnly = true)
    public boolean hasUnreadNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return notificationRepository.existsByUserAndIsReadFalse(user);
    }

    @Transactional(readOnly = true)
    public List<Notification> getFilteredNotifications(Long userId, NotificationType type, Boolean isRead, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Notification> notifications = notificationRepository.findByUser(user, pageable);

        return notifications.stream()
                .filter(n -> type == null || n.getType() == type)
                .filter(n -> isRead == null || n.isRead() == isRead)
                .collect(Collectors.toList());
    }
}