package com.proyectogimnasio.rutina.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EjercicioResponse {
    private Long id;
    private String nombreEjercicio;
    private String zonaEjercitada;
    private Integer repeticiones;
}