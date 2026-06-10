package com.proyectogimnasio.planes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "planes")
public class Planes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombrePlan;
    private String precioPlan;
    private String descripcionPlan;
    private String beneficios;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "pago_id", referencedColumnName = "pago_id")
    private Pagos tipoPago;
}
