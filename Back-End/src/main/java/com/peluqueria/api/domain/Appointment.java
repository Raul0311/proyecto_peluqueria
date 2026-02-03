package com.peluqueria.api.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class Appointment.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    
    /** The id. */
    private Long id;
    
    /** The user id. */
    private Long userId;
    
    /** The client full name. */
    private String clientFullName;
    
    /** The client email. */
    private String clientEmail;
    
    /** The client phone. */
    private String clientPhone;
    
    /** The appointment date. */
    private LocalDateTime appointmentDate;
    
    /** The service name. */
    private String serviceName;
    
    /** The status. */
    private String status;
    
    /** The duration minutes. */
    private Integer durationMinutes;
}