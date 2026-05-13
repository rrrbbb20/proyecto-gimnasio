package com.example.ms_rutina.dto;

import com.example.ms_rutina.model.Ejercicio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EjercicioResponse {
    private Long id;
    private String nombreRutina;
    private String duracion;
    private Integer tiempoDescanso;
    private Ejercicio ejercicio;
}
