package com.example.ms_rutina.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="rutina")
public class Rutina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rutina")
    private Long id;
    private String nombreRutina;
    private String duracion;
    private Integer tiempoDescanso;
    @ManyToMany
    @JoinTable(name="rutina_ejercicio", joinColumns = @JoinColumn(name = "id_rutina"),
            inverseJoinColumns = @JoinColumn(name = "id_ejercicio"))
    private Set<Ejercicio> ejercicios = new HashSet<>();
}
