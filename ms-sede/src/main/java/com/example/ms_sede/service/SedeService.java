package com.example.ms_sede.service;

import com.example.ms_sede.client.EncargadoClient;
import com.example.ms_sede.dto.SedeRequest;
import com.example.ms_sede.dto.SedeResponse;
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
    private final EncargadoClient encargadoClient;

    public SedeResponse createSede(SedeRequest dto, String token) {

        log.info("Agregar Sede", keyValue("nombre", dto.getNombre()));

        var encargado = encargadoClient.obtenerEncargado(dto.getIdEncargado(), token);

        if (encargado == null) {
            throw new RuntimeException("encargado no existe");
        }

        Sede sede = sedeRepository.save(
                new Sede(null,dto.getNombre(),dto.getDireccion(),dto.getHoraApertura()
                        ,dto.getHoraCierre(),dto.getIdEncargado())
        );

        return mapToResponse(sede, token);
    }

    public List<SedeResponse> listar(String token) {
        log.info("Listando todas las Sedes");
        return sedeRepository.findAll()
                .stream()
                .map(i -> mapToResponse(i,token))
                .toList();
    }

    public SedeResponse obtenerSede(Long id,  String token) {
        log.info("Buscando Sede {}", keyValue("id",id));
        Sede sede1 = sedeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sede no encontrada"));
        return mapToResponse(sede1, token);    }

    public SedeResponse actualizarSede(Long id, SedeRequest dto, String token) {

        var encargado = encargadoClient.obtenerEncargado(dto.getIdEncargado(), token);
        if (encargado == null) {
            throw new RuntimeException("mantenimiento no existe");
        }
        Sede sede1 = sedeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no Encontrado"));;
        log.info("Actualizando Sede de ID {}", keyValue("id", id));
        sede1.setNombre(dto.getNombre());
        sede1.setDireccion(dto.getDireccion());
        sede1.setHoraCierre(dto.getHoraCierre());
        sede1.setHoraApertura(dto.getHoraApertura());
        sede1.setIdEncargado(dto.getIdEncargado());
        return mapToResponse(sedeRepository.save(sede1), token);
    }

    public void eliminarSede(Long id) {
        log.warn("Eliminando Sede de ID {}", keyValue("id", id));
        sedeRepository.deleteById(id);
    }

    private SedeResponse mapToResponse(Sede s, String token) {
        var encargado = encargadoClient.obtenerEncargado(s.getId(), token);
        return SedeResponse.builder()
                .id(s.getId())
                .nombre(s.getNombre())
                .direccion(s.getDireccion())
                .horaApertura(s.getHoraApertura())
                .horaCierre(s.getHoraCierre())
                .infoEncargado(encargado)
                .build();
    }
}
