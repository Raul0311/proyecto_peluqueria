package com.peluqueria.api.application.ports.in;

import java.time.LocalDate;
import java.util.List;

import com.peluqueria.api.domain.ClosedDay;

/**
 * The Interface ClosedDayPortIn.
 */
public interface ClosedDayPortIn {
    
    /**
     * Find all.
     *
     * @return the list
     */
    List<ClosedDay> findAll();
    
    /**
     * Toggle day.
     *
     * @param date the date
     */
    void toggleDay(LocalDate date);
}