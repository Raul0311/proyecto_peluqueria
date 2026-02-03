package com.peluqueria.api.adapter.out.persistence.dayexception;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class DayExceptionEntity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "day_exceptions")
public class DayExceptionEntity {
    
    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** The date. */
    @Column(name = "date", unique = true)
    private LocalDate date;
    
    /** The start time. */
    @Column(name = "startTime")
    private LocalTime startTime;
    
    /** The end time. */
    @Column(name = "endTime")
    private LocalTime endTime;
}