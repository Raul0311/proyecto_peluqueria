package com.peluqueria.api.adapter.out.persistence.roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class RoleEntity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class RoleEntity {
    
    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** The role name. */
    @Column(name = "role_name", unique = true, nullable = false)
    private String roleName;

    /** The description. */
    @Column(name = "description")
    private String description;
}