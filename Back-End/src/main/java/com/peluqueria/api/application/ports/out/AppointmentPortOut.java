package com.peluqueria.api.application.ports.out;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.peluqueria.api.domain.Appointment;
import com.peluqueria.api.domain.dto.TimeRangeDto;

/**
 * The Interface AppointmentPortOut.
 */
public interface AppointmentPortOut {
    
    /**
     * Find by user id.
     *
     * @param userId the user id
     * @return the list
     */
    List<Appointment> findByUserId(Long userId);
    
    /**
     * Find by id.
     *
     * @param id the id
     * @return the appointment
     */
    Appointment findById(Long id);
    
    /**
     * Find all.
     *
     * @return the list
     */
    List<Appointment> findAll();
    
    /**
     * Exists by date.
     *
     * @param dateTime the date time
     * @return true, if successful
     */
    boolean existsByDate(LocalDateTime dateTime);
    
    /**
     * Save.
     *
     * @param appointment the appointment
     * @return the appointment
     */
    Appointment save(Appointment appointment);
    
    /**
     * Update status.
     *
     * @param id the id
     * @param status the status
     */
    void updateStatus(Long id, String status);
    
    /**
     * Exists overlapping.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return true, if successful
     */
    boolean existsOverlapping(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Gets the occupied slots.
     *
     * @param date the date
     * @return the occupied slots
     */
    public List<TimeRangeDto> getOccupiedSlots(LocalDate date);
}