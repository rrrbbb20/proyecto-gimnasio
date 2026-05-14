package com.example.ms_rutina.dto;

import com.example.ms_rutina.model.Ejercicio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RutinaResponse {
    private Long id;
    private String nombreRutina;
    private String duracion;
    private Integer tiempoDescanso;
    private List<EjercicioResponse> ejercicios;

}
