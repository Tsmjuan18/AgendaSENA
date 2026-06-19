
package com.example.demo_agenda_sena.services;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo_agenda_sena.entitys.Ambiente;
import com.example.demo_agenda_sena.entitys.Reserva;
import com.example.demo_agenda_sena.enums.TipoAmbiente;
import com.example.demo_agenda_sena.repository.AmbienteRepository;
import com.example.demo_agenda_sena.repository.ReservaRepository;

@Service
public class AmbienteService {

    private final AmbienteRepository ambienteRepository;
    private final ReservaRepository reservaRepository;

    // Inyección por constructor (con ambos repositorios)
    public AmbienteService(AmbienteRepository ambienteRepository, ReservaRepository reservaRepository) {
        this.ambienteRepository = ambienteRepository;
        this.reservaRepository = reservaRepository;
    }

    // ===== CREAR AMBIENTE =====
    public Ambiente crearAmbiente(String nombre, TipoAmbiente tipo, Integer capacidad, Boolean activo) {
        // Si no se envía activo, por defecto es true
        boolean activoFinal = activo != null ? activo : true;
        Ambiente ambiente = new Ambiente(nombre, tipo, capacidad, activoFinal);
        return ambienteRepository.save(ambiente);
    }

    // ===== LISTAR AMBIENTES =====
    public List<Ambiente> listarAmbientes() {
        return ambienteRepository.findAll();
    }

    // ===== LISTAR AMBIENTES ACTIVOS =====
    public List<Ambiente> listarAmbientesActivos() {
        return ambienteRepository.findByActivoTrue();
    }

    // ===== OBTENER RESERVAS DE UN AMBIENTE EN UNA FECHA =====
    public List<Reserva> obtenerReservasDeAmbienteEnFecha(Long ambienteId, LocalDate fecha) {
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1);
        return reservaRepository.findReservasActivasDeAmbienteEnFecha(ambienteId, inicioDia, finDia);
    }

    // ===== LISTAR AMBIENTES DISPONIBLES EN UN RANGO DE TIEMPO =====
    public List<Ambiente> listarAmbientesDisponibles(LocalDateTime inicio, LocalDateTime fin) {
        // 1. Obtener todas las reservas activas que se cruzan con el rango
        List<Reserva> reservasEnRango = reservaRepository.findReservasActivasEnRango(inicio, fin);
        
        // 2. Extraer los IDs de los ambientes ocupados
        List<Long> idsOcupados = reservasEnRango.stream()
                .map(r -> r.getAmbiente().getId())
                .distinct()
                .collect(Collectors.toList());

        // 3. Retornar ambientes activos que NO están en la lista de ocupados
        return ambienteRepository.findByActivoTrue().stream()
                .filter(a -> !idsOcupados.contains(a.getId()))
                .collect(Collectors.toList());
    }

    // ===== BUSCAR AMBIENTE POR ID ===== (necesario para validaciones)
    public Ambiente buscarPorId(Long id) {
        return ambienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ambiente no encontrado con id: " + id));
    }
}