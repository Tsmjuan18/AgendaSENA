package com.example.demo_agenda_sena.repository;

import com.example.demo_agenda_sena.enums.EstadoReserva;
import com.example.demo_agenda_sena.entitys.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM Reserva r " +
           "WHERE r.ambiente.id = :ambienteId " +
           "AND r.estado = 'ACTIVA' " +
           "AND r.fechaInicio < :fin " +
           "AND r.fechaFin > :inicio")
    List<Reserva> findReservasQueSeSolapan(
            @Param("ambienteId") Long ambienteId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    @Query("SELECT COUNT(r) FROM Reserva r " +
           "WHERE r.nombreInstructor = :nombreInstructor " +
           "AND r.estado = 'ACTIVA' " +
           "AND r.fechaInicio >= :inicioDia " +
           "AND r.fechaInicio < :finDia")
    long contarReservasActivasDelInstructorEnElDia(
            @Param("nombreInstructor") String nombreInstructor,
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("finDia") LocalDateTime finDia
    );

    @Query("SELECT r FROM Reserva r " +
           "WHERE r.ambiente.id = :ambienteId " +
           "AND r.estado = 'ACTIVA' " +
           "AND r.fechaInicio < :finDia " +
           "AND r.fechaFin > :inicioDia")
    List<Reserva> findReservasActivasDeAmbienteEnFecha(
            @Param("ambienteId") Long ambienteId,
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("finDia") LocalDateTime finDia
    );

    @Query("SELECT r FROM Reserva r " +
           "WHERE r.estado = 'ACTIVA' " +
           "AND r.fechaInicio < :fin " +
           "AND r.fechaFin > :inicio")
    List<Reserva> findReservasActivasEnRango(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    @Query("SELECT r FROM Reserva r " +
           "WHERE r.ambiente.id = :ambienteId " +
           "AND r.estado IN ('ACTIVA', 'FINALIZADA') " +
           "AND r.fechaInicio < :finDia " +
           "AND r.fechaFin > :inicioDia")
    List<Reserva> findReservasParaReporte(
            @Param("ambienteId") Long ambienteId,
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("finDia") LocalDateTime finDia
    );

    List<Reserva> findByEstado(EstadoReserva estado);
}