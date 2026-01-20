package com.example.demo.adapter.out.persistence.user;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.adapter.out.persistence.roles.RoleEntity;
import com.example.demo.adapter.out.persistence.roles.UserRolesEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class UserEntity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** The username. */
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    
    /** The passw. */
    @Column(name = "passw", nullable = false)
    private String passw;

    /** The name. */
    @Column(name = "name", nullable = false)
    private String name;
    
    /** The lastname 1. */
    @Column(name = "lastname1", nullable = false)
    private String lastname1;
    
    /** The lastname 2. */
    @Column(name = "lastname2")
    private String lastname2;

    /** The city. */
    @Column(name = "city")
    private String city;
    
    /** The country. */
    @Column(name = "country")
    private String country;

    /** The street. */
    @Column(name = "street")
    private String street;
    
    /** The number address. */
    @Column(name = "number_address")
    private String numberAddress;
    
    /** The apartment. */
    @Column(name = "apartment")
    private String apartment;

    /** The zip code. */
    @Column(name = "zip_code")
    private String zipCode;

    /** The phone. */
    @Column(name = "phone", nullable = false)
    private String phone;
    
    /** The email. */
    @Column(name = "email", nullable = false)
    private String email;

    /** The enabled. */
    @Column(name = "enabled")
    private Boolean enabled;

    /** The last login. */
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    /** The created at. */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** The updated at. */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /** The user roles. */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true) // 'user' es el campo ManyToOne en UserRolesEntity
    private List<UserRolesEntity> userRoles;
    
    /**
     * Gets the roles.
     *
     * @return the roles
     */
    public List<RoleEntity> getRoles() {
        if (this.userRoles == null) return List.of();
        // Mapea la lista de UserRolesEntity a la lista de RoleEntity
        return this.userRoles.stream()
                .map(UserRolesEntity::getRole)
                .toList();
    }
}
