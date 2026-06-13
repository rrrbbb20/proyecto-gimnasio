package com.proyectogimnasio.rutina.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class RutinaRequest {
    @NotBlank(message = "El nombre de la rutina es obligatorio")
    private String nombreRutina;

    @NotBlank(message = "Debe introducir una descripción a la rutina")
    private String descripcionRutina;

    private List<DetallesEjercicioRequest> detalles;
}