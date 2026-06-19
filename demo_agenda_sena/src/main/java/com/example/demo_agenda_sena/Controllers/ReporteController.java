package com.example.demo_agenda_sena.controllers;

import com.example.demo_agenda_sena.entitys.Ambiente;
import com.example.demo_agenda_sena.entitys.Reserva;
import com.example.demo_agenda_sena.repository.AmbienteRepository;
import com.example.demo_agenda_sena.repository.ReservaRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final AmbienteRepository ambienteRepository;
    private final ReservaRepository reservaRepository;

    public ReporteController(AmbienteRepository ambienteRepository, ReservaRepository reservaRepository) {
        this.ambienteRepository = ambienteRepository;
        this.reservaRepository = reservaRepository;
    }

    // ===== GET /api/reportes/ocupacion?fecha=2026-06-15 =====
    @GetMapping("/ocupacion")
    public ResponseEntity<List<Map<String, Object>>> reporteOcupacion(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1);
        
        // Horas institucionales: 6:00 a 22:00 = 16 horas
        long totalMinutosDisponibles = 16 * 60; // 960 minutos

        // Obtener todos los ambientes
        List<Ambiente> ambientes = ambienteRepository.findAll();

        // Generar reporte por cada ambiente
        List<Map<String, Object>> reporte = ambientes.stream().map(ambiente -> {
            // Obtener reservas del ambiente en esa fecha (ACTIVAS + FINALIZADAS)
            List<Reserva> reservas = reservaRepository.findReservasParaReporte(
                    ambiente.getId(), inicioDia, finDia);

            // Calcular minutos totales ocupados (sumando los intervalos)
            long minutosOcupados = reservas.stream()
                    .mapToLong(r -> {
                        // Ajustar inicio/fin a los límites del día si es necesario
                        LocalDateTime inicio = r.getFechaInicio().isBefore(inicioDia) 
                                ? inicioDia : r.getFechaInicio();
                        LocalDateTime fin = r.getFechaFin().isAfter(finDia) 
                                ? finDia : r.getFechaFin();
                        
                        // Si el inicio es después del fin, no contar (caso borde)
                        if (inicio.isAfter(fin)) {
                            return 0L;
                        }
                        
                        return Duration.between(inicio, fin).toMinutes();
                    })
                    .sum();

            // Calcular horas y porcentaje
            double horasOcupadas = minutosOcupados / 60.0;
            double porcentajeOcupacion = (minutosOcupados * 100.0) / totalMinutosDisponibles;

            // Construir el mapa de respuesta
            Map<String, Object> item = new HashMap<>();
            item.put("ambienteId", ambiente.getId());
            item.put("nombreAmbiente", ambiente.getNombre());
            item.put("tipo", ambiente.getTipo());
            item.put("capacidad", ambiente.getCapacidad());
            item.put("activo", ambiente.isActivo());
            item.put("horasOcupadas", Math.round(horasOcupadas * 100.0) / 100.0);
            item.put("porcentajeOcupacion", Math.round(porcentajeOcupacion * 100.0) / 100.0);
            item.put("cantidadReservas", reservas.size());
            
            return item;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(reporte);
    }
}