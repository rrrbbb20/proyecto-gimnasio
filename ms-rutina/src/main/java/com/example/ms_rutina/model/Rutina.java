package com.example.ms_rutina.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @OneToMany(mappedBy = "rutina")
    private Set<Ejercicio> ejercicios;
}
