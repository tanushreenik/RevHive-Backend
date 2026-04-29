package com.project.revhive.demo.dto.response;

import com.project.revhive.demo.dto.request.UserGrowthDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnalyticsResponse {
    private long totalUsers;
    private long activeUsers;
    private  long newUsers;

    private  long dau;
    private long mau;

    private List<UserGrowthDTO> growthDTOList;
}
