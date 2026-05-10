package com.example.ms_clase.service;


import com.example.ms_clase.client.EntrenadorClient;
import com.example.ms_clase.dto.ClaseRequest;
import com.example.ms_clase.dto.ClaseResponse;
import com.example.ms_clase.dto.EntrenadorResponse;
import com.example.ms_clase.repository.ClaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClaseService {

    private ClaseRepository repository;
    private final EntrenadorClient entrenadorClient;
    public ClaseResponse add(ClaseRequest c,String token){
        log.info("Anadir Clase", keyValue("Nombre de Clase", c.getNombreClase()));
        var entrenador = entrenadorClient.getEntrenador(c.getIdEntrenador(), token);
        if
        return null;
    }

}
