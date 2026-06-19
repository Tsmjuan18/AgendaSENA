package com.example.demo_agenda_sena.entitys;

import com.example.demo_agenda_sena.enums.EstadoReserva;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ambiente_id", nullable = false)
    private Ambiente ambiente;

    @Column(nullable = false)
    private String nombreInstructor;

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaFin;

    @Column(nullable = false)
    private Integer numeroAprendices;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado;

    public Reserva() {
    }

    public Reserva(Ambiente ambiente, String nombreInstructor, LocalDateTime fechaInicio,
                    LocalDateTime fechaFin, Integer numeroAprendices, EstadoReserva estado) {
        this.ambiente = ambiente;
        this.nombreInstructor = nombreInstructor;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.numeroAprendices = numeroAprendices;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ambiente getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(Ambiente ambiente) {
        this.ambiente = ambiente;
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

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }
}