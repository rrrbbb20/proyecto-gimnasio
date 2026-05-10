package com.example.ms_entrenador.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EntrenadorRequest {

    @NotBlank(message = "Debe ingresar nombre")
    private String nombreCompleto;
    @NotBlank(message = "Debe ingresar run")
    private String run;
    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fechaNacimiento;

}
