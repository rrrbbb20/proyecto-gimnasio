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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rutina")
    private Ejercicio ejercicio;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rutina")
    private Rutina rutina;


    private Integer numeroEjercicios;
    private String duracionRutina;
    private String tiempoDescanso;
}