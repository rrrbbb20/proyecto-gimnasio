package com.example.ms_inscripcion_clase.service;


import com.example.ms_inscripcion_clase.dto.IncripcionClaseRequest;
import com.example.ms_inscripcion_clase.dto.InscripcionClaseResponse;
import com.example.ms_inscripcion_clase.repository.InscripcionClaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InscripcionClaseService {

    private InscripcionClaseRepository repository;

    public InscripcionClaseResponse add(IncripcionClaseRequest ir){


    }
}
