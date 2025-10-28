package com.transport.entity.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.transport.entity.base.BaseEntity;
import com.transport.enums.UserRole;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_username", columnList = "username")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "full_name")
    private String fullName;
    
    @Column(name = "id_number")
    private String idNumber;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "avatar_url")
    private String avatarUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Driver driver;

    @Builder.Default
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Trip> createdTrips = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "approvedByUser", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Trip> approvedTrips = new ArrayList<>();

    // @ManyToMany(fetch = FetchType.EAGER)
    // @JoinTable(
    //     name = "user_permissions", 
    //     joinColumns = @JoinColumn(name = "user_id"), 
    //     inverseJoinColumns = @JoinColumn(name = "permission_id")
    // )
    // @Builder.Default
    // private Set<Permission> permissions = new HashSet<>();
    @Builder.Default
    @ManyToMany
    @JoinTable(
        name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_name") 
    )
    private Set<Role> roles = new HashSet<>();
}