package com.proyectogimnasio.cliente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClienteRequest {
    @NotBlank(message = "El nombre no debe quedar en blanco")
    private String nombres;
    @NotBlank(message = "El apellido no debe de quedar en blanco")
    private String apellidos;
    @NotBlank(message = "El run es obligatorio")
    private String run;
    @NotNull(message = "El correo es obligatorio")
    private String correo;
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fechaNac;
    @NotNull(message = "Debe ingresar el id del plan")
    private Long idPlan;
}
