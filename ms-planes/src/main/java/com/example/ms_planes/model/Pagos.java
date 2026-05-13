package com.example.ms_planes.model;

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
    private Long idPago;
    @Column(name = "tipo_pago")
    private String tipoPago;
    @Column(name = "numero_tarjeta")
    private Integer numeroTarjeta;
    @Column(name = "fecha_caducidad")
    private String fechaCaducidad;
    @Column(nullable = true,name = "cvc")
    private Integer cvc;


    public Pagos(Long idPago) {
        super();
        this.idPago = idPago;
    }
}
