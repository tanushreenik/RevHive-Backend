package com.project.revhive.demo.repository;

import com.project.revhive.demo.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}