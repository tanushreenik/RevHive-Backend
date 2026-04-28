package com.project.revhive.demo.controller;

import com.project.revhive.demo.model.Notification;
import com.project.revhive.demo.enums.NotificationType;
import com.project.revhive.demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<Map<String, Object>> getUserNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<Notification> notifications = notificationService.getUserNotifications(userId, page, size);
        long unreadCount = notificationService.getUnreadCount(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", notifications);
        response.put("unreadCount", unreadCount);
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalCount", notifications.size());

        return ResponseEntity.ok(response);
    }

    // Get unread notifications
    @GetMapping("/users/{userId}/unread")
    public ResponseEntity<Map<String, Object>> getUnreadNotifications(@PathVariable Long userId) {

        List<Notification> unreadNotifications = notificationService.getUnreadNotifications(userId);
        long unreadCount = notificationService.getUnreadCount(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", unreadNotifications);
        response.put("unreadCount", unreadCount);

        return ResponseEntity.ok(response);
    }

    // Get unread count
    @GetMapping("/users/{userId}/unread/count")
    public ResponseEntity<Map<String, Object>> getUnreadCount(@PathVariable Long userId) {

        long unreadCount = notificationService.getUnreadCount(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("userId", userId);
        response.put("unreadCount", unreadCount);

        return ResponseEntity.ok(response);
    }

    // Get notifications by type
    @GetMapping("/users/{userId}/type/{type}")
    public ResponseEntity<Map<String, Object>> getNotificationsByType(
            @PathVariable Long userId,
            @PathVariable NotificationType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<Notification> notifications = notificationService.getNotificationsByType(userId, type, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", notifications);
        response.put("type", type);
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalCount", notifications.size());

        return ResponseEntity.ok(response);
    }

    // Get latest notification
    @GetMapping("/users/{userId}/latest")
    public ResponseEntity<Map<String, Object>> getLatestNotification(@PathVariable Long userId) {

        Notification latestNotification = notificationService.getLatestNotification(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", latestNotification);

        return ResponseEntity.ok(response);
    }

    // Get notification statistics
    @GetMapping("/users/{userId}/statistics")
    public ResponseEntity<Map<String, Object>> getNotificationStatistics(@PathVariable Long userId) {

        Map<String, Object> stats = notificationService.getNotificationStats(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("userId", userId);
        response.put("statistics", stats);

        return ResponseEntity.ok(response);
    }

    // Mark a single notification as read
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Map<String, Object>> markAsRead(
            @PathVariable Long notificationId,
            @RequestParam Long userId) {

        notificationService.markAsRead(userId, notificationId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Notification marked as read");
        response.put("notificationId", notificationId);

        return ResponseEntity.ok(response);
    }

    // Mark all notifications as read
    @PutMapping("/users/{userId}/read-all")
    public ResponseEntity<Map<String, Object>> markAllAsRead(@PathVariable Long userId) {

        notificationService.markAllAsRead(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "All notifications marked as read");
        response.put("userId", userId);

        return ResponseEntity.ok(response);
    }

    // Batch mark notifications as read
    @PutMapping("/batch-read")
    public ResponseEntity<Map<String, Object>> batchMarkAsRead(
            @RequestParam Long userId,
            @RequestBody List<Long> notificationIds) {

        notificationService.batchMarkAsRead(userId, notificationIds);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Batch marked " + notificationIds.size() + " notifications as read");

        return ResponseEntity.ok(response);
    }

    // Delete a notification
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Map<String, Object>> deleteNotification(
            @PathVariable Long notificationId,
            @RequestParam Long userId) {

        notificationService.deleteNotification(userId, notificationId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Notification deleted successfully");
        response.put("notificationId", notificationId);

        return ResponseEntity.ok(response);
    }

    // Delete all notifications for a user
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Map<String, Object>> deleteAllUserNotifications(@PathVariable Long userId) {

        notificationService.deleteAllUserNotifications(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "All notifications deleted for user");
        response.put("userId", userId);

        return ResponseEntity.ok(response);
    }

    // Delete notifications by entity ID
    @DeleteMapping("/entity/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteNotificationsByEntityId(@PathVariable Long entityId) {

        notificationService.deleteNotificationsByEntityId(entityId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Notifications for entity deleted successfully");
        response.put("entityId", entityId);

        return ResponseEntity.ok(response);
    }

    // Check if user has unread notifications
    @GetMapping("/users/{userId}/has-unread")
    public ResponseEntity<Map<String, Object>> hasUnreadNotifications(@PathVariable Long userId) {

        boolean hasUnread = notificationService.hasUnreadNotifications(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("userId", userId);
        response.put("hasUnread", hasUnread);

        return ResponseEntity.ok(response);
    }

    // Get filtered notifications
    @GetMapping("/users/{userId}/filter")
    public ResponseEntity<Map<String, Object>> getFilteredNotifications(
            @PathVariable Long userId,
            @RequestParam(required = false) NotificationType type,
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<Notification> notifications = notificationService.getFilteredNotifications(userId, type, isRead, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", notifications);
        response.put("filters", Map.of("type", type, "isRead", isRead));
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalCount", notifications.size());

        return ResponseEntity.ok(response);
    }

    // Clean up old notifications (Admin endpoint)
    @DeleteMapping("/admin/cleanup")
    public ResponseEntity<Map<String, Object>> cleanupOldNotifications(@RequestParam(defaultValue = "30") int daysOld) {

        notificationService.cleanupOldNotifications(daysOld);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Cleaned up notifications older than " + daysOld + " days");

        return ResponseEntity.ok(response);
    }
}