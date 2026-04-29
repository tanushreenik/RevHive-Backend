package com.project.revhive.demo.controller.admin;

import com.project.revhive.demo.dto.response.AnalyticsResponse;
import com.project.revhive.demo.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping
    public AnalyticsResponse getAnalytics() {
        return analyticsService.getAnalytics();
    }
}
