package com.example.demo_agenda_sena.dto;

import com.example.demo_agenda_sena.enums.TipoAmbiente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AmbienteRequestDTO {

    @NotBlank(message = "El nombre del ambiente es obligatorio")
    private String nombre;

    @NotNull(message = "El tipo de ambiente es obligatorio (SALA, LABORATORIO, AUDITORIO)")
    private TipoAmbiente tipo;

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser un número positivo mayor a 0")
    private Integer capacidad;

    private Boolean activo; // Opcional, si no se envía por defecto es true

    public AmbienteRequestDTO() {
        this.activo = true; // Por defecto, activo
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoAmbiente getTipo() {
        return tipo;
    }

    public void setTipo(TipoAmbiente tipo) {
        this.tipo = tipo;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}