package com.example.demo.application.ports.in;

import java.util.List;

import com.example.demo.domain.dto.DayExceptionDto;

/**
 * The Interface DayExceptionPortIn.
 */
public interface DayExceptionPortIn {
    
    /**
     * Find all exceptions.
     *
     * @return the list
     */
    List<DayExceptionDto> findAllExceptions();
    
    /**
     * Save exception.
     *
     * @param dto the dto
     */
    void saveException(DayExceptionDto dto);
}