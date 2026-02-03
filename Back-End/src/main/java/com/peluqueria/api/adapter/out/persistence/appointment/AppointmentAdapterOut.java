package com.peluqueria.api.adapter.out.persistence.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.peluqueria.api.adapter.out.persistence.user.UserRepository;
import com.peluqueria.api.application.ports.out.AppointmentPortOut;
import com.peluqueria.api.domain.Appointment;
import com.peluqueria.api.domain.dto.TimeRangeDto;

import lombok.RequiredArgsConstructor;

/**
 * The Class AppointmentAdapterOut.
 */
@Component
@RequiredArgsConstructor
public class AppointmentAdapterOut implements AppointmentPortOut {

    /** The repository. */
    private final AppointmentRepository repository;
    
    /** The mapper. */
    private final AppointmentMapper mapper;
    
    /** The user repository. */
    private final UserRepository userRepository;

    /**
     * Find by user id.
     *
     * @param userId the user id
     * @return the list
     */
    @Override
    public List<Appointment> findByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    /**
     * Find by id.
     *
     * @param id the id
     * @return the appointment
     */
    @Override
    public Appointment findById(Long id) {
    	return repository.findById(id)
    	        .map(mapper::toDomain) 
    	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));
    }

    /**
     * Find all.
     *
     * @return the list
     */
    @Override
    public List<Appointment> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    /**
     * Exists by date.
     *
     * @param dateTime the date time
     * @return true, if successful
     */
    @Override
    public boolean existsByDate(LocalDateTime dateTime) {
    	return repository.existsByAppointmentDateAndStatusNot(dateTime, "CANCELLED");
    }

    /**
     * Save.
     *
     * @param appointment the appointment
     * @return the appointment
     */
    @Override
    public Appointment save(Appointment appointment) {
        // 1. Convertimos a entidad
        AppointmentEntity entity = mapper.toEntity(appointment);
        
        // 2. Buscamos el UserEntity y lo vinculamos
        // Esto soluciona el problema de que USER sea null
        userRepository.findById(appointment.getUserId()).ifPresent(entity::setUser);
        
        // 3. Guardamos
        AppointmentEntity savedEntity = repository.save(entity);
        
        // 4. Mapeamos directamente al dominio (ahora entity.getUser() ya tiene datos)
        return mapper.toDomain(savedEntity);
    }

    /**
     * Update status.
     *
     * @param id the id
     * @param status the status
     */
    @Override
    @Transactional
    public void updateStatus(Long id, String status) {
        repository.findById(id).ifPresent(entity -> {
            entity.setStatus(status);
            repository.save(entity);
        });
    }

	/**
	 * Exists overlapping.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @return true, if successful
	 */
	@Override
	public boolean existsOverlapping(LocalDateTime startDate, LocalDateTime endDate) {
		int ifExists = repository.existsOverlapping(startDate, endDate);
		if(ifExists == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets the occupied slots.
	 *
	 * @param date the date
	 * @return the occupied slots
	 */
	@Override
	public List<TimeRangeDto> getOccupiedSlots(LocalDate date) {
	    // 1. Buscamos todas las citas confirmadas para ese día
	    LocalDateTime startOfDay = date.atStartOfDay();
	    LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

	    List<AppointmentEntity> appointments = repository.findByAppointmentDateBetweenAndStatusNot(
	        startOfDay, endOfDay, "CANCELLED"
	    );

	    // 2. Mapeamos a un objeto simple de inicio y fin
	    return appointments.stream()
	            .map(app -> new TimeRangeDto(
	                app.getAppointmentDate().toLocalTime(),
	                app.getAppointmentDate().plusMinutes(app.getDurationMinutes()).toLocalTime()
	            ))
	            .toList();
	}
}