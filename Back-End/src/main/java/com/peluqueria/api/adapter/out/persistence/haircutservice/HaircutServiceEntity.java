package com.peluqueria.api.adapter.out.persistence.haircutservice;

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
 * The Class HaircutServiceEntity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "haircut_services")
public class HaircutServiceEntity {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The name. */
    @Column(name = "name", nullable = false)
    private String name;

    /** The price. */
    @Column(name = "price", nullable = false)
    private Double price;

    /** The duration minutes. */
    // Podrías añadir duración en minutos si lo necesitas en el futuro
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes = 30; 

    /** The active. */
    private boolean active = true;
}