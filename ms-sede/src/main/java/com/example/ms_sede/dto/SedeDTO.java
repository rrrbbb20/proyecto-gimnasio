package com.example.ms_sede.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SedeDTO {
    @NotBlank(message = "El nombre de la sede es obligatorio")
    private String nombre;

    @NotBlank(message = "La direccion es obligatoria")
    private String direccion;

    @NotBlank(message = "La hora de apertura tiene que existir")
    private int horaApertura;

    @NotBlank(message = "La hora de cierre tiene que existir")
    private int horaCierre;
}   
