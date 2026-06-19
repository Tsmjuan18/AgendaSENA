package com.example.demo_agenda_sena.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class ReservaRequestDTO {

    @NotNull(message = "El ID del ambiente es obligatorio")
    private Long ambienteId;

    @NotBlank(message = "El nombre del instructor es obligatorio")
    private String nombreInstructor;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDateTime fechaFin;

    @NotNull(message = "El número de aprendices es obligatorio")
    @Positive(message = "El número de aprendices debe ser mayor a 0")
    private Integer numeroAprendices;

    public ReservaRequestDTO() {
    }

    // Getters y Setters
    public Long getAmbienteId() {
        return ambienteId;
    }

    public void setAmbienteId(Long ambienteId) {
        this.ambienteId = ambienteId;
    }

    public String getNombreInstructor() {
        return nombreInstructor;
    }

    public void setNombreInstructor(String nombreInstructor) {
        this.nombreInstructor = nombreInstructor;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getNumeroAprendices() {
        return numeroAprendices;
    }

    public void setNumeroAprendices(Integer numeroAprendices) {
        this.numeroAprendices = numeroAprendices;
    }
}