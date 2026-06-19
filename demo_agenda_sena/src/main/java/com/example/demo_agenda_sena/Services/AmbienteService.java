package com.example.demo_agenda_sena.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo_agenda_sena.Entitys.Ambiente;
import com.example.demo_agenda_sena.Repository.AmbienteRepository;

@Service
public class AmbienteService {

    private AmbienteRepository ambienteRepository;


    public AmbienteService(AmbienteRepository ambienteRepository){
        this.ambienteRepository=ambienteRepository;

    }

    public List<Ambiente> getAllClassrooms(){

        return ambienteRepository.findAll();
    }

    public Optional<Ambiente>getIdClassroom(Long id){

        return ambienteRepository.findById(id);

    }

    public Ambiente createClassroom(Ambiente ambiente){

        return ambienteRepository.save(ambiente);


    }

    public Ambiente updateClassroom(Long id,Ambiente ambiente){

        Optional<Ambiente> ambienteFound= ambienteRepository.findById(id);

        if (ambienteFound.isEmpty()) {

            return null;
            
        }

        return ambienteRepository.save(ambiente);

    }

    public Ambiente delete(Long id){

        Optional<Ambiente> ambienteDeleted = ambienteRepository.findById(id);

        if (ambienteDeleted.isEmpty()) {

            return null;
            
        }

        ambienteRepository.delete(ambienteDeleted.get());
        return ambienteDeleted.get();



    }




    
}
