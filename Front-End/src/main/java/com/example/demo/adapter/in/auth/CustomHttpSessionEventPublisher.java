package com.example.demo.adapter.in.auth;

import jakarta.servlet.http.HttpSessionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import com.example.demo.adapter.out.persistence.UserAdapterOut;

import java.util.Optional;

/**
 * The Class CustomHttpSessionEventPublisher.
 */
@Slf4j
@RequiredArgsConstructor
public class CustomHttpSessionEventPublisher extends HttpSessionEventPublisher {

    /** The user service. */
    private final UserAdapterOut userService;

    /**
     * Session destroyed.
     * Maneja la limpieza del token del usuario cuando la sesión expira o se cierra.
     *
     * @param event the event
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        // Usamos Optional para evitar los NullPointerException que marca SonarQube
        extractSessionId(event).ifPresent(sessionId -> {
            try {
                userService.deleteUserToken(sessionId);
                log.info("Sesión destruida correctamente! ID: {}", sessionId);
            } catch (Exception e) {
                log.error("No se pudo eliminar el token de sesión para ID {}: {}", sessionId, e.getMessage());
            }
        });

        super.sessionDestroyed(event);
    }

    /**
     * Método auxiliar para navegar el contexto de seguridad de forma segura.
     *
     * @param event the event
     * @return the optional
     */
    private Optional<String> extractSessionId(HttpSessionEvent event) {
        return Optional.ofNullable(event.getSession().getAttribute("SPRING_SECURITY_CONTEXT"))
                .filter(SecurityContext.class::isInstance)
                .map(SecurityContext.class::cast)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getDetails)
                .filter(WebAuthenticationDetails.class::isInstance)
                .map(WebAuthenticationDetails.class::cast)
                .map(WebAuthenticationDetails::getSessionId);
    }
}