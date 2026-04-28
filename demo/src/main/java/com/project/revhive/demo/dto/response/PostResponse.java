package com.project.revhive.demo.dto.response;

import com.project.revhive.demo.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private Long userId;
    private String username;
    private String userAvatarUrl;
    private String content;
    private String imageUrl;
    private String videoUrl;
    private int likeCount;
    private int commentCount;
    private int shareCount;
    private boolean isLikedByCurrentUser;
    private Long createdAt;
    private String formattedCreatedAt;
    private Long updatedAt;

    public static PostResponse fromPost(Post post, boolean isLikedByCurrentUser) {
        return PostResponse.builder()
                .id(post.getId())
                .userId(post.getUser().getId())
                .username(post.getUser().getUsername())
                .userAvatarUrl(post.getUser().getAvatarUrl())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .videoUrl(post.getVideoUrl())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .shareCount(post.getShareCount())
                .isLikedByCurrentUser(isLikedByCurrentUser)
                .createdAt(post.getCreatedAt())
                .formattedCreatedAt(formatTimestamp(post.getCreatedAt()))
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    private static String formatTimestamp(Long timestamp) {
        if (timestamp == null) return null;
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault()
        );
        return dateTime.toString();
    }
}