package com.example.demo.adapter.in.config;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.example.demo.application.ports.out.RolesPortOut;
import com.example.demo.application.ports.out.UserPortOut;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * The Class UserTokenFilter.
 */
@AllArgsConstructor
public class UserTokenFilter implements WebFilter {

    /** The user port out. */
    private final UserPortOut userPortOut;
    
    /** The roles port out. */
    private final RolesPortOut rolesPortOut;

    /**
     * Filter.
     *
     * @param exchange the exchange
     * @param chain the chain
     * @return the mono
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 1. Obtener el header de Authorization
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String userToken = authHeader.substring(7);

        // 2. Ejecutar la validación (bloqueante) en el hilo elástico
        return Mono.fromCallable(() -> {
            Long userId = userPortOut.validateUser(userToken);
            List<String> userRoles = rolesPortOut.findRolesByUserId(userId);
            
            List<SimpleGrantedAuthority> authorities = userRoles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            return new UsernamePasswordAuthenticationToken(userId, null, authorities);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .flatMap(authentication -> {
            return chain.filter(exchange)
                // El contextWrite debe ir pegado al flujo que continúa la cadena
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        })
        // Si hay error en la validación, simplemente seguimos sin autenticar
        .onErrorResume(e -> {
            e.printStackTrace(); 
            return chain.filter(exchange);
        });
    }
}