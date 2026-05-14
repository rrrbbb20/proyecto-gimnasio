package com.example.ms_entrenador.service;


import com.example.ms_entrenador.dto.EntrenadorRequest;
import com.example.ms_entrenador.dto.EntrenadorResponse;
import com.example.ms_entrenador.model.Entrenador;
import com.example.ms_entrenador.repository.EntrenadorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntrenadorService {

    private final EntrenadorRepository repository;
    //despues ver si cual dejar
    /*public Entrenador add(EntrenadorRequest request){
        log.info("Crear Entrenador", keyValue("nombre", request.getNombreCompleto()));
        Entrenador entrenador1 = new Entrenador();
        entrenador1.setNombreCompleto(request.getNombreCompleto());
        entrenador1.setRun(request.getRun());
        entrenador1.setFechaNacimiento(request.getFechaNacimiento());
        return repository.save(entrenador1);
    }*/
    public EntrenadorResponse add(EntrenadorRequest request){
        log.info("Crear Entrenador", keyValue("nombre", request.getNombreCompleto()));
        Entrenador entrenador1 = new Entrenador();
        entrenador1.setNombreCompleto(request.getNombreCompleto());
        entrenador1.setRun(request.getRun());
        entrenador1.setFechaNacimiento(request.getFechaNacimiento());
        Entrenador saveEntrenador = repository.save(entrenador1);
        log.info("Entrenador creado correctamente",
                keyValue("id", saveEntrenador.getId())
        );
        return mapToResponse(saveEntrenador);


    }

    /*public Entrenador findById(Long id){
        log.info("Obtener Entrenador", keyValue("id", id));
        return repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Entrenador no encontrado"));
    }*/
    public EntrenadorResponse findById(Long id){
        log.info("Obtener Entrenador", keyValue("id", id));
        Entrenador entrenador1 = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Entrenador no encontrado"));
        log.info("Entrenador encontrado",
                keyValue("id", entrenador1.getId()),
                keyValue("nombre", entrenador1.getNombreCompleto())
        );
        return mapToResponse(entrenador1);
    }

    /*public List<Entrenador> getAll(){
        log.info("Listar Entrenadores");
        return repository.findAll();
    }*/
    public List<EntrenadorResponse> getAll(){
        log.info("Listando entrenadores");
        return repository.findAll()
                .stream()
                .map(e -> mapToResponse(e))
                .toList();

    }
    public EntrenadorResponse update(Long id , EntrenadorRequest e){

        Entrenador entrenador1 = repository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Entrenador no Encontrado"));
        log.info("Actualizar Entrenador", keyValue("id", id));
        entrenador1.setId(id);
        entrenador1.setNombreCompleto(e.getNombreCompleto());
        entrenador1.setRun(e.getRun());
        entrenador1.setFechaNacimiento(e.getFechaNacimiento());
        Entrenador updateEntrenador = repository.save(entrenador1);
        log.info("Entrenador actualizado correctamente",
                keyValue("id", updateEntrenador.getId())
        );

        return mapToResponse(updateEntrenador);
    }

    public void delete(Long id){
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("No se puede eliminar entrenador no encontrado");
        }
        log.info("Eliminar Entrenador", keyValue("id", id));
        repository.deleteById(id);
        log.info("Entrenador eliminado correctamente",
                keyValue("id", id));
    }

    private EntrenadorResponse mapToResponse(Entrenador e) {
        log.info("Mapeando entrenador",
                keyValue("id", e.getId())
        );
        return EntrenadorResponse.builder()
                .id(e.getId())
                .nombreCompleto(e.getNombreCompleto())
                .run(e.getRun())
                .fechaNacimiento(e.getFechaNacimiento())
                .build();
    }
}
