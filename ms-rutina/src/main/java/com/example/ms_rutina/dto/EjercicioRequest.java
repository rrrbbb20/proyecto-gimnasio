package com.example.ms_rutina.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EjercicioRequest {
    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombreEjercicio;
    @NotBlank(message = "Debe de poner el tipo de ejercicio")
    private String tipoEjercicio;
    @NotBlank(message = "Debe agregar el area que se ejercita")
    private String zonaEjercitada;
    @NotNull(message = "Debe agregar las repeticiones del ejercicio")
    private Integer repeticiones;
}
