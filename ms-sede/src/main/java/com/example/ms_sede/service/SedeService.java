package com.example.ms_sede.service;

import com.example.ms_sede.dto.ApiResponse;
import com.example.ms_sede.dto.SedeDTO;
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

    public Sede createSede(SedeDTO dto) {
        log.info("Crear Sede", keyValue("nombre", dto.getNombre()));
    }

    public List<Sede> listar() {
        log.info("Listando todas las Sedes");
        return sedeRepository.findAll();
    }

    public Sede obtenerSede(Long id) {
        log.info("Obtendendo Sede de ID {}", id);
        return sedeRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Sede no encontrado"));
    }

    //falta:Agregar, Actualizar y eliminar
}
