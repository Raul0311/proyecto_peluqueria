package com.peluqueria.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class HaircutService.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HaircutService {

    /** The id. */
    private Long id;

    /** The name. */
    private String name;

    /** The price. */
    private Double price;

    /** The duration minutes. */
    private Integer durationMinutes = 30; 

    /** The active. */
    private boolean active = true;
}