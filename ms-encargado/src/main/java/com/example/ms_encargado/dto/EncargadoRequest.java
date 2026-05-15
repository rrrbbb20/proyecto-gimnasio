package com.example.ms_encargado.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
@Data
public class EncargadoRequest {

    @NotBlank(message="Debe ingresar nombre")
    private String nombreCompleto;
    @NotBlank(message="Debe ingresar run")
    private String run;
    @NotBlank(message = "Debe ingresar direccion")
    private String direccion;
    @NotNull(message = "debe ingresar fecha")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fechaNacimiento;
}
