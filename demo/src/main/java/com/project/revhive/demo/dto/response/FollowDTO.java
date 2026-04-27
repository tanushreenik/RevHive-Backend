package com.project.revhive.demo.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowDTO {
    private Long id;
    private UserDTO follower;
    private UserDTO following;
    private String createdAt;
}