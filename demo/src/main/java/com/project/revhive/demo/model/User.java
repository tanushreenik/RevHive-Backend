package com.project.revhive.demo.model;
import com.project.revhive.demo.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false,unique = true)
    private String username;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String bio;
    private String avatarUrl;

    private int followersCount=0;
    private int followingCount=0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean isActive=true;

    @Column(updatable = false)
    private Long createdAt;

    private Long updatedAt;

    @PrePersist
    protected void onCreate()
    {
        this.createdAt=System.currentTimeMillis();
    }

    @PreUpdate
    protected void onUpdate()
    {
        this.updatedAt=System.currentTimeMillis();
    }

}
