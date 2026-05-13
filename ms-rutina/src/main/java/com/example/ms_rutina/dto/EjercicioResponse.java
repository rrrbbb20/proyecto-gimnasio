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
    private Long idEjercicio;
    private String nombreEjercicio;
    private String tipoEjercicio;
    private String zonaEjercitada;
    private Integer repeticiones;
}
