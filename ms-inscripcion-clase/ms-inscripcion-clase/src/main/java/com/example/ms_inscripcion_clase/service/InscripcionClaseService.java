package com.example.ms_inscripcion_clase.service;


import com.example.ms_inscripcion_clase.client.ClaseClient;
import com.example.ms_inscripcion_clase.client.ClienteClient;
import com.example.ms_inscripcion_clase.dto.ClaseResponse;
import com.example.ms_inscripcion_clase.dto.ClienteResponse;
import com.example.ms_inscripcion_clase.dto.InscripcionClaseRequest;
import com.example.ms_inscripcion_clase.dto.InscripcionClaseResponse;
import com.example.ms_inscripcion_clase.model.InscripcionClase;
import com.example.ms_inscripcion_clase.repository.InscripcionClaseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class InscripcionClaseService {
    private ClaseClient claseClient;
    private ClienteClient clienteClient;
    private InscripcionClaseRepository repository;

    public InscripcionClaseResponse add(InscripcionClaseRequest ir, String token){

        ClaseResponse claseResponse1  = claseClient.getClase(ir.getIdClase(), token);
        if (claseResponse1 == null) {
            throw new EntityNotFoundException("Clase no encontrada");
        }
        ClienteResponse clienteResponse1  = clienteClient.getCliente(ir.getIdCliente(), token);

        if (clienteResponse1 == null) {
            throw new EntityNotFoundException("Cliente no encontrado");
        }
        InscripcionClase inscripcionClase1 = new InscripcionClase();
        inscripcionClase1.setFechaInscripcion(LocalDate.now());
        inscripcionClase1.setHoraInscripcion(LocalTime.now());
        inscripcionClase1.setIdClase(ir.getIdClase());
        inscripcionClase1.setIdCliente(ir.getIdCliente());
        return mapToResponse(inscripcionClase1,token);
    }

    public InscripcionClaseResponse findById(Long id,String token){
        log.info("Buscar inscripcion {}", keyValue("Id de la Inscripcion", id));
        InscripcionClase clase1= repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inscripcion a clase no encontrada"));
        log.info("Inscripcion encontrada{}", keyValue("Id de la Inscripcion", id));
        return mapToResponse(clase1,token);
    }
    public List<InscripcionClaseResponse> getAll(String token){

        return repository.findAll().stream()
                .map(ic -> mapToResponse(ic,token))
                .toList();
    }

    public InscripcionClaseResponse update(Long id, InscripcionClaseRequest ir, String token ){
        InscripcionClase ic = new InscripcionClase();
        verificarExistencia(ir,token);
        InscripcionClase clase1 = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inscripcion a clase no encontrada"));
        clase1.setFechaInscripcion(ir.getFechaInscripcion());
        clase1.setHoraInscripcion(ir.getHoraInscripcion());
        clase1.setIdClase(ir.getIdClase());
        clase1.setIdCliente(ir.getIdCliente());
        return mapToResponse(repository.save(clase1),token);
    }

    private InscripcionClaseResponse mapToResponse(InscripcionClase ic, String token) {

        var clase1 = claseClient.getClase(ic.getIdClase(), token);
        var cliente1 = clienteClient.getCliente(ic.getIdClase(), token);
        return InscripcionClaseResponse.builder()
                .id(ic.getId())
                .idClase(ic.getIdClase())
                .idCliente(ic.getIdCliente())
                .fechaInscripcion(ic.getFechaInscripcion())
                .horaInscripcion(ic.getHoraInscripcion())
                .build();
    }



    public boolean verificarExistencia(InscripcionClaseRequest ir ,String token){
        ClaseResponse claseResponse1  = claseClient.getClase(ir.getIdClase(), token);
        if (claseResponse1 == null) {
            throw new EntityNotFoundException("Clase no encontrada");
        }
        ClienteResponse clienteResponse1  = clienteClient.getCliente(ir.getIdCliente(), token);

        if (clienteResponse1 == null) {
            throw new EntityNotFoundException("Cliente no encontrado");
        }
        return true;
    }
}
