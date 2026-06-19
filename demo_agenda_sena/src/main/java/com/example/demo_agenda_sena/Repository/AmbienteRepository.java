package com.example.demo_agenda_sena.repository;

import com.example.demo_agenda_sena.entitys.Ambiente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmbienteRepository extends JpaRepository<Ambiente, Long> {

    List<Ambiente> findByActivoTrue();
}

    

