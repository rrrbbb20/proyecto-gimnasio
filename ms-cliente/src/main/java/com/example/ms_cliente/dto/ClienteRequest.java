package com.example.ms_cliente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClienteRequest {
    @NotBlank(message = "Ingrese un nombre")
    private String nombreCompletoCliente;
    @NotBlank(message = "Ingrese un run valido")
    private String run;
    @NotNull(message = "La fecha de nacimiento no puede estar vacia")
    @Past(message = "Debe insertar una fecha pasada")
    private LocalDate fechaNacCliente;
    @NotBlank(message = "Ingrese un plan valido")
    private String tipoPlan;


}
