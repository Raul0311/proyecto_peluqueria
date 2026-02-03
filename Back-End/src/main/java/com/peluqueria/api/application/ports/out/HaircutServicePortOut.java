package com.peluqueria.api.application.ports.out;

import java.util.List;

import com.peluqueria.api.domain.HaircutService;

/**
 * The Interface HaircutServicePortOut.
 */
public interface HaircutServicePortOut {
    
    /**
     * Find all active.
     *
     * @return the list
     */
    List<HaircutService> findAllActive();
    
    /**
     * Persist.
     *
     * @param s the s
     * @return the haircut service
     */
    HaircutService persist(HaircutService s);
    
    /**
     * Logical delete.
     *
     * @param id the id
     */
    void logicalDelete(Long id);
    
    /**
     * Update.
     *
     * @param id the id
     * @param s the s
     * @return the haircut service
     */
    HaircutService update(Long id, HaircutService s);
    
    HaircutService findByName(String name);
}