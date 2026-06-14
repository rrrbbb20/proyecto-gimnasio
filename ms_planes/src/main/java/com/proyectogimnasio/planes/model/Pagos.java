package com.proyectogimnasio.planes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pagos")
public class Pagos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pago_id")
    private Long id;
    private String tipoPago;
    private String numTarjeta;
    private String fechaVencimiento;
    private Integer cvc;
    private String direccionFacturacion;
    private String codigoPostal;
    @JoinColumn(name = "nombre_cliente_id")
    private Long idCliente;
}
