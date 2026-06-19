package com.example.demo_agenda_sena.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, Object>> manejarReglaNegocio(ReglaNegocioException e) {
        Map<String, Object> cuerpo = Map.of(
                "error", e.getStatus().getReasonPhrase(),
                "mensaje", e.getMessage(),
                "status", e.getStatus().value(),
                "timestamp", LocalDateTime.now().toString()
        );
        return ResponseEntity.status(e.getStatus()).body(cuerpo);
    }

    /**
     * Red de seguridad: cualquier excepción no controlada cae aquí
     * en vez de devolver un 500 sin explicación al cliente.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorInesperado(Exception e) {
        Map<String, Object> cuerpo = Map.of(
                "error", "Internal Server Error",
                "mensaje", "Ocurrió un error inesperado: " + e.getMessage(),
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "timestamp", LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cuerpo);
    }
}