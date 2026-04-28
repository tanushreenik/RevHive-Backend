package com.project.revhive.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

<<<<<<< HEAD
//@Entity
//@Table(name = "follows")
=======

>>>>>>> 96396ec45f12e792c55865a5cd593bf97939a8d8
@Data
@Entity
@Table(name = "follows",
        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following_id")
    private User following;

    private LocalDateTime createdAt;

    private boolean accepted = true;
}

<<<<<<< HEAD
=======
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "follower_id", nullable = false)
//    private User follower;  // User who follows
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "following_id", nullable = false)
//    private User following;  // User being followed
//
//    @Column(nullable = false, updatable = false)
//    private Long createdAt;
//
//    @PrePersist
//    protected void onCreate() {
//        this.createdAt = System.currentTimeMillis();
//    }
}
>>>>>>> 96396ec45f12e792c55865a5cd593bf97939a8d8
