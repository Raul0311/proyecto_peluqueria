package com.peluqueria.api.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class ClosedDay.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClosedDay {
    
    /** The id. */
    private Long id;
    
    /** The date. */
    private LocalDate date;
    
    /** The reason. */
    private String reason;
}