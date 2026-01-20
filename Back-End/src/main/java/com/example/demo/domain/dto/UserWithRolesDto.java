package com.example.demo.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class UserWithRolesDto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithRolesDto {
    
    /** The id. */
    private Long id;
    
    /** The username. */
    private String username;
    
    /** The roles. */
    private List<RoleDto> roles;
}

