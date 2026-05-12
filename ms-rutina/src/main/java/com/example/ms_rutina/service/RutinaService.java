package com.example.ms_rutina.service;

import com.example.ms_rutina.dto.RutinaRequest;
import com.example.ms_rutina.dto.RutinaResponse;
import com.example.ms_rutina.model.Rutina;
import com.example.ms_rutina.repository.EjercicioRepository;
import com.example.ms_rutina.repository.RutinaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class RutinaService {

    private RutinaRepository rutinaRepository;

    private EjercicioRepository ejercicioRepository;

    public RutinaResponse add (RutinaRequest request){
        log.info("Crear Rutina", keyValue("nombre", request.getNombreRutina()));
        Rutina rutina1 = new Rutina();
        rutina1.setNombreRutina(request.getNombreRutina());
        rutina1.setDuracion(request.getDuracion());
        rutina1.setTiempoDescanso(request.getTiempoDescanso());
        rutina1.setEjercicio(request.getEjercicio());
        rutinaRepository.save(rutina1);
        return mapToResponse(rutina1);
    }
    public RutinaResponse findById(Long id){
        log.info("Obtener Rutina", keyValue("id", id));
        Rutina rutina1 = rutinaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rutina no encontrada"));
        return mapToResponse(rutina1);
    }
    public List<RutinaResponse> getAll(){
        return rutinaRepository.findAll().stream().map(rutina -> mapToResponse(rutina)).toList();
    }
    public RutinaResponse update(Long id , RutinaRequest r) {
        Rutina rutina1 = rutinaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rutina no Encontrada"));
        log.info("Actualizar Rutina", keyValue("id", id));
        rutina1.setId(id);
        rutina1.setNombreRutina(r.getNombreRutina());
        rutina1.setDuracion(r.getDuracion());
        rutina1.setTiempoDescanso(r.getTiempoDescanso());
        rutina1.setEjercicio(r.getEjercicio());
        rutinaRepository.save(rutina1);
        return mapToResponse(rutina1);
    }
    public void delete(Long id){
        log.info("Eliminar Rutina", keyValue("id", id));
        rutinaRepository.deleteById(id);
    }

    private RutinaResponse mapToResponse(Rutina r) {
        return RutinaResponse.builder()
                .id(r.getId()) //
                .nombreRutina(r.getNombreRutina())
                .duracion(r.getDuracion())
                .tiempoDescanso(r.getTiempoDescanso())
                .ejercicio(r.getEjercicio())
                .build();

    }

}
