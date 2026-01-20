package com.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class UserSimpleDto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSimpleDto {
    
    /** The id. */
    private Long id;
    
    /** The full name. */
    private String username;
    
    /** The email. */
    private String email;
}