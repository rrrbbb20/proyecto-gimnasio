package com.proyectogimnasio.rutina.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RutinaResponse {
    private Long id;
    private String nombreRutina;
    private String descripcionRutina;
    private List<DetallesEjercicioResponse> detalles;
}