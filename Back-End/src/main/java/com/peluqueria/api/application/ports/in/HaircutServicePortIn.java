package com.peluqueria.api.application.ports.in;

import java.util.List;

import com.peluqueria.api.domain.HaircutService;

/**
 * The Interface HaircutServicePortIn.
 */
public interface HaircutServicePortIn {
    
    /**
     * Gets the all available.
     *
     * @return the all available
     */
    List<HaircutService> getAllAvailable();
    
    /**
     * Save.
     *
     * @param service the service
     * @return the haircut service
     */
    HaircutService save(HaircutService service);
    
    /**
     * Delete.
     *
     * @param id the id
     */
    void delete(Long id);
    
    /**
     * Update.
     *
     * @param id the id
     * @param service the service
     * @return the haircut service
     */
    HaircutService update(Long id, HaircutService service);
}