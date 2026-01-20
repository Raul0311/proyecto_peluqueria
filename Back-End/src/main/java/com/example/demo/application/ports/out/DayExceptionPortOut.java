package com.example.demo.application.ports.out;

import java.util.List;

import com.example.demo.domain.dto.DayExceptionDto;

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