package com.peluqueria.api.adapter.out.persistence.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.peluqueria.api.adapter.out.persistence.dayexception.DayExceptionRepository;
import com.peluqueria.api.application.ports.out.EmailPortOut;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class AppointmentScheduler.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataScheduler {

    /** The repository. */
    private final AppointmentRepository repository;
    
    /** The closed day repository. */
    private final ClosedDayRepository closedDayRepository;
    
    /** The day exception repository. */
    private final DayExceptionRepository dayExceptionRepository;
    
    /** The mail service. */
    private final EmailPortOut mailService;

    /**
     * Purge old appointments.
     */
    @Scheduled(cron = "0 0 * * * *") // Revisa cada hora
    @Transactional
    public void purgeOldAppointments() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);

        // 1. Eliminar CANCELADAS después de 1 día de la cancelación (updatedAt)
        List<AppointmentEntity> cancelledToDelete = repository.findByStatusAndUpdatedAtBefore("CANCELLED", oneDayAgo);
        repository.deleteAll(cancelledToDelete);

        // 2. Eliminar CONFIRMADAS después de 1 día de haber sucedido (appointmentDate)
        List<AppointmentEntity> pastToClear = repository.findByStatusAndAppointmentDateBefore("CONFIRMED", oneDayAgo);
        repository.deleteAll(pastToClear);
    }
    
    /**
     * Se ejecuta todos los días a las 3:00 AM
     * Borra registros cuya fecha sea anterior a hace 7 días.
     */
    @Scheduled(cron = "0 0 3 * * ?") 
    @Transactional
    public void cleanupOldRecords() {
        LocalDate limitDate = LocalDate.now().minusDays(7);
        
        log.info("Iniciando limpieza de registros anteriores a: {}", limitDate);

        // Necesitarás añadir estos métodos en tus interfaces Repository
        closedDayRepository.deleteByDateBefore(limitDate);
        dayExceptionRepository.deleteByDateBefore(limitDate);
        
        log.info("Limpieza completada con éxito.");
    }
    
    /**
     * Send daily reminders.
     */
    @Scheduled(cron = "0 0 9 * * ?")
    @Transactional
    public void sendDailyReminders() {
        // Calculamos el inicio y fin del día de mañana
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime startOfTomorrow = tomorrow.atStartOfDay();
        LocalDateTime endOfTomorrow = tomorrow.atTime(LocalTime.MAX);

        log.info("Buscando citas confirmadas para el día: {}", tomorrow);

        // Buscamos citas CONFIRMED entre el inicio y fin de mañana
        List<AppointmentEntity> appointments = repository.findByStatusAndAppointmentDateBetween(
                "CONFIRMED", startOfTomorrow, endOfTomorrow);

        for (AppointmentEntity apt : appointments) {
            try {
                // Asumiendo que tu AppointmentEntity tiene relación con User para sacar el email
                String email = apt.getUser().getEmail(); 
                String clientName = apt.getUser().getName();
                String time = apt.getAppointmentDate().toLocalTime().toString();
                String service = apt.getServiceName();

                mailService.sendAppointmentReminder(email, clientName, tomorrow.toString(), time, service);
                
                log.info("Recordatorio enviado a: {}", email);
            } catch (Exception e) {
                log.error("Error enviando recordatorio para la cita ID: {}", apt.getId(), e);
            }
        }
    }
    
    /**
     * Se ejecuta cada 15 minutos.
     * Busca citas que comiencen en el rango de los próximos 60-75 minutos.
     */
    @Scheduled(cron = "0 */15 * * * *") 
    @Transactional
    public void sendShortTermReminders() {
        // Buscamos citas que ocurran dentro de 1 hora
        // Usamos un margen (ej. de aquí a 60-75 min) para no saltarnos ninguna cita 
        // debido al intervalo de ejecución de la tarea.
        LocalDateTime windowStart = LocalDateTime.now().plusMinutes(60);
        LocalDateTime windowEnd = LocalDateTime.now().plusMinutes(75);

        log.info("Buscando citas para recordatorio de última hora entre {} y {}", windowStart, windowEnd);

        List<AppointmentEntity> appointments = repository.findByStatusAndAppointmentDateBetween(
                "CONFIRMED", windowStart, windowEnd);

        for (AppointmentEntity apt : appointments) {
            try {
                // Validación para clientes externos o registrados
                String email = (apt.getUser() != null) ? apt.getUser().getEmail() : apt.getClientEmail();
                String clientName = (apt.getUser() != null) ? apt.getUser().getName() : apt.getClientFullName();
                
                if (email != null && !email.isEmpty()) {
                    String time = apt.getAppointmentDate().toLocalTime().toString();
                    String service = apt.getServiceName();

                    mailService.sendUrgentReminder(email, clientName, time, service);
                    log.info("Recordatorio de última hora enviado a: {}", email);
                }
            } catch (Exception e) {
                log.error("Error enviando recordatorio urgente para cita ID: {}", apt.getId(), e);
            }
        }
    }
}