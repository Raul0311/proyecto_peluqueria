package com.peluqueria.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class Role.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
	
	/** The id. */
	private Long id;
    
    /** The name. */
    private String name;
    
    /** The description. */
    private String description;
}