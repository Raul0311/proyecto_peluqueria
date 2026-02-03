package com.peluqueria.api.adapter.out.persistence.appointment;

import java.time.LocalDateTime;

import com.peluqueria.api.adapter.out.persistence.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class AppointmentEntity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class AppointmentEntity {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The user id. */
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;
    
    /** The client full name. */
    @Column(name = "client_full_name")
    private String clientFullName;
    
    /** The client email. */
    @Column(name = "client_email")
    private String clientEmail;
    
    /** The client phone. */
    @Column(name = "client_phone")
    private String clientPhone;

    /** The service name. */
    @Column(name = "service_name", nullable = false)
    private String serviceName;

    /** The appointment date. */
    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    /** The status. */
    @Column(name="status", nullable = false)
    private String status; // PENDING, CONFIRMED, CANCELLED
    
    /** The updated at. */
    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** The user. */
    // Si quieres traer el nombre del cliente directamente desde la DB
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private UserEntity user;
    
    /** The duration minutes. */
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
}