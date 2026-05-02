package com.project.revhive.demo.service;

import com.project.revhive.demo.dto.request.UserGrowthDTO;
import com.project.revhive.demo.dto.response.AnalyticsResponse;
import com.project.revhive.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final UserRepository userRepository;

    public AnalyticsResponse getAnalytics()
    {
        LocalDateTime now =LocalDateTime.now();
        int totalUsers= Math.toIntExact(userRepository.count());
        long activeUsers= userRepository.countByLastActive(now.minusDays(7));
        long newUsers=userRepository.countByCreatedAtAfter(now.minusDays(7));

        long dailyActiveUsers=userRepository.getDailyActiveUsers();
        long monthlyActiveUsers=userRepository.getMonthlyActiveUsers(now.minusDays(30));

        List<UserGrowthDTO> growth = userRepository.getUserGrowth()
                .stream()
                .map(obj -> new UserGrowthDTO(
                        obj[0].toString(),
                        (Long) obj[1]
                ))
                .toList();

        return new AnalyticsResponse(
                totalUsers,
                activeUsers,
                newUsers,
                dailyActiveUsers,
               monthlyActiveUsers,
                growth
        );
    }
}
