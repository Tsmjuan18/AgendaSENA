package com.example.demo_agenda_sena.dto;

import com.example.demo_agenda_sena.enums.TipoAmbiente;
import com.example.demo_agenda_sena.entitys.Ambiente;

public class AmbienteResponseDTO {

    private Long id;
    private String nombre;
    private TipoAmbiente tipo;
    private Integer capacidad;
    private Boolean activo;

    public AmbienteResponseDTO() {
    }

    // Método factory para convertir de Entidad a DTO
    public static AmbienteResponseDTO desdeEntidad(Ambiente ambiente) {
        if (ambiente == null) {
            return null;
        }

        AmbienteResponseDTO dto = new AmbienteResponseDTO();
        dto.id = ambiente.getId();
        dto.nombre = ambiente.getNombre();
        dto.tipo = ambiente.getTipo();
        dto.capacidad = ambiente.getCapacidad();
        dto.activo = ambiente.isActivo();
        return dto;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoAmbiente getTipo() {
        return tipo;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public Boolean getActivo() {
        return activo;
    }

    // Setters (opcionales, útiles para pruebas o si quieres construir el DTO manualmente)
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(TipoAmbiente tipo) {
        this.tipo = tipo;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "AmbienteResponseDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", capacidad=" + capacidad +
                ", activo=" + activo +
                '}';
    }
}