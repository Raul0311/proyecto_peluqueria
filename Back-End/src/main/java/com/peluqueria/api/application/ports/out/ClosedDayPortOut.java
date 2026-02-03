package com.peluqueria.api.application.ports.out;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.peluqueria.api.domain.ClosedDay;

public interface ClosedDayPortOut {
    List<ClosedDay> findAll();
    Optional<ClosedDay> findByDate(LocalDate date);
    void save(ClosedDay closedDay);
    void delete(Long id);
}