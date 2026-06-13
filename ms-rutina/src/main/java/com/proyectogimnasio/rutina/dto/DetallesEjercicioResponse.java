package com.proyectogimnasio.rutina.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetallesEjercicioResponse {
    private Long id;
    private Long ejercicioId;
    private String nombreEjercicio;
    private String zonaEjercitada;
    private Integer repeticiones;
    private Integer numeroEjercicios;
    private String duracionRutina;
    private String tiempoDescanso;
}