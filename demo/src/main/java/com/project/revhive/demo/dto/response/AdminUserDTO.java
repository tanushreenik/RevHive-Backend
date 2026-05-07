
package com.project.revhive.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUserDTO {

    private Long id;
    private String username;
    private String email;
    private String avatarUrl;
    private boolean active;
    private boolean premium;
    private int followersCount;
    private int followingCount;
    private String role;
}
