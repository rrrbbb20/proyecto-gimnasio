package com.example.ms_planes.model;

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
    @Column(name = "planes_id")
    private Long id;
    private String nombrePlan;
    private Integer precioPlan;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "pago_id", referencedColumnName = "pago_id")
    private Pagos pagos;



}
