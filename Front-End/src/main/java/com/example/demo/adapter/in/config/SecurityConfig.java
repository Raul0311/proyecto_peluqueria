package com.example.demo.adapter.in.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.example.demo.adapter.in.auth.CustomAuthFailureHandler;
import com.example.demo.adapter.in.auth.CustomAuthenticationProvider;
import com.example.demo.adapter.in.auth.CustomHttpSessionEventPublisher;
import com.example.demo.adapter.in.auth.CustomSuccessHandler;
import com.example.demo.adapter.out.persistence.UserAdapterOut;

import lombok.AllArgsConstructor;

/**
 * The Class SecurityConfig.
 */
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    /** The custom auth provider. */
    private final CustomAuthenticationProvider customAuthProvider;
    
    /** The custom success handler. */
    private final CustomSuccessHandler customSuccessHandler;
    
    /** The custom auth failure handler. */
    private final CustomAuthFailureHandler customFailureHandler;

    /**
     * Http session event publisher.
     *
     * @param userService the user service
     * @return the http session event publisher
     */
    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher(UserAdapterOut userService) {
        return new CustomHttpSessionEventPublisher(userService);
    }

    /**
     * Security filter chain.
     *
     * @param http the http
     * @param failureHandler the failure handler
     * @return the security filter chain
     */
    // Configuración de seguridad
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
        	.authenticationProvider(customAuthProvider)
        	.authorizeHttpRequests(auth -> auth
    			.requestMatchers("/private/admin").hasRole("ADMIN")
    			.requestMatchers("/private/home").hasRole("USER")
    			// "/auth", "/register", "/auth/register", "/static/**"
    			.anyRequest().permitAll()
            )
            // Configuración del login
            .formLogin(form -> form
                .loginPage("/auth")                 // GET /login muestra el formulario
                .loginProcessingUrl("/auth")        // POST /login procesa login
                .successHandler(customSuccessHandler)   // redirige a /home tras login exitoso
                .failureHandler(customFailureHandler)
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            // Configuración del logout
            .logout(logout -> logout
                .logoutUrl("/j_spring_security_logout")
                .logoutSuccessUrl("/auth?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .headers(headers -> headers
        		.frameOptions(FrameOptionsConfig::sameOrigin)
        		.contentSecurityPolicy(csp -> csp
    				.policyDirectives(
						    "default-src 'self'; " +
						    "script-src 'self' www.google-analytics.com; " +
						    "connect-src 'self' wss://*.mizu-voip.com wss://*.webvoipphone.com; " +
						    "img-src 'self'; " +
						    "style-src 'self' 'unsafe-inline'; " +
						    "font-src 'self'; " +
						    "form-action 'self'; " +
						    "base-uri 'self'; " +
						    "frame-ancestors 'none'"
					)
                )
    		)
            // CSRF por defecto
            .csrf(Customizer.withDefaults());

        return http.build();
    }
}
