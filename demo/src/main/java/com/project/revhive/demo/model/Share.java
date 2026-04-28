package com.project.revhive.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shares", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "postId"})
})
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String postId;

    private LocalDateTime createdAt;

    public Share() {}

    public Long getId() { return id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}