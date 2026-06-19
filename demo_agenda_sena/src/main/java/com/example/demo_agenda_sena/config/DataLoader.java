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
        // ===== 1. CREAR AMBIENTES DE PRUEBA (al menos 4) =====
        
        Ambiente aula101 = new Ambiente("Aula 101", TipoAmbiente.SALA, 30, true);
        Ambiente aula102 = new Ambiente("Aula 102", TipoAmbiente.SALA, 25, true);
        Ambiente laboratorioRedes = new Ambiente("Laboratorio de Redes", TipoAmbiente.LABORATORIO, 20, true);
        Ambiente auditorioPrincipal = new Ambiente("Auditorio Principal", TipoAmbiente.AUDITORIO, 100, true);
        Ambiente aulaInactiva = new Ambiente("Aula 103 (Inactiva)", TipoAmbiente.SALA, 15, false);
        Ambiente laboratorioProgramacion = new Ambiente("Laboratorio de Programación", TipoAmbiente.LABORATORIO, 25, true);

        // Guardar ambientes
        ambienteRepository.save(aula101);
        ambienteRepository.save(aula102);
        ambienteRepository.save(laboratorioRedes);
        ambienteRepository.save(auditorioPrincipal);
        ambienteRepository.save(aulaInactiva);
        ambienteRepository.save(laboratorioProgramacion);

        System.out.println("✅ Ambientes de prueba creados: " + ambienteRepository.count());

        // ===== 2. CREAR RESERVAS DE PRUEBA =====
        
        LocalDateTime ahora = LocalDateTime.now();
        
        // Reserva 1: Aula 101 - Hoy 9:00 a 11:00 (ACTIVA)
        Reserva reserva1 = new Reserva(
                aula101,
                "Carlos Pérez",
                ahora.withHour(9).withMinute(0),
                ahora.withHour(11).withMinute(0),
                20,
                EstadoReserva.ACTIVA
        );

        // Reserva 2: Laboratorio de Redes - Hoy 14:00 a 16:00 (ACTIVA)
        Reserva reserva2 = new Reserva(
                laboratorioRedes,
                "María Gómez",
                ahora.withHour(14).withMinute(0),
                ahora.withHour(16).withMinute(0),
                15,
                EstadoReserva.ACTIVA
        );

        // Reserva 3: Aula 102 - Hoy 10:00 a 12:00 (CANCELADA)
        Reserva reserva3 = new Reserva(
                aula102,
                "Juan Rodríguez",
                ahora.withHour(10).withMinute(0),
                ahora.withHour(12).withMinute(0),
                10,
                EstadoReserva.CANCELADA
        );

        // Reserva 4: Auditorio - Ayer 8:00 a 10:00 (FINALIZADA)
        Reserva reserva4 = new Reserva(
                auditorioPrincipal,
                "Ana Martínez",
                ahora.withHour(8).withMinute(0).minusDays(1),
                ahora.withHour(10).withMinute(0).minusDays(1),
                50,
                EstadoReserva.FINALIZADA
        );

        // Reserva 5: Aula 101 - Mañana 13:00 a 15:00 (ACTIVA)
        Reserva reserva5 = new Reserva(
                aula101,
                "Pedro Sánchez",
                ahora.withHour(13).withMinute(0).plusDays(1),
                ahora.withHour(15).withMinute(0).plusDays(1),
                25,
                EstadoReserva.ACTIVA
        );

        // Reserva 6: Laboratorio de Programación - Hoy 16:00 a 18:00 (ACTIVA)
        Reserva reserva6 = new Reserva(
                laboratorioProgramacion,
                "Laura Fernández",
                ahora.withHour(16).withMinute(0),
                ahora.withHour(18).withMinute(0),
                18,
                EstadoReserva.ACTIVA
        );

        // Guardar reservas
        reservaRepository.save(reserva1);
        reservaRepository.save(reserva2);
        reservaRepository.save(reserva3);
        reservaRepository.save(reserva4);
        reservaRepository.save(reserva5);
        reservaRepository.save(reserva6);

        System.out.println("✅ Reservas de prueba creadas: " + reservaRepository.count());
        System.out.println("📊 Datos de carga completados exitosamente!");
    }
}