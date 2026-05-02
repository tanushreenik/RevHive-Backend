
        package com.project.revhive.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {
    @NotNull(message = "Post ID is required")
    private Long postId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Comment content cannot be empty")
    private String content;
}
