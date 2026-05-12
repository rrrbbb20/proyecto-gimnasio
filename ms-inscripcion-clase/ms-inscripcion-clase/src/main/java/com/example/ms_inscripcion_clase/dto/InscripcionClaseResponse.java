package com.example.ms_inscripcion_clase.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class InscripcionClaseResponse {
    private Long id;
    private Long idClase;
    private Long idCliente;
    private LocalDate fechaInscripcion;
    private LocalTime horaInscripcion;
}
