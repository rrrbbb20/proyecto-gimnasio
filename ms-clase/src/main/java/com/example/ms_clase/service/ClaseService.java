package com.example.ms_clase.service;


import com.example.ms_clase.client.EntrenadorClient;
import com.example.ms_clase.dto.ClaseRequest;
import com.example.ms_clase.dto.ClaseResponse;
import com.example.ms_clase.model.Clase;
import com.example.ms_clase.repository.ClaseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
        if(entrenador == null){
            log.warn("Entrenador no encontrado",
                    keyValue("idEntrenador", c.getIdEntrenador()));
            throw new EntityNotFoundException("Entrenador no encontrado");
        }
        Clase clase1 = new Clase();
        clase1.setCupos(c.getCupos());
        clase1.setNivelDeClase(c.getNivelDeClase());
        clase1.setNombreClase(c.getNombreClase());
        clase1.setEstado(c.getEstado());
        clase1.setDescripcion(c.getDescripcion());
        clase1.setFechaRealizacion(c.getFechaRealizacion());
        clase1.setHoraRealizacion(c.getHoraRealizacion());
        clase1.setIdEntrenador(c.getIdEntrenador());
        Clase saveClase =repository.save(clase1);
        log.info("Clase creada correctamente",
                keyValue("idClase", saveClase.getId())
        );

        return mapToResponse(saveClase,token);
    }

    public ClaseResponse findById(Long id,String token){
        log.info("Buscar clase", keyValue("Id de la Clase", id));
        Clase clase = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada"));
        log.info("Clase Encontrada", keyValue("Nombre de Clase", clase.getNombreClase()));
        return mapToResponse(clase , token);

    }

    public List<ClaseResponse> getAll(String token){
        log.info("Listando clases");
        return repository.findAll().stream()
                .map(c -> mapToResponse(c,token))
                .toList();
    }

    public ClaseResponse update(Long id,ClaseRequest c,String token){
        log.info("Actualizando clase",
                keyValue("idClase", id)
        );
        Clase clase1 = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada"));
        var entrenador = entrenadorClient.getEntrenador(c.getIdEntrenador(), token);

        if(entrenador == null){
            log.warn("Entrenador no encontrado",
                    keyValue("idEntrenador", c.getIdEntrenador())
            );
            throw new EntityNotFoundException("Entrenador no encontrado");
        }
        clase1.setCupos(c.getCupos());
        clase1.setNivelDeClase(c.getNivelDeClase());
        clase1.setNombreClase(c.getNombreClase());
        clase1.setEstado(c.getEstado());
        clase1.setDescripcion(c.getDescripcion());
        clase1.setFechaRealizacion(c.getFechaRealizacion());
        clase1.setHoraRealizacion(c.getHoraRealizacion());
        clase1.setIdEntrenador(c.getIdEntrenador());
        Clase updateClase = repository.save(clase1);
        log.info("Clase actualizada correctamente",
                keyValue("idClase", updateClase.getId())
        );
        return mapToResponse(updateClase,token);
    }

    public void delete(Long id ){
        log.info("Eliminando clase",
                keyValue("idClase", id)
        );

        if (!repository.existsById(id)) {
            log.warn("clase a eliminar inexistente",
                    keyValue("idClase", id)
            );
            throw new EntityNotFoundException("No se puede eliminar clase no encontrada");
        }
        repository.deleteById(id);
        log.info("Clase eliminada correctamente",
                keyValue("idClase", id)
        );
    }
    private ClaseResponse mapToResponse(Clase c, String token) {
        log.info("Mapeando clase",
                keyValue("idClase", c.getId())
        );
        var entrenador1 = entrenadorClient.getEntrenador(c.getIdEntrenador(), token);

        return ClaseResponse.builder().id(c.getId()).nombreClase(c.getNombreClase()).
                descripcion(c.getDescripcion()).
                nivelDeClase(c.getNivelDeClase()).
                fechaRealizacion(c.getFechaRealizacion()).
                horaRealizacion(c.getHoraRealizacion()).
                cupos(c.getCupos()).
                idEntrenador(entrenador1).
                estado(c.getEstado()).
                build();

    }

    @Transactional
    public void personaInscrita(Long id){
        /*Clase clase = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada"));;
        if (clase.getCupos() <= 0) {
            throw new RuntimeException("No quedan cupos disponibles para esta clase");
        }

        clase.setCupos(clase.getCupos() - 1 );
        log.info("Cupo actualizado", keyValue("idClase", clase.getId()));
        repository.save(clase);*/

        log.info("resta de cupo ", keyValue("idClase", id));

        int filasAfectadas = repository.restarCupo(id);

        if (filasAfectadas == 0) {
            log.warn("No se pudo restar cupo",
                    keyValue("idClase", id)
            );

            throw new RuntimeException("No se pudo restar el cupo: Clase no encontrada o llena.");
        }

        log.info("Cupo actualizado correctamente",
                keyValue("idClase", id)
        );

    }
    @Transactional
    public void removerInscripcion(Long id){
        /*Clase clase = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada"));;

        clase.setCupos(clase.getCupos() + 1 );
        log.info("Cupo actualizado", keyValue("idClase", clase.getId()));
        repository.save(clase);*/
        log.info("Sumando cupo de clase",
                keyValue("idClase", id)
        );

        int filasAfectadas = repository.sumarCupo(id);

        if (filasAfectadas == 0) {
            log.warn("No se pudo sumar cupo",
                    keyValue("idClase", id)
            );

            throw new RuntimeException("No se pudo restar el cupo: Clase no encontrada o llena.");
        }

        log.info("Cupo restaurado correctamente",
                keyValue("idClase", id)
        );
    }

}
