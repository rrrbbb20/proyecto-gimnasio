package com.example.ms_entrenador.service;


import com.example.ms_entrenador.dto.EntrenadorRequest;
import com.example.ms_entrenador.model.Entrenador;
import com.example.ms_entrenador.repository.EntrenadorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntrenadorService {

    private final EntrenadorRepository repository;

    public Entrenador add(EntrenadorRequest request){
        log.info("Crear Entrenador", keyValue("nombre", request.getNombreCompleto()));
        Entrenador entrenador1 = new Entrenador();
        entrenador1.setNombreCompleto(request.getNombreCompleto());
        entrenador1.setRun(request.getRun());
        entrenador1.setFechaNacimiento(request.getFechaNacimiento());
        return repository.save(entrenador1);
    }
    public Entrenador findById(Long id){
        log.info("Obtener Entrenador", keyValue("id", id));
        return repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Entrenador no encontrado"));
    }

    public List<Entrenador> getAll(){
        log.info("Listar Entrenadores");
        return repository.findAll();
    }
    public Entrenador update(Long id , EntrenadorRequest e){
        Entrenador entrenador1 = findById(id);
        log.info("Actualizar Entrenador", keyValue("id", id));
        entrenador1.setId(id);
        entrenador1.setNombreCompleto(e.getNombreCompleto());
        entrenador1.setRun(e.getRun());
        entrenador1.setFechaNacimiento(e.getFechaNacimiento());
        return entrenador1;
    }

    public void delete(Long id){
        log.info("Eliminar Entrenador", keyValue("id", id));
        repository.deleteById(id);
    }
}
