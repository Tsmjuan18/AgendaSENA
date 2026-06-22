package com.example.demo_agenda_sena.config;

import com.example.demo_agenda_sena.enums.EstadoReserva;
import com.example.demo_agenda_sena.enums.TipoAmbiente;
import com.example.demo_agenda_sena.entitys.Ambiente;
import com.example.demo_agenda_sena.entitys.Reserva;
import com.example.demo_agenda_sena.repository.AmbienteRepository;
import com.example.demo_agenda_sena.repository.ReservaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final AmbienteRepository ambienteRepository;
    private final ReservaRepository reservaRepository;

    public DataLoader(AmbienteRepository ambienteRepository, ReservaRepository reservaRepository) {
        this.ambienteRepository = ambienteRepository;
        this.reservaRepository = reservaRepository;
    }

    @Override
    public void run(String... args) {

        // ===== AMBIENTES =====
        Ambiente aula101               = new Ambiente("Aula 101",                    TipoAmbiente.SALA,        30,  true);
        Ambiente aula102               = new Ambiente("Aula 102",                    TipoAmbiente.SALA,        25,  true);
        Ambiente laboratorioRedes      = new Ambiente("Laboratorio de Redes",        TipoAmbiente.LABORATORIO, 20,  true);
        Ambiente auditorioPrincipal    = new Ambiente("Auditorio Principal",         TipoAmbiente.AUDITORIO,   100, true);
        Ambiente aulaInactiva          = new Ambiente("Aula 103 (Inactiva)",         TipoAmbiente.SALA,        15,  false);
        Ambiente laboratorioProgramacion = new Ambiente("Laboratorio de Programación", TipoAmbiente.LABORATORIO, 25, true);

        ambienteRepository.save(aula101);
        ambienteRepository.save(aula102);
        ambienteRepository.save(laboratorioRedes);
        ambienteRepository.save(auditorioPrincipal);
        ambienteRepository.save(aulaInactiva);
        ambienteRepository.save(laboratorioProgramacion);

        System.out.println("Ambientes creados: " + ambienteRepository.count());

        // ===== FECHAS FIJAS =====
        // Martes 23 → reserva FINALIZADA (pasado) → para el reporte de ocupación
        // Jueves 25 → reservas ACTIVAS          → para las pruebas en vivo el miércoles
        // Viernes 26 → reserva ACTIVA extra      → para probar disponibilidad

        // ----- MARTES 23 (ayer de la presentación) -----
        LocalDateTime martes8  = LocalDateTime.of(2026, 6, 23, 8,  0, 0);
        LocalDateTime martes10 = LocalDateTime.of(2026, 6, 23, 10, 0, 0);

        // ----- JUEVES 25 (día siguiente a la presentación) -----
        LocalDateTime jueves8  = LocalDateTime.of(2026, 6, 25, 8,  0, 0);
        LocalDateTime jueves10 = LocalDateTime.of(2026, 6, 25, 10, 0, 0);
        LocalDateTime jueves10b= LocalDateTime.of(2026, 6, 25, 10, 0, 0);
        LocalDateTime jueves12 = LocalDateTime.of(2026, 6, 25, 12, 0, 0);
        LocalDateTime jueves14 = LocalDateTime.of(2026, 6, 25, 14, 0, 0);
        LocalDateTime jueves16 = LocalDateTime.of(2026, 6, 25, 16, 0, 0);
        LocalDateTime jueves16b= LocalDateTime.of(2026, 6, 25, 16, 0, 0);
        LocalDateTime jueves18 = LocalDateTime.of(2026, 6, 25, 18, 0, 0);

        // ----- VIERNES 26 -----
        LocalDateTime viernes13 = LocalDateTime.of(2026, 6, 26, 13, 0, 0);
        LocalDateTime viernes15 = LocalDateTime.of(2026, 6, 26, 15, 0, 0);

        // ===== RESERVAS =====

        // Reserva 1  CLAVE PARA SOLAPAMIENTO
        // Aula 101 — jueves 8:00 a 10:00 — ACTIVA
        // El profesor pedirá intentar crear una reserva de 9:00 a 11:00 aquí → debe rechazarse
        Reserva reserva1 = new Reserva(
                aula101, "Carlos Pérez",
                jueves8, jueves10,
                20, EstadoReserva.ACTIVA);

        // Reserva 2
        // Lab. Redes — jueves 14:00 a 16:00 — ACTIVA
        Reserva reserva2 = new Reserva(
                laboratorioRedes, "María Gómez",
                jueves14, jueves16,
                15, EstadoReserva.ACTIVA);

        // Reserva 3
        // Aula 102 — jueves 10:00 a 12:00 — CANCELADA
        // Demuestra que una reserva cancelada NO bloquea el horario
        // (se puede crear otra reserva en ese mismo horario)
        Reserva reserva3 = new Reserva(
                aula102, "Juan Rodríguez",
                jueves10b, jueves12,
                10, EstadoReserva.CANCELADA);

        // Reserva 4
        // Auditorio — MARTES 8:00 a 10:00 — FINALIZADA
        // Para demostrar el reporte de ocupación con fecha=2026-06-23
        Reserva reserva4 = new Reserva(
                auditorioPrincipal, "Ana Martínez",
                martes8, martes10,
                50, EstadoReserva.FINALIZADA);

        // Reserva 5
        // Aula 101 — viernes 13:00 a 15:00 — ACTIVA
        // Para demostrar disponibilidad en otro día
        Reserva reserva5 = new Reserva(
                aula101, "Pedro Sánchez",
                viernes13, viernes15,
                25, EstadoReserva.ACTIVA);

        // Reserva 6
        // Lab. Programación — jueves 16:00 a 18:00 — ACTIVA
        Reserva reserva6 = new Reserva(
                laboratorioProgramacion, "Laura Fernández",
                jueves16b, jueves18,
                18, EstadoReserva.ACTIVA);

        reservaRepository.save(reserva1);
        reservaRepository.save(reserva2);
        reservaRepository.save(reserva3);
        reservaRepository.save(reserva4);
        reservaRepository.save(reserva5);
        reservaRepository.save(reserva6);

        System.out.println(" Reservas creadas: " + reservaRepository.count());
        System.out.println(" Datos listos para la sustentación del miércoles 25/06/2026!");
    }
}