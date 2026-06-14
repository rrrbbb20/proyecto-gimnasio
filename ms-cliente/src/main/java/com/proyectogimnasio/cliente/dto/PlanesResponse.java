package com.proyectogimnasio.cliente.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanesResponse {
    private Long id;
    private String nombrePlan;
    private BigDecimal precioPlan;
    private String descripcionPlan;
    private String beneficios;
    private Object idPago;
}
