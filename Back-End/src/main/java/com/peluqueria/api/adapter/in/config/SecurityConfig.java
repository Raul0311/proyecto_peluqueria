package com.peluqueria.api.adapter.in.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.peluqueria.api.application.ports.out.RolesPortOut;
import com.peluqueria.api.application.ports.out.UserPortOut;

import lombok.AllArgsConstructor;

/**
 * The Class SecurityConfig adaptada para WebFlux.
 */
@Configuration(proxyBeanMethods = false)
@EnableWebFluxSecurity
@AllArgsConstructor
public class SecurityConfig {
	
	/** The user port out. */
	private final UserPortOut userPortOut;
	
	/** The roles port out. */
	private final RolesPortOut rolesPortOut;
	
	/**
	 * Definición del filtro personalizado.
	 *
	 * @return the user token filter
	 */
    @Bean
    public UserTokenFilter userTokenFilter() {
        return new UserTokenFilter(userPortOut, rolesPortOut);
    }
	
	/**
	 * Configuración de CORS para WebFlux.
	 *
	 * @return the cors configuration source
	 */
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:8080", 
                "http://localhost:3000", 
                "https://*.ngrok-free.app"
            ));
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "ngrok-skip-browser-warning"));
        configuration.setAllowCredentials(true); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); 
        return source;
    }

    /**
     * Cadena de filtros de seguridad reactiva.
     *
     * @param http the http
     * @return the security web filter chain
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            // 1. CORS adaptado a WebFlux
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 2. Desactivar CSRF para APIs REST
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            
            // 3. Configurar como Stateless (Sin sesiones de servidor)
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            
            // 4. REGISTRO DEL FILTRO PERSONALIZADO
            // Lo ponemos en la posición de AUTHENTICATION para que se ejecute antes de autorizar las rutas
            .addFilterAt(userTokenFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
            
            // 5. Autorización de rutas
            .authorizeExchange(exchanges -> exchanges
                // Rutas que no necesitan token
                .pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs", "/api-docs/**").permitAll()
                .pathMatchers("/api/closed-days", "/api/get-day-exceptions", "/api/get-haircut-services", "/api/occupied-slots/**", 
                             "/api/getAppointment/**").permitAll()
                
                // El resto de la API requiere estar autenticado
                .anyExchange().authenticated()
            )
            
            // 6. Cabeceras de seguridad
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives(
                        "default-src 'self'; " +
                        "script-src 'self' 'unsafe-inline' 'unsafe-eval' www.google-analytics.com; " +
                        "connect-src 'self' https://*.ngrok-free.app wss://*.mizu-voip.com wss://*.webvoipphone.com; " +
                        "img-src 'self' data:; " +
                        "style-src 'self' 'unsafe-inline'; " +
                        "font-src 'self'; " +
                        "frame-ancestors 'none'"
                    )
                )
            )
            .build();
    }
}