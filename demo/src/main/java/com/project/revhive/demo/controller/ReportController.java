package com.project.revhive.demo.controller;

import com.project.revhive.demo.model.Report;
import com.project.revhive.demo.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // USER REPORT
    @PostMapping
    public Report createReport(@RequestBody Map<String, Object> body,
                               Authentication authentication) {

        String username = authentication.getName();

        String targetType = (String) body.get("targetType");
        Long targetId = Long.valueOf(body.get("targetId").toString());
        String reason = (String) body.get("reason");

        return reportService.createReport(username, targetType, targetId, reason);
    }

    // ADMIN VIEW REPORTS
    @GetMapping("/admin")
    public List<Report> getReports() {
        return reportService.getAllReports();
    }
}