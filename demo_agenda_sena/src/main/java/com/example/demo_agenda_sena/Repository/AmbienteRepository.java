package com.example.demo_agenda_sena.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo_agenda_sena.Entitys.Ambiente;

@Repository
public interface AmbienteRepository extends JpaRepository<Ambiente,Long>{    
} 


    

