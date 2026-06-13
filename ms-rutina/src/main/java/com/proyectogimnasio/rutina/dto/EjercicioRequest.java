package com.proyectogimnasio.rutina.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EjercicioRequest {
    @NotBlank(message = "Debe introducir un nombre al ejercicio")
    private String nombreEjercicio;

    @NotBlank(message = "Debe especificar qué zona se ejercita")
    private String zonaEjercitada;

    @NotNull(message = "Debe incluir las repeticiones del ejercicio")
    private Integer repeticiones;
}