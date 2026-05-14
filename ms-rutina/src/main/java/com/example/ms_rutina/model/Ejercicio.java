package com.example.ms_rutina.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ejercicios")
public class Ejercicio  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEjercicio;
    private String nombreEjercicio;
    private String tipoEjercicio;
    private String zonaEjercitada;
    private Integer repeticiones;
    @ManyToMany(mappedBy = "ejercicios")
    private Set<Rutina> rutinas = new HashSet<>();
}
