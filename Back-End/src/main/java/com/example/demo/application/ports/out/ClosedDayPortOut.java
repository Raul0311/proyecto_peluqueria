package com.example.demo.application.ports.out;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.demo.domain.ClosedDay;

public interface ClosedDayPortOut {
    List<ClosedDay> findAll();
    Optional<ClosedDay> findByDate(LocalDate date);
    void save(ClosedDay closedDay);
    void delete(Long id);
}