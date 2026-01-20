package com.example.demo.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.application.ports.in.HaircutServicePortIn;
import com.example.demo.application.ports.out.HaircutServicePortOut;
import com.example.demo.domain.HaircutService;

import lombok.RequiredArgsConstructor;

/**
 * The Class HaircutServiceUsecase.
 */
@Service
@RequiredArgsConstructor
public class HaircutServiceUsecase implements HaircutServicePortIn {
    
    /** The port out. */
    private final HaircutServicePortOut portOut;

    /**
     * Gets the all available.
     *
     * @return the all available
     */
    @Override
    public List<HaircutService> getAllAvailable() {
    	
    	return portOut.findAllActive();
    }

    /**
     * Save.
     *
     * @param s the s
     * @return the haircut service
     */
    @Override
    public HaircutService save(HaircutService s) {
    	
    	return portOut.persist(s); 
    }

    /**
     * Delete.
     *
     * @param id the id
     */
    @Override
    public void delete(Long id) {
    	
    	portOut.logicalDelete(id); 
	}

    /**
     * Update.
     *
     * @param id the id
     * @param s the s
     * @return the haircut service
     */
    @Override
    public HaircutService update(Long id, HaircutService s) { 
        
        return portOut.update(id, s); 
    }
}