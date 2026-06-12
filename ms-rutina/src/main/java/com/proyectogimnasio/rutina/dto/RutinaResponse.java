package com.proyectogimnasio.rutina.dto;

import com.proyectogimnasio.rutina.model.DetallesEjercicio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RutinaResponse {
    private Long id;
    private String nombreRutina;
    private String descripcionRutina;
    private Set<DetallesEjercicio> detalles;
}
