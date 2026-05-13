package com.example.ms_planes.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagosResponse {
    private Long idPago;
    private String tipoPago;
    private Integer numeroTarjeta;
    private String fechaCaducidad;
    @Column(nullable = true)
    private Integer cvc;

}
