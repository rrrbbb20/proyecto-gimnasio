package com.proyectogimnasio.rutina.dto;

import com.proyectogimnasio.rutina.model.Ejercicio;
import com.proyectogimnasio.rutina.model.Rutina;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetallesEjercicioResponse {
    private Long id;
    private Ejercicio ejercicio;
    private Rutina rutina;


    private Integer numeroEjercicios;
    private String duracionRutina;
    private String tiempoDescanso;
}
