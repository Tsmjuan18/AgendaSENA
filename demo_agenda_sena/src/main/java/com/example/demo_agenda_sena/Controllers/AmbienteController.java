package com.example.demo_agenda_sena.controllers;

import com.example.demo_agenda_sena.dto.AmbienteRequestDTO;
import com.example.demo_agenda_sena.dto.AmbienteResponseDTO;
import com.example.demo_agenda_sena.dto.ReservaResponseDTO;
import com.example.demo_agenda_sena.entitys.Ambiente;
import com.example.demo_agenda_sena.entitys.Reserva;
import com.example.demo_agenda_sena.services.AmbienteService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ambientes")
public class AmbienteController {

    private final AmbienteService ambienteService;

    public AmbienteController(AmbienteService ambienteService) {
        this.ambienteService = ambienteService;
    }

    // ===== POST /api/ambientes - Registrar un ambiente =====
    @PostMapping
    public ResponseEntity<AmbienteResponseDTO> crearAmbiente(@Valid @RequestBody AmbienteRequestDTO request) {
        Ambiente ambiente = ambienteService.crearAmbiente(
                request.getNombre(),
                request.getTipo(),
                request.getCapacidad(),
                request.getActivo());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AmbienteResponseDTO.desdeEntidad(ambiente));
    }

    // ===== GET /api/ambientes - Listar todos los ambientes =====
    @GetMapping
    public ResponseEntity<List<AmbienteResponseDTO>> listarAmbientes() {
        List<Ambiente> ambientes = ambienteService.listarAmbientes();
        return ResponseEntity.ok(ambientes.stream()
                .map(AmbienteResponseDTO::desdeEntidad)
                .collect(Collectors.toList()));
    }

    // ===== GET /api/ambientes/{id}/reservas?fecha=2026-06-15 =====
    @GetMapping("/{id}/reservas")
    public ResponseEntity<List<ReservaResponseDTO>> listarReservasDeAmbiente(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        // Verificar que el ambiente existe
        ambienteService.buscarPorId(id);

        List<Reserva> reservas = ambienteService.obtenerReservasDeAmbienteEnFecha(id, fecha);
        return ResponseEntity.ok(reservas.stream()
                .map(ReservaResponseDTO::desdeEntidad)
                .collect(Collectors.toList()));
    }

    // ===== GET /api/ambientes/disponibles?inicio=...&fin=... =====
    @GetMapping("/disponibles")
    public ResponseEntity<List<AmbienteResponseDTO>> listarAmbientesDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        // Validar que las fechas tengan sentido
        if (inicio.isAfter(fin)) {
            throw new RuntimeException("La fecha de inicio debe ser anterior a la fecha de fin");
        }

        List<Ambiente> disponibles = ambienteService.listarAmbientesDisponibles(inicio, fin);
        return ResponseEntity.ok(disponibles.stream()
                .map(AmbienteResponseDTO::desdeEntidad)
                .collect(Collectors.toList()));
    }
}