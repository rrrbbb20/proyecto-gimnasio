package com.proyectogimnasio.rutina.service;

import com.proyectogimnasio.rutina.dto.*;
import com.proyectogimnasio.rutina.model.DetallesEjercicio;
import com.proyectogimnasio.rutina.model.Ejercicio;
import com.proyectogimnasio.rutina.model.Rutina;
import com.proyectogimnasio.rutina.repository.DetallesEjercicioRepository;
import com.proyectogimnasio.rutina.repository.EjercicioRepository;
import com.proyectogimnasio.rutina.repository.RutinaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class RutinaService {
    private final EjercicioRepository ejercicioRepository;
    private final RutinaRepository rutinaRepository;
    private final DetallesEjercicioRepository detallesEjercicioRepository;

    @Transactional
    public RutinaResponse addRutina(RutinaRequest request, String token) {
        log.info("Crear Rutina", keyValue("nombre", request.getNombreRutina()));

        Rutina rutina = new Rutina();
        rutina.setNombreRutina(request.getNombreRutina());
        rutina.setDescripcionRutina(request.getDescripcionRutina());

        Rutina saveRutina = rutinaRepository.save(rutina);

        if (request.getDetalles() != null && !request.getDetalles().isEmpty()) {
            Set<DetallesEjercicio> detallesSet = procesarDetallesRequest(request.getDetalles(), saveRutina);
            saveRutina.setDetalles(detallesSet);
            saveRutina = rutinaRepository.save(saveRutina);
        }

        log.info("Rutina creada exitosamente", keyValue("idRutina", saveRutina.getId()));
        return mapToResponseRutina(saveRutina, token);
    }

    @Transactional
    public EjercicioResponse addEjercicio(EjercicioRequest request, String token) {
        log.info("Crear Ejercicio", keyValue("nombre", request.getNombreEjercicio()));

        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setNombreEjercicio(request.getNombreEjercicio());
        ejercicio.setZonaEjercitada(request.getZonaEjercitada());
        ejercicio.setRepeticiones(request.getRepeticiones());

        Ejercicio saveEjercicio = ejercicioRepository.save(ejercicio);
        log.info("Ejercicio creado exitosamente", keyValue("idEjercicio", saveEjercicio.getId()));
        return mapToResponseEjercicio(saveEjercicio, token);
    }

    @Transactional(readOnly = true)
    public RutinaResponse findRutina(Long id, String token) {
        log.info("Buscar rutina", keyValue("idRutina", id));
        Rutina rutina = rutinaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rutina no encontrada"));
        return mapToResponseRutina(rutina, token);
    }

    @Transactional(readOnly = true)
    public EjercicioResponse findEjercicio(Long id, String token) {
        log.info("Buscar ejercicio", keyValue("idEjercicio", id));
        Ejercicio ejercicio = ejercicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ejercicio no encontrado"));
        return mapToResponseEjercicio(ejercicio, token);
    }

    @Transactional(readOnly = true)
    public List<RutinaResponse> getRutinas(String token) {
        log.info("Listando rutinas");
        return rutinaRepository.findAll().stream()
                .map(rutina -> mapToResponseRutina(rutina, token))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EjercicioResponse> getEjercicios(String token) {
        log.info("Listando ejercicios del catálogo global");
        return ejercicioRepository.findAll().stream()
                .map(ejercicio -> mapToResponseEjercicio(ejercicio, token))
                .toList();
    }

    @Transactional
    public RutinaResponse updateRutina(Long id, RutinaRequest request, String token) {
        log.info("Actualizando rutina", keyValue("idRutina", id));
        Rutina rutina = rutinaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rutina no encontrada"));

        rutina.setNombreRutina(request.getNombreRutina());
        rutina.setDescripcionRutina(request.getDescripcionRutina());

        // Limpiamos los detalles anteriores para evitar inconsistencias o duplicados en la actualización
        if (rutina.getDetalles() != null) {
            rutina.getDetalles().clear();
            rutinaRepository.saveAndFlush(rutina);
        }

        if (request.getDetalles() != null && !request.getDetalles().isEmpty()) {
            Set<DetallesEjercicio> nuevosDetalles = procesarDetallesRequest(request.getDetalles(), rutina);
            rutina.setDetalles(nuevosDetalles);
        }

        Rutina saveRutina = rutinaRepository.save(rutina);
        log.info("Rutina actualizada exitosamente", keyValue("idRutina", saveRutina.getId()));
        return mapToResponseRutina(saveRutina, token);
    }

    @Transactional
    public EjercicioResponse updateEjercicio(Long id, EjercicioRequest request, String token) {
        log.info("Actualizando ejercicio", keyValue("idEjercicio", id));
        Ejercicio ejercicio = ejercicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ejercicio no encontrado"));

        ejercicio.setNombreEjercicio(request.getNombreEjercicio());
        ejercicio.setZonaEjercitada(request.getZonaEjercitada());
        ejercicio.setRepeticiones(request.getRepeticiones());

        Ejercicio saveEjercicio = ejercicioRepository.save(ejercicio);
        log.info("Ejercicio actualizado exitosamente", keyValue("idEjercicio", saveEjercicio.getId()));
        return mapToResponseEjercicio(saveEjercicio, token);
    }

    @Transactional
    public void deleteRutina(Long id) {
        log.info("Eliminando Rutina", keyValue("idRutina", id));
        if (!rutinaRepository.existsById(id)) {
            log.warn("Rutina a eliminar inexistente", keyValue("idRutina", id));
            throw new EntityNotFoundException("No se puede eliminar una rutina inexistente");
        }
        rutinaRepository.deleteById(id);
        log.info("Rutina eliminada correctamente", keyValue("idRutina", id));
    }

    @Transactional
    public void deleteEjercicio(Long id) {
        log.info("Eliminando ejercicio", keyValue("idEjercicio", id));
        if (!ejercicioRepository.existsById(id)) {
            log.warn("Ejercicio a eliminar inexistente", keyValue("idEjercicio", id));
            throw new EntityNotFoundException("No se puede eliminar un ejercicio inexistente");
        }
        ejercicioRepository.deleteById(id);
        log.info("Ejercicio eliminado correctamente", keyValue("idEjercicio", id));
    }
    

    private Set<DetallesEjercicio> procesarDetallesRequest(List<DetallesEjercicioRequest> detallesList, Rutina rutina) {
        return detallesList.stream().map(detReq -> {
            Ejercicio ejercicio = ejercicioRepository.findById(detReq.getEjercicioId())
                    .orElseThrow(() -> new EntityNotFoundException("Ejercicio global no encontrado con ID: " + detReq.getEjercicioId()));

            DetallesEjercicio detalle = new DetallesEjercicio();
            detalle.setRutina(rutina);
            detalle.setEjercicio(ejercicio);
            detalle.setNumeroEjercicios(detReq.getNumeroEjercicios());
            detalle.setDuracionRutina(detReq.getDuracionRutina());
            detalle.setTiempoDescanso(detReq.getTiempoDescanso());
            return detalle;
        }).collect(Collectors.toSet());
    }

    private RutinaResponse mapToResponseRutina(Rutina rutina, String token) {
        List<DetallesEjercicioResponse> detallesMapped = List.of();
        if (rutina.getDetalles() != null) {
            detallesMapped = rutina.getDetalles().stream()
                    .map(this::mapToResponseDetalles)
                    .toList();
        }
        return RutinaResponse.builder()
                .id(rutina.getId())
                .nombreRutina(rutina.getNombreRutina())
                .descripcionRutina(rutina.getDescripcionRutina())
                .detalles(detallesMapped)
                .build();
    }

    private EjercicioResponse mapToResponseEjercicio(Ejercicio ejercicio, String token) {
        return EjercicioResponse.builder()
                .id(ejercicio.getId())
                .nombreEjercicio(ejercicio.getNombreEjercicio())
                .zonaEjercitada(ejercicio.getZonaEjercitada())
                .repeticiones(ejercicio.getRepeticiones())
                .build();
    }

    private DetallesEjercicioResponse mapToResponseDetalles(DetallesEjercicio detalles) {
        return DetallesEjercicioResponse.builder()
                .id(detalles.getId())
                .ejercicioId(detalles.getEjercicio().getId())
                .nombreEjercicio(detalles.getEjercicio().getNombreEjercicio())
                .zonaEjercitada(detalles.getEjercicio().getZonaEjercitada())
                .repeticiones(detalles.getEjercicio().getRepeticiones())
                .numeroEjercicios(detalles.getNumeroEjercicios())
                .duracionRutina(detalles.getDuracionRutina())
                .tiempoDescanso(detalles.getTiempoDescanso())
                .build();
    }
}