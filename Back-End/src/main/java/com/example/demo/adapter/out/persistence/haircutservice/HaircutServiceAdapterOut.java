package com.example.demo.adapter.out.persistence.haircutservice;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.application.ports.out.HaircutServicePortOut;
import com.example.demo.domain.HaircutService;

import lombok.RequiredArgsConstructor;

/**
 * The Class HaircutServiceAdapterOut.
 */
@Component
@RequiredArgsConstructor
public class HaircutServiceAdapterOut implements HaircutServicePortOut {

    /** The repository. */
    private final HaircutServiceRepository repository;
    
    /** The mapper. */
    private final HaircutServiceMapper mapper;

    /**
     * Find all active.
     *
     * @return the list
     */
    @Override
    public List<HaircutService> findAllActive() {
        return repository.findByActiveTrue().stream()
                .map(mapper::toDomain)
                .toList();
    }

    /**
     * Persist.
     *
     * @param s the s
     * @return the haircut service
     */
    @Override
    public HaircutService persist(HaircutService s) {
        HaircutServiceEntity entity = mapper.toEntity(s);
        HaircutServiceEntity savedEntity = repository.save(entity);
        
        return mapper.toDomain(savedEntity);
    }

    /**
     * Logical delete.
     *
     * @param id the id
     */
    @Override
    public void logicalDelete(Long id) {
        repository.findById(id).ifPresent(e -> {
            e.setActive(false);
            repository.save(e);
        });
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
        return repository.findById(id)
                .map(existingEntity -> {
                    existingEntity.setName(s.getName());
                    existingEntity.setPrice(s.getPrice());
                    existingEntity.setDurationMinutes(s.getDurationMinutes());
                    
                    HaircutServiceEntity updatedEntity = repository.save(existingEntity);
                    return mapper.toDomain(updatedEntity);
                })
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));
    }

	@Override
	public HaircutService findByName(String name) {
        HaircutServiceEntity entity = repository.findByName(name);
        
        if (entity == null) {
            return null;
        }

        return mapper.toDomain(entity);
	}
}