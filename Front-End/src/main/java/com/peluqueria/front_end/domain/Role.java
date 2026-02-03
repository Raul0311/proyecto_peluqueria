package com.peluqueria.front_end.domain;

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