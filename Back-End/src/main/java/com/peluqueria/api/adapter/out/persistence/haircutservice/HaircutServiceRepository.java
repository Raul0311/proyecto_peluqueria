package com.peluqueria.api.adapter.out.persistence.haircutservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * The Interface HaircutServiceRepository.
 */
public interface HaircutServiceRepository extends JpaRepository<HaircutServiceEntity, Long> {
    
    /**
     * Find by active true.
     *
     * @return the list
     */
    List<HaircutServiceEntity> findByActiveTrue();
    
    @Query(value = "SELECT * FROM haircut_services s WHERE s.name = ?1", nativeQuery = true)
    HaircutServiceEntity findByName(String name);
}