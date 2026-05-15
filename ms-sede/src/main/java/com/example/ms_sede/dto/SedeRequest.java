package com.example.ms_sede.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SedeRequest {
    @NotBlank(message = "El nombre de la sede es obligatorio")
    private String nombre;

    @NotBlank(message = "La direccion es obligatoria")
    private String direccion;

    @NotNull(message = "La hora de apertura tiene que existir")
    @Min(0)
    @Max(2359)
    private Integer horaApertura;

    @NotNull(message = "La hora de cierre tiene que existir")
    @Min(0)
    @Max(2359)
    private Integer horaCierre;

    @NotNull
    private Long idEncargado;
}   
