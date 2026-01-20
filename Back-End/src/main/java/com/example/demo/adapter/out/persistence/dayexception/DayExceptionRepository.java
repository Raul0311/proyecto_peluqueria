package com.example.demo.adapter.out.persistence.dayexception;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface DayExceptionRepository.
 */
@Repository
public interface DayExceptionRepository extends JpaRepository<DayExceptionEntity, Long> {
    
    /**
     * Find by date.
     *
     * @param date the date
     * @return the optional
     */
    Optional<DayExceptionEntity> findByDate(LocalDate date);
    
    /**
     * Delete by date before.
     *
     * @param date the date
     */
    void deleteByDateBefore(LocalDate date);
}