package com.project.revhive.demo.model;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;


@Entity
@Table(name= "follows", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"follower_id", "following_id"})
}, indexes = {
        @Index(name= "idx_follower", columnList = "follower_id"),
        @Index(name = "idx_following", columnList = "following_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "following_id", nullable = false)
    private User following;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private boolean accepted = true;

}
