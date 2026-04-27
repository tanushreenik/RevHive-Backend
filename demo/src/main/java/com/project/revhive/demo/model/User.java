package com.project.revhive.demo.model;


import com.project.revhive.demo.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name="users")
@Data
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

    @Column(nullable = false)
    private String bio;
    private String avatarUrl;



    @Builder.Default
    private int followersCount=0;
    @Builder.Default
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
        this.updatedAt=System.currentTimeMillis();
    }

    @PreUpdate
    protected void onUpdate()
    {
        this.updatedAt=System.currentTimeMillis();
    }




}
