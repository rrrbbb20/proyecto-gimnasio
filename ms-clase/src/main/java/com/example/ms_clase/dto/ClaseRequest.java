package com.example.ms_clase.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
public class ClaseRequest {

    @NotBlank(message = "nombre de clase obligatorio")
    private String nombreClase;
    private String descripcion;
    @NotBlank(message = "debe definir el nivel de la clase")
    private String nivelDeClase;
    @NotNull(message = "Debe ingresar fecha")
    @Future(message = "fecha debe ser superior a hoy")
    private LocalDate fechaRealizacion;
    @NotNull(message = "Debe ingresar hora de la clase")
    private LocalTime horaRealizacion;
    @NotNull(message="Obligatorio ingresar cupos")
    @Min(value =0, message ="Cupos Debe ser  igual o superior a 0")
    private Integer cupos;
    @NotNull(message = "Debe ingresar estado de la clase")
    private Boolean estado;
    @NotNull(message = "Debe ingresar id de entrenador")
    private Long idEntrenador;
}
