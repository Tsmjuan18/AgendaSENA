package com.example.demo_agenda_sena.entitys;

import com.example.demo_agenda_sena.enums.TipoAmbiente;
import jakarta.persistence.*;

@Entity
@Table(name = "ambientes")
public class Ambiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAmbiente tipo;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(nullable = false)
    private boolean activo;

    public Ambiente() {
    }

    public Ambiente(String nombre, TipoAmbiente tipo, Integer capacidad, boolean activo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
