package com.example.demo.adapter.in.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Centralizamos aquí todos los errores de estado (fecha pasada, usuario ya existe, etc.)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException ex) {
        // Devolvemos CONFLICT (409) para indicar que la operación no puede realizarse 
        // debido al estado actual (ej: la fecha ya pasó)
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para esta acción.");
    }
    
    // Opcional: Manejador genérico para evitar que el front reciba un error vacío
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + ex.getMessage());
    }
    
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> body = new HashMap<>();
        
        // ex.getReason() contiene SOLO el String "La hora ya está ocupada."
        // ex.getMessage() contiene "409 CONFLICT 'La hora ya está ocupada.'"
        body.put("message", ex.getReason()); 
        
        return new ResponseEntity<>(body, ex.getStatusCode());
    }
}