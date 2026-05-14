package com.example.ms_rutina.service;

import com.example.ms_rutina.dto.EjercicioRequest;
import com.example.ms_rutina.dto.EjercicioResponse;
import com.example.ms_rutina.dto.RutinaRequest;
import com.example.ms_rutina.dto.RutinaResponse;
import com.example.ms_rutina.model.Ejercicio;
import com.example.ms_rutina.model.Rutina;
import com.example.ms_rutina.repository.EjercicioRepository;
import com.example.ms_rutina.repository.RutinaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class RutinaService {

    private final RutinaRepository rutinaRepository;

    private final EjercicioRepository ejercicioRepository;

    public RutinaResponse addRutina (RutinaRequest request){
        log.info("Crear Rutina", keyValue("nombre", request.getNombreRutina()));
        Rutina rutina1 = new Rutina();
        rutina1.setNombreRutina(request.getNombreRutina());
        rutina1.setDuracion(request.getDuracion());
        rutina1.setTiempoDescanso(request.getTiempoDescanso());

        if (request.getEjerciciosId() !=null && !request.getEjerciciosId().isEmpty()){
            List<Ejercicio> ejercicios = ejercicioRepository.findAllById(request.getEjerciciosId());
            rutina1.setEjercicios(new HashSet<>(ejercicios));
        }
        Rutina rutinaLista = rutinaRepository.save(rutina1);
        return mapToResponseRutina(rutinaLista);
    }
    public EjercicioResponse addEjercicio (EjercicioRequest ejercicioRequest){
        log.info("Crear Ejercicio", keyValue("nombre", ejercicioRequest.getNombreEjercicio()));
        Ejercicio ejercicio1 = new Ejercicio();
        ejercicio1.setNombreEjercicio(ejercicioRequest.getNombreEjercicio());
        ejercicio1.setTipoEjercicio(ejercicioRequest.getTipoEjercicio());
        ejercicio1.setZonaEjercitada(ejercicioRequest.getZonaEjercitada());
        ejercicio1.setRepeticiones(ejercicioRequest.getRepeticiones());
        ejercicioRepository.save(ejercicio1);
        return mapToResponseEjercicio(ejercicio1);

    }
    public RutinaResponse findByIdRutina(Long id){
        log.info("Obtener Rutina", keyValue("id", id));
        Rutina rutina1 = rutinaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rutina no encontrada"));
        return mapToResponseRutina(rutina1);
    }
    public EjercicioResponse findByIdEjercicio(Long id){
        log.info("Obtener Rutina", keyValue("id", id));
        Ejercicio ejercicio1 = ejercicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ejercicio no encontrado"));
        return mapToResponseEjercicio(ejercicio1);
    }

    public List<RutinaResponse> getAllRutinas(){
        return rutinaRepository.findAll().stream().map(rutina -> mapToResponseRutina(rutina)).toList();
    }

    public List<EjercicioResponse> getAllEjercicios(){
        return ejercicioRepository.findAll().stream().map(ejercicio -> mapToResponseEjercicio(ejercicio)).toList();
    }

    public RutinaResponse updateRutina(Long id , RutinaRequest r) {
        Rutina rutina1 = rutinaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rutina no Encontrada"));
        log.info("Actualizar Rutina", keyValue("id", id));
        rutina1.setId(id);
        rutina1.setNombreRutina(r.getNombreRutina());
        rutina1.setDuracion(r.getDuracion());
        rutina1.setTiempoDescanso(r.getTiempoDescanso());
        if (r.getEjerciciosId()!=null){
            List<Ejercicio> ejerciciosNuevos = ejercicioRepository.findAllById(r.getEjerciciosId());
            rutina1.setEjercicios(new HashSet<>(ejerciciosNuevos));
        }
        rutinaRepository.save(rutina1);
        return mapToResponseRutina(rutina1);
    }
    public EjercicioResponse updateEjercicio(Long idEjercicio , EjercicioRequest e) {
        Ejercicio ejercicio1 = ejercicioRepository.findById(idEjercicio)
                .orElseThrow(() -> new EntityNotFoundException("Ejercicio no Encontrado"));
        log.info("Actualizar Rutina", keyValue("id", idEjercicio));
        ejercicio1.setNombreEjercicio(e.getNombreEjercicio());
        ejercicio1.setTipoEjercicio(e.getTipoEjercicio());
        ejercicio1.setZonaEjercitada(e.getZonaEjercitada());
        ejercicio1.setRepeticiones(e.getRepeticiones());
        ejercicioRepository.save(ejercicio1);
        return mapToResponseEjercicio(ejercicio1);
    }
    public void deleteRutina(Long id){
        log.info("Eliminar Rutina", keyValue("id", id));
        rutinaRepository.deleteById(id);
    }
    public void deleteEjercicio(Long idEjercicio){
        log.info("Eliminar Rutina", keyValue("id", idEjercicio));
        ejercicioRepository.deleteById(idEjercicio);
    }

    private RutinaResponse mapToResponseRutina(Rutina r) {
        List<EjercicioResponse> ejerciciosResponse = r.getEjercicios() != null ?
                r.getEjercicios().stream().map(this::mapToResponseEjercicio).toList() :
                List.of();
        return RutinaResponse.builder()
                .id(r.getId()) //
                .nombreRutina(r.getNombreRutina())
                .duracion(r.getDuracion())
                .tiempoDescanso(r.getTiempoDescanso())
                .ejercicios(ejerciciosResponse)
                .build();

    }
    private EjercicioResponse mapToResponseEjercicio(Ejercicio e) {
        return EjercicioResponse.builder()
                .idEjercicio(e.getIdEjercicio()) //
                .nombreEjercicio(e.getNombreEjercicio())
                .tipoEjercicio(e.getTipoEjercicio())
                .zonaEjercitada(e.getZonaEjercitada())
                .repeticiones(e.getRepeticiones())
                .build();

    }

}
