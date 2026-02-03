package com.peluqueria.api.adapter.out.persistence.appointment;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface ClosedDayRepository.
 */
@Repository
public interface ClosedDayRepository extends JpaRepository<ClosedDayEntity, Long> {
	
	/**
	 * Find by date.
	 *
	 * @param date the date
	 * @return the optional
	 */
	Optional<ClosedDayEntity> findByDate(LocalDate date);
	
	/**
	 * Delete by date before.
	 *
	 * @param date the date
	 */
	void deleteByDateBefore(LocalDate date);
}