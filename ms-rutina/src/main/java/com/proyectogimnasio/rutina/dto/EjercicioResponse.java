package com.proyectogimnasio.rutina.dto;

import com.proyectogimnasio.rutina.model.DetallesEjercicio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EjercicioResponse {
    private Long id;
    private String nombreEjercicio;
    private String zonaEjercitada;
    private Integer repeticiones;

    private Set<DetallesEjercicio> detalles;
}
