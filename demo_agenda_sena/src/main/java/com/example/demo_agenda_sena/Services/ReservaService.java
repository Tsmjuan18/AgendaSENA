package com.example.demo_agenda_sena.services;

import com.example.demo_agenda_sena.enums.EstadoReserva;
import com.example.demo_agenda_sena.exception.ReglaNegocioException;
import com.example.demo_agenda_sena.entitys.Ambiente;
import com.example.demo_agenda_sena.entitys.Reserva;
import com.example.demo_agenda_sena.repository.AmbienteRepository;
import com.example.demo_agenda_sena.repository.ReservaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservaService {

    private static final LocalTime HORA_APERTURA = LocalTime.of(6, 0);
    private static final LocalTime HORA_CIERRE = LocalTime.of(22, 0);
    private static final int DURACION_MINIMA_HORAS = 1;
    private static final int DURACION_MAXIMA_HORAS = 4;
    private static final int MAX_RESERVAS_ACTIVAS_POR_INSTRUCTOR_POR_DIA = 3;
    private static final int HORAS_MINIMAS_PARA_CANCELAR = 2;

    private final ReservaRepository reservaRepository;
    private final AmbienteRepository ambienteRepository;

    public ReservaService(ReservaRepository reservaRepository, AmbienteRepository ambienteRepository) {
        this.reservaRepository = reservaRepository;
        this.ambienteRepository = ambienteRepository;
    }

    @Transactional
    public Reserva crearReserva(Long ambienteId, String nombreInstructor, LocalDateTime fechaInicio,
            LocalDateTime fechaFin, Integer numeroAprendices) {

        Ambiente ambiente = ambienteRepository.findById(ambienteId)
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe un ambiente con id " + ambienteId, HttpStatus.NOT_FOUND));

        validarFechasBasicas(fechaInicio, fechaFin);
        validarHorarioInstitucional(fechaInicio, fechaFin);
        validarNoEsEnElPasado(fechaInicio);
        validarAmbienteActivo(ambiente);
        validarCapacidad(ambiente, numeroAprendices);
        validarSinCruceDeHorario(ambiente.getId(), fechaInicio, fechaFin);
        validarLimitePorInstructor(nombreInstructor, fechaInicio);

        Reserva reserva = new Reserva(ambiente, nombreInstructor, fechaInicio, fechaFin,
                numeroAprendices, EstadoReserva.ACTIVA);

        return reservaRepository.save(reserva);
    }

    @Transactional
    public Reserva cancelarReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe una reserva con id " + reservaId, HttpStatus.NOT_FOUND));

        if (reserva.getEstado() != EstadoReserva.ACTIVA) {
            throw new ReglaNegocioException(
                    "Solo se pueden cancelar reservas ACTIVAS. Esta reserva está " + reserva.getEstado(),
                    HttpStatus.CONFLICT);
        }

        long minutosParaInicio = Duration.between(LocalDateTime.now(), reserva.getFechaInicio()).toMinutes();

        if (minutosParaInicio < HORAS_MINIMAS_PARA_CANCELAR * 60) {
            throw new ReglaNegocioException(
                    "Solo se puede cancelar una reserva si faltan al menos " + HORAS_MINIMAS_PARA_CANCELAR
                            + " horas para su inicio. Faltan aproximadamente " + (minutosParaInicio / 60) + " horas.",
                    HttpStatus.CONFLICT);
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        return reservaRepository.save(reserva);
    }

    // ---------- Reglas de negocio individuales ----------

    private void validarFechasBasicas(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null || fin == null) {
            throw new ReglaNegocioException("La fecha de inicio y de fin son obligatorias", HttpStatus.BAD_REQUEST);
        }
        if (!fin.isAfter(inicio)) {
            throw new ReglaNegocioException("La fecha de fin debe ser posterior a la fecha de inicio",
                    HttpStatus.BAD_REQUEST);
        }

        long minutos = Duration.between(inicio, fin).toMinutes();
        long horas = minutos / 60;
        boolean esExactaEnHoras = minutos % 60 == 0;

        if (horas < DURACION_MINIMA_HORAS || horas > DURACION_MAXIMA_HORAS || !esExactaEnHoras) {
            throw new ReglaNegocioException(
                    "La reserva debe durar entre " + DURACION_MINIMA_HORAS + " y " + DURACION_MAXIMA_HORAS + " horas",
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validarHorarioInstitucional(LocalDateTime inicio, LocalDateTime fin) {
        LocalTime horaInicio = inicio.toLocalTime();
        LocalTime horaFin = fin.toLocalTime();

        // inicio: desde las 06:00 (inclusive) hasta antes de las 22:00 (exclusivo)
        boolean inicioValido = !horaInicio.isBefore(HORA_APERTURA) && horaInicio.isBefore(HORA_CIERRE);
        // fin: desde las 06:00 (inclusive) hasta las 22:00 (inclusive)
        boolean finValido = !horaFin.isBefore(HORA_APERTURA) && !horaFin.isAfter(HORA_CIERRE);

        if (!inicioValido || !finValido) {
            throw new ReglaNegocioException(
                    "Las reservas solo pueden estar entre las " + HORA_APERTURA + " y las " + HORA_CIERRE,
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validarNoEsEnElPasado(LocalDateTime inicio) {
        if (!inicio.isAfter(LocalDateTime.now())) {
            throw new ReglaNegocioException(
                    "La fecha de inicio debe ser posterior al momento actual", HttpStatus.BAD_REQUEST);
        }
    }

    private void validarAmbienteActivo(Ambiente ambiente) {
        if (!ambiente.isActivo()) {
            throw new ReglaNegocioException(
                    "El ambiente '" + ambiente.getNombre() + "' no está activo y no puede reservarse",
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validarCapacidad(Ambiente ambiente, Integer numeroAprendices) {
        if (numeroAprendices == null || numeroAprendices <= 0) {
            throw new ReglaNegocioException(
                    "El número de aprendices debe ser mayor a 0", HttpStatus.BAD_REQUEST);
        }
        if (numeroAprendices > ambiente.getCapacidad()) {
            throw new ReglaNegocioException(
                    "El número de aprendices (" + numeroAprendices + ") supera la capacidad del ambiente ("
                            + ambiente.getCapacidad() + ")",
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validarSinCruceDeHorario(Long ambienteId, LocalDateTime inicio, LocalDateTime fin) {
        List<Reserva> cruces = reservaRepository.findReservasQueSeSolapan(ambienteId, inicio, fin);
        if (!cruces.isEmpty()) {
            throw new ReglaNegocioException(
                    "El ambiente ya tiene una reserva ACTIVA que se cruza con el horario solicitado",
                    HttpStatus.CONFLICT);
        }
    }

    private void validarLimitePorInstructor(String nombreInstructor, LocalDateTime fechaInicio) {
        LocalDateTime inicioDelDia = fechaInicio.toLocalDate().atStartOfDay();
        LocalDateTime finDelDia = inicioDelDia.plusDays(1);

        long reservasDelDia = reservaRepository.contarReservasActivasDelInstructorEnElDia(
                nombreInstructor, inicioDelDia, finDelDia);

        if (reservasDelDia >= MAX_RESERVAS_ACTIVAS_POR_INSTRUCTOR_POR_DIA) {
            throw new ReglaNegocioException(
                    "El instructor '" + nombreInstructor + "' ya tiene " + reservasDelDia
                            + " reservas ACTIVAS ese día (máximo " + MAX_RESERVAS_ACTIVAS_POR_INSTRUCTOR_POR_DIA + ")",
                    HttpStatus.CONFLICT);
        }
    }
}