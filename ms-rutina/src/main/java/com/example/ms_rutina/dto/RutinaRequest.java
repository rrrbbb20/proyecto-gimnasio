package com.example.ms_rutina.dto;

import com.example.ms_rutina.model.Ejercicio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RutinaRequest {
    @NotBlank(message = "El nombre de la rutina no puede estar en blanco")
    private String nombreRutina;
    @NotBlank(message = "La duracion no puede quedar vacia")
    private String duracion;
    @NotNull(message = "Debe poner un tiempo de descanso")
    private Integer tiempoDescanso;
    private List<Long > ejerciciosId;
}
