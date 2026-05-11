package com.project.revhive.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminStatsDTO {

    private long totalUsers;

    private long activeUsers;

    private long blockedUsers;

    private long premiumUsers;

    private long admins;
}
