package com.proyectogimnasio.rutina.model;

import jakarta.persistence.*;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "detalles_ejercicio")
public class DetallesEjercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ejercicio_id")
    private Ejercicio ejercicio;
    @ManyToOne
    @JoinColumn(name = "rutina_id")
    private Rutina rutina;


    private Integer numeroEjercicios;
    private String duracionRutina;
    private String tiempoDescanso;
}