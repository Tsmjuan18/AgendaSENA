package com.example.demo_agenda_sena.dto;

import com.example.demo_agenda_sena.enums.EstadoReserva;
import com.example.demo_agenda_sena.entitys.Reserva;

import java.time.LocalDateTime;

public class ReservaResponseDTO {

    private Long id;
    private Long ambienteId;
    private String nombreAmbiente;
    private String nombreInstructor;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer numeroAprendices;
    private EstadoReserva estado;

    public ReservaResponseDTO() {
    }

    public static ReservaResponseDTO desdeEntidad(Reserva reserva) {
        ReservaResponseDTO dto = new ReservaResponseDTO();
        dto.id = reserva.getId();
        dto.ambienteId = reserva.getAmbiente().getId();
        dto.nombreAmbiente = reserva.getAmbiente().getNombre();
        dto.nombreInstructor = reserva.getNombreInstructor();
        dto.fechaInicio = reserva.getFechaInicio();
        dto.fechaFin = reserva.getFechaFin();
        dto.numeroAprendices = reserva.getNumeroAprendices();
        dto.estado = reserva.getEstado();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public Long getAmbienteId() {
        return ambienteId;
    }

    public String getNombreAmbiente() {
        return nombreAmbiente;
    }

    public String getNombreInstructor() {
        return nombreInstructor;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public Integer getNumeroAprendices() {
        return numeroAprendices;
    }

    public EstadoReserva getEstado() {
        return estado;
    }
}