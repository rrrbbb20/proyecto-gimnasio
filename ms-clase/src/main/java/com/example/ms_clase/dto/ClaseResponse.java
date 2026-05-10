package com.example.ms_clase.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaseResponse {

    private String nombreClase;
    private String descripcion;
    private String nivelDeClase;
    private LocalDate fechaRealizacion;
    private LocalTime horaRealizacion;
    private Integer cupos;
    private EntrenadorResponse idEntrenador;
    private Boolean estado;

}
