package com.proyectogimnasio.planes.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "suscripciones")
public class Suscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_cliente")
    private Long idCliente;

    @ManyToOne
    @JoinColumn(name = "id_plan")
    private Planes plan;

    @OneToOne
    @JoinColumn(name = "id_pago")
    private Pagos pago;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
}