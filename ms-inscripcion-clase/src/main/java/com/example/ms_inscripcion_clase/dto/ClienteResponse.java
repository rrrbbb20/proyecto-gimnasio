package com.example.ms_inscripcion_clase.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ClienteResponse {
    private Long id;
    private String nombreCompletoCliente;
    private String run;
    private LocalDate fechaNacCliente;
    private String tipoPlan;

}
