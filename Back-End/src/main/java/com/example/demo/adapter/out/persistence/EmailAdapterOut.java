package com.example.demo.adapter.out.persistence;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.example.demo.application.ports.out.EmailPortOut;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class EmailAdapterOut.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailAdapterOut implements EmailPortOut {

    /** The mail sender. */
    private final JavaMailSender mailSender;
    
    /** The Constant BRAND_COLOR. */
    private static final String BRAND_COLOR = "#D4AF37"; // Dorado
    
    /** The Constant SECONDARY_COLOR. */
    private static final String SECONDARY_COLOR = "#111827"; // Negro/Gris oscuro

    /**
     * Send appointment notification.
     *
     * @param email the email
     * @param clientName the client name
     * @param serviceName the service name
     * @param date the date
     * @param status the status
     */
    @Async
    @Override
    public void sendAppointmentNotification(String email, String clientName, String serviceName, String date, String status) {
        boolean isConfirmed = "CONFIRMED".equals(status);
        String subject = isConfirmed ? "✅ Cita Confirmada - Peluquería Ángel Díaz" : "❌ Cita Cancelada - Peluquería Ángel Díaz";
        
        String content = "<div style='font-family: sans-serif; color: " + SECONDARY_COLOR + "; max-width: 600px; border: 1px solid #eee; padding: 20px;'>" +
                "<h2 style='color: " + BRAND_COLOR + ";'>Peluquería Ángel Díaz</h2>" +
                "<p>Hola <strong>" + clientName + "</strong>,</p>" +
                "<p>Tu cita para el servicio de <strong>" + serviceName + "</strong> ha sido <strong>" + (isConfirmed ? "CONFIRMADA" : "CANCELADA") + "</strong>.</p>" +
                "<div style='background: #f9fafb; padding: 15px; border-radius: 8px; margin: 20px 0;'>" +
                "📅 <strong>Fecha y Hora:</strong> " + date + "<br>" +
                "</div>" +
                "<p>" + (isConfirmed ? "¡Estamos deseando verte por la peluquería!" : "Si ha sido un error, puedes volver a solicitar una reserva en nuestra web.") + "</p>" +
                "<hr style='border: 0; border-top: 1px solid #eee;'>" +
                "<small style='color: #6b7280;'>Este es un mensaje automático, por favor no respondas a este correo.</small>" +
                "</div>";

        sendHtmlEmail(email, subject, content);
    }

    /**
     * Send admin notification.
     *
     * @param adminEmail the admin email
     * @param clientName the client name
     * @param service the service
     * @param date the date
     */
    @Async
    @Override
    public void sendAdminNotification(String adminEmail, String clientName, String service, String date) {
        String subject = "🔔 Nueva Reserva Confirmada: " + clientName;
        String content = "<div style='font-family: sans-serif; border-left: 5px solid " + BRAND_COLOR + "; padding: 20px; background: #f3f4f6;'>" +
                "<h3 style='color: " + SECONDARY_COLOR + ";'>¡Hola Ángel! Se ha registrado una nueva reserva:</h3>" +
                "<ul style='list-style: none; padding: 0;'>" +
                "<li><strong>👤 Cliente:</strong> " + clientName + "</li>" +
                "<li><strong>✂️ Servicio:</strong> " + service + "</li>" +
                "<li><strong>📅 Fecha:</strong> " + date + "</li>" +
                "</ul>" +
                "<p style='font-size: 0.9em; color: #374151;'>La cita se ha confirmado automáticamente en el sistema.</p>" +
                "</div>";

        sendHtmlEmail(adminEmail, subject, content);
    }

    /**
     * Send appointment reminder.
     *
     * @param to the to
     * @param clientName the client name
     * @param date the date
     * @param time the time
     * @param service the service
     */
    @Async
    @Override
    public void sendAppointmentReminder(String to, String clientName, String date, String time, String service) {
        String subject = "⏰ Recordatorio de tu cita - Mañana";
        String content = "<div style='font-family: sans-serif; color: " + SECONDARY_COLOR + "; max-width: 600px; border: 1px solid " + BRAND_COLOR + "; padding: 20px;'>" +
                "<h2 style='color: " + BRAND_COLOR + ";'>¡Te vemos mañana!</h2>" +
                "<p>Hola <strong>" + clientName + "</strong>,</p>" +
                "<p>Te recordamos que tienes una cita programada para mañana:</p>" +
                "<div style='background: #f9fafb; padding: 15px; border-radius: 8px; margin: 20px 0; border: 1px dashed " + BRAND_COLOR + ";'>" +
                "📅 <strong>Día:</strong> " + date + "<br>" +
                "🕒 <strong>Hora:</strong> " + time + "<br>" +
                "✂️ <strong>Servicio:</strong> " + service +
                "</div>" +
                "<p>Si no puedes asistir, por favor infórmanos lo antes posible.</p>" +
                "</div>";

        sendHtmlEmail(to, subject, content);
    }
    
    /**
     * Send urgent reminder.
     *
     * @param to the to
     * @param clientName the client name
     * @param time the time
     * @param service the service
     */
    @Async
    @Override
    public void sendUrgentReminder(String to, String clientName, String time, String service) {
        String subject = "🔔 ¡Tu cita es en 1 hora! - Peluquería Ángel Díaz";
        
        // Diseño con un toque de urgencia sutil, resaltando la hora
        String content = "<div style='font-family: sans-serif; color: " + SECONDARY_COLOR + "; max-width: 600px; border: 2px solid " + BRAND_COLOR + "; padding: 25px; border-radius: 12px;'>" +
                "<div style='text-align: center; margin-bottom: 20px;'>" +
                    "<h2 style='color: " + BRAND_COLOR + "; margin: 0;'>¡Te vemos pronto!</h2>" +
                    "<p style='font-size: 1.1em;'>Tu cita está programada para dentro de una hora.</p>" +
                "</div>" +
                
                "<p>Hola <strong>" + clientName + "</strong>,</p>" +
                "<p>Este es un recordatorio automático de que tu servicio comienza en breve:</p>" +
                
                "<div style='background: " + SECONDARY_COLOR + "; color: white; padding: 20px; border-radius: 8px; margin: 20px 0; text-align: center;'>" +
                    "<span style='font-size: 0.9em; opacity: 0.8; text-transform: uppercase;'>Comienza a las</span><br>" +
                    "<strong style='font-size: 2em; color: " + BRAND_COLOR + ";'>" + time + "</strong><br>" +
                    "<span style='font-size: 1.1em; display: block; margin-top: 10px;'>✂️ " + service + "</span>" +
                "</div>" +
                
                "<p style='text-align: center; color: #4b5563;'>📍 Recuerda llegar unos minutos antes. ¡Te esperamos!</p>" +
                
                "<hr style='border: 0; border-top: 1px solid #eee; margin: 20px 0;'>" +
                "<div style='text-align: center;'>" +
                    "<small style='color: #9ca3af;'>Peluquería Ángel Díaz - Estilo y Confianza</small>" +
                "</div>" +
            "</div>";

        sendHtmlEmail(to, subject, content);
    }

    /**
     * Send html email.
     *
     * @param to the to
     * @param subject the subject
     * @param htmlContent the html content
     */
    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Fallo al enviar correo a {}: {}", to, e.getMessage());
            throw new EmailSendingException("Fallo al enviar correo electrónico", e);
        }
    }

    /**
     * The Class EmailSendingException.
     */
    @SuppressWarnings("serial")
    class EmailSendingException extends RuntimeException {
        
        /**
         * Instantiates a new email sending exception.
         *
         * @param message the message
         * @param cause the cause
         */
        public EmailSendingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}