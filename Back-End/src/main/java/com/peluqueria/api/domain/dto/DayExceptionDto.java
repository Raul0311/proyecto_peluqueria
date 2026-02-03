package com.peluqueria.api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class DayExceptionDto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayExceptionDto {
    
    /** The date. */
    private String date;
    
    /** The start time. */
    private String startTime;
    
    /** The end time. */
    private String endTime;
}