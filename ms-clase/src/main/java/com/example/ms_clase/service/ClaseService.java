package com.example.ms_clase.service;


import com.example.ms_clase.client.EntrenadorClient;
import com.example.ms_clase.dto.ClaseRequest;
import com.example.ms_clase.dto.ClaseResponse;
import com.example.ms_clase.model.Clase;
import com.example.ms_clase.repository.ClaseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClaseService {

    private final ClaseRepository repository;
    private final EntrenadorClient entrenadorClient;


    public ClaseResponse add(ClaseRequest c,String token){
        log.info("Anadir Clase", keyValue("Nombre de Clase", c.getNombreClase()));
        var entrenador = entrenadorClient.getEntrenador(c.getIdEntrenador(), token);
        Clase clase1 = new Clase();
        clase1.setCupos(c.getCupos());
        clase1.setNivelDeClase(c.getNivelDeClase());
        clase1.setNombreClase(c.getNombreClase());
        clase1.setEstado(c.getEstado());
        clase1.setDescripcion(c.getDescripcion());
        clase1.setFechaRealizacion(c.getFechaRealizacion());
        clase1.setHoraRealizacion(c.getHoraRealizacion());
        clase1.setIdEntrenador(c.getIdEntrenador());
        repository.save(clase1);
        return mapToResponse(repository.save(clase1),token);
    }

    public ClaseResponse findById(Long id,String token){
        log.info("Buscar clase", keyValue("Id de la Clase", id));
        Clase clase = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada"));
        log.info("Clase Encontrada", keyValue("Nombre de Clase", clase.getNombreClase()));
        return mapToResponse(clase , token);

    }

    public List<ClaseResponse> getAll(String token){

        return repository.findAll().stream()
                .map(c -> mapToResponse(c,token))
                .toList();
    }

    public ClaseResponse update(Long id,ClaseRequest c,String token){
        Clase clase1 = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada"));
        clase1.setCupos(c.getCupos());
        clase1.setNivelDeClase(c.getNivelDeClase());
        clase1.setNombreClase(c.getNombreClase());
        clase1.setEstado(c.getEstado());
        clase1.setDescripcion(c.getDescripcion());
        clase1.setFechaRealizacion(c.getFechaRealizacion());
        clase1.setHoraRealizacion(c.getHoraRealizacion());
        clase1.setIdEntrenador(c.getIdEntrenador());
        return mapToResponse(repository.save(clase1),token);
    }

    public void delete(Long id ){
        repository.deleteById(id);

    }
    private ClaseResponse mapToResponse(Clase c, String token) {

        var entrenador1 = entrenadorClient.getEntrenador(c.getIdEntrenador(), token);

        return ClaseResponse.builder().nombreClase(c.getNombreClase()).
                descripcion(c.getDescripcion()).
                nivelDeClase(c.getNivelDeClase()).
                fechaRealizacion(c.getFechaRealizacion()).
                horaRealizacion(c.getHoraRealizacion()).
                cupos(c.getCupos()).
                idEntrenador(entrenador1).
                estado(c.getEstado()).
                build();

    }
}
