package com.example.demo.adapter.out.persistence.appointment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The Interface AppointmentRepository.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    
    /**
     * Find by user id custom.
     *
     * @param userId the user id
     * @return the list
     */
    List<AppointmentEntity> findByUserId(Long userId);
    
    /**
     * Find by id with user.
     *
     * @param id the id
     * @return the appointment entity
     */
    @Query(value = "SELECT a FROM AppointmentEntity a JOIN FETCH a.user WHERE a.id = :id")
    AppointmentEntity findByIdWithUser(Long id);

    /**
     * Update status.
     *
     * @param id the id
     * @param status the status
     */
    @Modifying
    @Query(value = "UPDATE appointments SET status = ?2 WHERE id = ?1", nativeQuery = true)
    void updateStatus(Long id, String status);
    
    /**
     * Exists by appointment date.
     *
     * @param date the date
     * @return true, if successful
     */
    boolean existsByAppointmentDate(LocalDateTime date);
    
    /**
     * Exists by appointment date and status not.
     *
     * @param date the date
     * @param status the status
     * @return true, if successful
     */
    // Para que la hora quede libre: el validador debe ignorar las canceladas
    boolean existsByAppointmentDateAndStatusNot(LocalDateTime date, String status);

    // Para el borrado a las 24h
    /**
     * Find by status and updated at before.
     *
     * @param status the status
     * @param time the time
     * @return the list
     */
    List<AppointmentEntity> findByStatusAndUpdatedAtBefore(String status, LocalDateTime time);
    
    /**
     * Find by status and appointment date before.
     *
     * @param status the status
     * @param time the time
     * @return the list
     */
    List<AppointmentEntity> findByStatusAndAppointmentDateBefore(String status, LocalDateTime time);
    
    /**
     * Find by status and appointment date between.
     *
     * @param status the status
     * @param start the start
     * @param end the end
     * @return the list
     */
    List<AppointmentEntity> findByStatusAndAppointmentDateBetween(String status, LocalDateTime start, LocalDateTime end);
    
    /**
     * Exists overlapping.
     *
     * @param newStart the new start
     * @param newEnd the new end
     * @return true, if successful
     */
    @Query(value = "SELECT COUNT(*) " +
            "FROM appointments a " +
            "WHERE a.status <> 'CANCELLED' " +
            "AND (:newStart < DATE_ADD(a.appointment_date, INTERVAL a.duration_minutes MINUTE)) " +
            "AND (:newEnd > a.appointment_date)", 
    nativeQuery = true)
    int existsOverlapping(@Param("newStart") LocalDateTime newStart, @Param("newEnd") LocalDateTime newEnd);
    
    /**
     * Find by appointment date between and status not.
     *
     * @param start the start
     * @param end the end
     * @param status the status
     * @return the list
     */
    List<AppointmentEntity> findByAppointmentDateBetweenAndStatusNot(
    	    LocalDateTime start, LocalDateTime end, String status
    	);
}