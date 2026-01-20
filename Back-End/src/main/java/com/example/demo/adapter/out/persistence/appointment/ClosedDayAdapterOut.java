package com.example.demo.adapter.out.persistence.appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.demo.application.ports.out.ClosedDayPortOut;
import com.example.demo.domain.ClosedDay;

import lombok.RequiredArgsConstructor;

/**
 * The Class ClosedDayAdapterOut.
 */
@Component
@RequiredArgsConstructor
public class ClosedDayAdapterOut implements ClosedDayPortOut {

    /** The repository. */
    private final ClosedDayRepository repository;

    /**
     * Find all.
     *
     * @return the list
     */
    @Override
    public List<ClosedDay> findAll() {
        return repository.findAll().stream()
                .map(this::mapToDomain)
                .toList();
    }

    /**
     * Find by date.
     *
     * @param date the date
     * @return the optional
     */
    @Override
    public Optional<ClosedDay> findByDate(LocalDate date) {
        return repository.findByDate(date).map(this::mapToDomain);
    }

    /**
     * Save.
     *
     * @param closedDay the closed day
     */
    @Override
    public void save(ClosedDay closedDay) {
        ClosedDayEntity entity = new ClosedDayEntity();
        entity.setDate(closedDay.getDate());
        entity.setReason(closedDay.getReason());
        repository.save(entity);
    }

    /**
     * Delete.
     *
     * @param id the id
     */
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    /**
     * Map to domain.
     *
     * @param entity the entity
     * @return the closed day
     */
    // Mappers sencillos
    private ClosedDay mapToDomain(ClosedDayEntity entity) {
        ClosedDay domain = new ClosedDay();
        domain.setId(entity.getId());
        domain.setDate(entity.getDate());
        domain.setReason(entity.getReason());
        return domain;
    }
}