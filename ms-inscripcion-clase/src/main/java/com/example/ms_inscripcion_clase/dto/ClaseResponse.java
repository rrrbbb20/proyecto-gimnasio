package com.example.ms_inscripcion_clase.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ClaseResponse {

    private Long id;
    private String nombreClase;
    private String descripcion;
    private String nivelDeClase;
    private LocalDate fechaRealizacion;
    private LocalTime horaRealizacion;
    private Integer cupos;
    private Boolean estado;
    private Long idEntrenador;
}
