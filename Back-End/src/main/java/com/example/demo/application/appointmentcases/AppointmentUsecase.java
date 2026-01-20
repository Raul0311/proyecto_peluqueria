package com.example.demo.application.appointmentcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.adapter.out.persistence.user.UserEntity;
import com.example.demo.application.ports.in.AppointmentPortIn;
import com.example.demo.application.ports.out.AppointmentPortOut;
import com.example.demo.application.ports.out.EmailPortOut;
import com.example.demo.application.ports.out.HaircutServicePortOut;
import com.example.demo.application.ports.out.UserPortOut;
import com.example.demo.domain.Appointment;
import com.example.demo.domain.HaircutService;
import com.example.demo.domain.dto.TimeRangeDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class AppointmentUsecase.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentUsecase implements AppointmentPortIn {

    /** The appointment port out. */
    private final AppointmentPortOut appointmentPortOut;
    
    /** The email port out. */
    private final EmailPortOut emailPortOut;
    
    /** The user port out. */
    private final UserPortOut userPortOut;
    
    /** The haircut service port out. */
    private final HaircutServicePortOut haircutServicePortOut;

    /**
     * Gets the appointments.
     *
     * @param userId the user id
     * @param isAdmin the is admin
     * @return the appointments
     */
    @Override
    public List<Appointment> getAppointments(Long userId, boolean isAdmin) {
        if (isAdmin) {
            return appointmentPortOut.findAll();
        }
        
        // No está logueado (petición pública del calendario)
        if (userId == null) {
            return appointmentPortOut.findAll().stream()
                    .map(apt -> {
                        Appointment anonymousApt = new Appointment();
                        anonymousApt.setAppointmentDate(apt.getAppointmentDate());
                        anonymousApt.setServiceName(apt.getServiceName());
                        // NO seteamos clientFullName, email ni phone por seguridad
                        return anonymousApt;
                    })
                    .toList();
        }

        // Usuario normal logueado -> Ve solo sus citas
        return appointmentPortOut.findByUserId(userId);
    }

    /**
     * Creates the.
     *
     * @param appointment the appointment
     * @return the appointment
     */
    @Override
    @Transactional
    public Appointment create(Appointment appointment) {
        // 1. Limpieza de tiempos
        LocalDateTime startDate = appointment.getAppointmentDate().withSecond(0).withNano(0);
        appointment.setAppointmentDate(startDate);
        
        LocalDateTime now = LocalDateTime.now();

        // 2. Validaciones básicas de fecha
        if (startDate.isBefore(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes reservar en una fecha pasada");
        }
        if (startDate.isAfter(now.plusMonths(1))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se puede reservar con un mes de antelación.");
        }
        
        // Si el userId está presente (usuario registrado), verificamos sus citas del día
        if (appointment.getUserId() != null) {
            LocalDate dateOnly = startDate.toLocalDate();
            List<Appointment> userAppointments = appointmentPortOut.findByUserId(appointment.getUserId());
            
            boolean alreadyHasAptToday = userAppointments.stream()
                .anyMatch(apt -> apt.getAppointmentDate().toLocalDate().equals(dateOnly) 
                                 && !"CANCELLED".equals(apt.getStatus()));

            if (alreadyHasAptToday) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Ya tienes una cita reservada para este día.");
            }
        }

        // 3. Obtener duración del servicio y calcular fecha fin
        HaircutService servicio = haircutServicePortOut.findByName(appointment.getServiceName());
        
        // Calculamos el final de la cita según la duración del servicio
        LocalDateTime endDate = startDate.plusMinutes(servicio.getDurationMinutes());

        // 4. Validar solapamiento por RANGO
        // Ya no usamos existsByDate, sino una función que verifique si chocan
        if (appointmentPortOut.existsOverlapping(startDate, endDate)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                "El horario colisiona con otra cita existente.");
        }

        // --- LÓGICA DE ASIGNACIÓN (Tu código se mantiene igual aquí) ---
        if (appointment.getUserId() != null) {
            UserEntity user = userPortOut.findById(appointment.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
            appointment.setUserId(user.getId());
            appointment.setClientFullName(user.getName());
            appointment.setClientEmail(user.getEmail());
            appointment.setClientPhone(user.getPhone());
        }

        appointment.setStatus("CONFIRMED");
        Appointment saved = appointmentPortOut.save(appointment);
        enviarNotificaciones(saved);

        return saved;
    }
    
    /**
     * Enviar notificaciones.
     *
     * @param saved the saved
     */
    private void enviarNotificaciones(Appointment saved) {
        // 1. Notificación al ADMIN
        try {
            emailPortOut.sendAdminNotification(
                "raulrrf03@gmail.com", 
                saved.getClientFullName(), 
                saved.getServiceName(), 
                saved.getAppointmentDate().toString()
            );
        } catch (Exception e) {
            log.error("Error notificando al admin: {}", e.getMessage());
        }

        // 2. Confirmación al CLIENTE
        if (saved.getClientEmail() != null && !saved.getClientEmail().isEmpty()) {
            try {
                emailPortOut.sendAppointmentNotification(
                    saved.getClientEmail(),
                    saved.getClientFullName(),
                    saved.getServiceName(),
                    saved.getAppointmentDate().toString(),
                    saved.getStatus()
                );
            } catch (Exception e) {
                log.error("Error enviando confirmación al cliente: {}", e.getMessage());
            }
        }
    }

    /**
     * Update status.
     *
     * @param id the id
     * @param status the status
     * @param isAdmin the is admin
     */
    @Override
    public void updateStatus(Long id, String status, boolean isAdmin) {
        // 1. Buscamos la cita
        Appointment apt = appointmentPortOut.findById(id);
        
        // 2. VALIDACIÓN: Si es una cancelación y no es admin, comprobamos el tiempo
        if ("CANCELLED".equals(status) && !isAdmin) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime limit = apt.getAppointmentDate().minusHours(1);
            
            if (now.isAfter(limit)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, 
                    "No se puede cancelar con menos de 1 hora de antelación."
                );
            }
        }

        // 3. Cambiamos el estado y guardamos
        apt.setStatus(status);
        appointmentPortOut.save(apt);

        // 4. Enviamos el correo
        emailPortOut.sendAppointmentNotification(
            apt.getClientEmail(), 
            apt.getClientFullName(), 
            apt.getServiceName(), 
            apt.getAppointmentDate().toString(), 
            status
        );
    }
    
    /**
     * Gets the occupied slots.
     *
     * @param date the date
     * @return the occupied slots
     */
    public List<TimeRangeDto> getOccupiedSlots(LocalDate date) {
    	return appointmentPortOut.getOccupiedSlots(date);
    }
}