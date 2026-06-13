package com.proyectogimnasio.rutina.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DetallesEjercicioRequest {
    @NotNull(message = "El ID del ejercicio es obligatorio")
    private Long ejercicioId;
    @NotNull(message = "Debe especificar el número de ejercicios o series")
    private Integer numeroEjercicios;
    @NotBlank(message = "Debe especificar la duración")
    private String duracionRutina;
    @NotBlank(message = "Debe especificar el tiempo de descanso")
    private String tiempoDescanso;
}