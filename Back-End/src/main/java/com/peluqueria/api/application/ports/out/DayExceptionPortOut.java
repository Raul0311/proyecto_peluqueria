package com.peluqueria.api.application.ports.out;

import java.util.List;

import com.peluqueria.api.domain.dto.DayExceptionDto;

/**
 * The Interface DayExceptionPortOut.
 */
public interface DayExceptionPortOut {
    
    /**
     * Gets the all.
     *
     * @return the all
     */
    List<DayExceptionDto> getAll();
    
    /**
     * Upsert.
     *
     * @param dto the dto
     */
    void upsert(DayExceptionDto dto);
}