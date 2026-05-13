package com.example.ms_inscripcion_clase.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class InscripcionClaseRequest {

    @NotNull(message = "Debe ingresar id de la clase")
    private Long idClase;
    @NotNull(message = "Debe ingresar id del inscrito")
    private Long idCliente;
    private LocalDate fechaInscripcion;
    private LocalTime horaInscripcion;

}
