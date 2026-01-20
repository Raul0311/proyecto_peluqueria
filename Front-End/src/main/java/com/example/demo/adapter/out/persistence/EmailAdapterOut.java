package com.example.demo.adapter.out.persistence;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.example.demo.application.ports.out.EmailPortOut;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

/**
 * The Class EmailAdapterOut.
 */
@Service
@RequiredArgsConstructor
public class EmailAdapterOut implements EmailPortOut {

    /** The mail sender. */
    private final JavaMailSender mailSender;
    
    /** The Constant BRAND_COLOR. */
    private static final String BRAND_COLOR = "#D4AF37"; // Dorado
    
    /** The Constant SECONDARY_COLOR. */
    private static final String SECONDARY_COLOR = "#111827"; // Negro/Gris oscuro

    /**
     * Send reset password email.
     *
     * @param email the email
     * @param token the token
     */
    @Async
    @Override
    public void sendResetPasswordEmail(String email, String token) {
        String resetLink = "https://f4125bef7192.ngrok-free.app/reset-password?token=" + token;
        String subject = "Restablece tu contraseña - Peluquería Ángel Díaz";
        String content = "<div style='font-family: sans-serif; text-align: center; padding: 30px; border: 1px solid #eee;'>" +
                "<h2 style='color: " + SECONDARY_COLOR + ";'>¿Olvidaste tu contraseña?</h2>" +
                "<p>No pasa nada, haz clic en el botón de abajo para elegir una nueva:</p>" +
                "<a href='" + resetLink + "' style='display: inline-block; margin-top: 20px; padding: 15px 25px; background: " + BRAND_COLOR + "; color: white; text-decoration: none; font-weight: bold; border-radius: 8px;'>Restablecer Contraseña</a>" +
                "<p style='margin-top: 30px; font-size: 0.8rem; color: #9ca3af;'>Si no solicitaste este cambio, puedes ignorar este correo de forma segura.</p>" +
                "</div>";

        sendHtmlEmail(email, subject, content);
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
            helper.setText(htmlContent, true); // true indica que es HTML
            
            mailSender.send(message);
        } catch (MessagingException e) {
            // Lanzamos nuestra excepción específica envolviendo el error original
            throw new EmailSendingException("Fallo al enviar correo electrónico a: " + to, e);
        } catch (Exception e) {
            // Capturamos cualquier otro error inesperado (como problemas de red)
            throw new EmailSendingException("Error inesperado en el servicio de correo", e);
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