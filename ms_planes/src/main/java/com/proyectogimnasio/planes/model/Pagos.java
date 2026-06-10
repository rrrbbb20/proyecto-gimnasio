package com.proyectogimnasio.planes.model;

import com.proyectogimnasio.cliente.model.Cliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Component;

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
    private Double numTarjeta;
    private String fechaVencimiento;
    private Integer cvc;
    private String direccionFacturacion;
    private String codigoPostal;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "nombre_cliente_id")
    private Cliente nombreCliente;
}
