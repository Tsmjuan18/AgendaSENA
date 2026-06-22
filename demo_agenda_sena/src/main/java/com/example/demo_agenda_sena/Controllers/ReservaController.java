package com.example.demo_agenda_sena.controllers;

//ggg
import com.example.demo_agenda_sena.dto.ReservaRequestDTO;
import com.example.demo_agenda_sena.dto.ReservaResponseDTO;
import com.example.demo_agenda_sena.entitys.Reserva;
import com.example.demo_agenda_sena.repository.ReservaRepository;
import com.example.demo_agenda_sena.services.ReservaService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final ReservaRepository reservaRepository;

    public ReservaController(ReservaService reservaService,ReservaRepository reservaRepository) {
        this.reservaService = reservaService;
        this.reservaRepository =reservaRepository;
    }

    // ===== POST /api/reservas - Crear reserva =====
    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crearReserva(@Valid @RequestBody ReservaRequestDTO request) {
        Reserva reserva = reservaService.crearReserva(
                request.getAmbienteId(),
                request.getNombreInstructor(),
                request.getFechaInicio(),
                request.getFechaFin(),
                request.getNumeroAprendices());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ReservaResponseDTO.desdeEntidad(reserva));
    }

    // ===== PATCH /api/reservas/{id}/cancelar - Cancelar reserva =====
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponseDTO> cancelarReserva(@PathVariable Long id) {
        Reserva reserva = reservaService.cancelarReserva(id);
        return ResponseEntity.ok(ReservaResponseDTO.desdeEntidad(reserva));
    }

    // ===== GET /api/reservas - Listar todas las reservas =====
@GetMapping
public ResponseEntity<List<ReservaResponseDTO>> listarReservas() {
    List<Reserva> reservas = reservaRepository.findAll();
    return ResponseEntity.ok(reservas.stream()
            .map(ReservaResponseDTO::desdeEntidad)
            .collect(Collectors.toList()));
}
}