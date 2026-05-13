package com.example.ms_sede.service;

import com.example.ms_sede.dto.SedeRequest;
import com.example.ms_sede.model.Sede;
import com.example.ms_sede.repository.SedeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.keyValue;


@Service
@RequiredArgsConstructor
@Slf4j
@Builder
public class SedeService {

    private final SedeRepository sedeRepository;

    public Sede createSede(SedeRequest dto) {

        log.info("Crear Sede", keyValue("nombre", dto.getNombre()));

        Sede sede = new Sede(null,dto.getNombre(),dto.getDireccion(),dto.getHoraApertura(), dto.getHoraCierre());
        return sedeRepository.save(sede);
    }

    public List<Sede> listar() {
        log.info("Listando todas las Sedes");
        return sedeRepository.findAll();
    }

    public Sede obtenerSede(Long id) {
        log.info("Buscando Sede {}", keyValue("id",id));
        return sedeRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Sede no encontrado"));
    }

    public Sede actualizarSede(Long id, SedeRequest dto) {
        log.info("Actualizando Sede de ID {}", keyValue("id", id));

        Sede sede = obtenerSede(id);
        sede.setNombre(dto.getNombre());
        sede.setDireccion(dto.getDireccion());
        sede.setHoraApertura(dto.getHoraApertura());
        sede.setHoraCierre(dto.getHoraCierre());
        return sedeRepository.save(sede);
    }

    public void eliminarSede(Long id) {
        log.warn("Eliminando Sede de ID {}", keyValue("id", id));
        sedeRepository.deleteById(id);
    }
    //falta:Agregar, Actualizar y eliminar
}
