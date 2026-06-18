package com.example.ms_inscripcion_clase.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ClienteResponse {
    private Long id;
    private String nombres;
    private String apellidos;
    private String run;
    private String correo;
    private LocalDate fechaNac;
    private Long idPlan;
}
