package com.peluqueria.api.application.closeddaycases;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.peluqueria.api.application.ports.in.ClosedDayPortIn;
import com.peluqueria.api.application.ports.out.ClosedDayPortOut;
import com.peluqueria.api.domain.ClosedDay;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * The Class ClosedDayUsecase.
 */
@Service
@RequiredArgsConstructor
public class ClosedDayUsecase implements ClosedDayPortIn {

    /** The closed day port out. */
    private final ClosedDayPortOut closedDayPortOut;

    /**
     * Find all.
     *
     * @return the list
     */
    @Override
    public List<ClosedDay> findAll() {
        return closedDayPortOut.findAll();
    }

    /**
     * Toggle day.
     *
     * @param date the date
     */
    @Override
    @Transactional
    public void toggleDay(LocalDate date) {
        Optional<ClosedDay> existing = closedDayPortOut.findByDate(date);

        if (existing.isPresent()) {
            closedDayPortOut.delete(existing.get().getId());
        } else {
            ClosedDay newDay = new ClosedDay();
            newDay.setDate(date);
            newDay.setReason("Cerrado por el administrador");
            closedDayPortOut.save(newDay);
        }
    }
}