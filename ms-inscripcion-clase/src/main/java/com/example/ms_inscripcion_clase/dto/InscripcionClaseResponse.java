package com.example.ms_inscripcion_clase.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class InscripcionClaseResponse {
    private Long id;
    private ClaseResponse idClase;
    private ClienteResponse idCliente;
    private LocalDate fechaInscripcion;
    private LocalTime horaInscripcion;
}
