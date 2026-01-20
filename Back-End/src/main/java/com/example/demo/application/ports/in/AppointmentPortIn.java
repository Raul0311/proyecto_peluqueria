package com.example.demo.application.ports.in;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.domain.Appointment;
import com.example.demo.domain.dto.TimeRangeDto;

/**
 * The Interface AppointmentPortIn.
 */
public interface AppointmentPortIn {
    
    /**
     * Gets the appointments.
     *
     * @param userId the user id
     * @param isAdmin the is admin
     * @return the appointments
     */
    List<Appointment> getAppointments(Long userId, boolean isAdmin);
    
    /**
     * Creates the.
     *
     * @param appointment the appointment
     * @return the appointment
     */
    Appointment create(Appointment appointment);
    
    /**
     * Update status.
     *
     * @param id the id
     * @param status the status
     * @param isAdmin the is admin
     */
    void updateStatus(Long id, String status, boolean isAdmin);
    
    /**
     * Gets the occupied slots.
     *
     * @param date the date
     * @return the occupied slots
     */
    public List<TimeRangeDto> getOccupiedSlots(LocalDate date);
}