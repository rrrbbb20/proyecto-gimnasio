package com.example.ms_clase.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="clase")
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreClase;
    private String descripcion;
    private String nivelDeClase;
    private LocalDate fechaRealizacion;
    private LocalTime horaRealizacion;
    private Integer cupos;
    private Boolean estado;
    private Long idEntrenador;

}
