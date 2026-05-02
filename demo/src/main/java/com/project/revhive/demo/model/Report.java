package com.project.revhive.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reporterUsername;

    private String targetType; // post or user

    private Long targetId;

    private String reason;

    private LocalDateTime createdAt;
}