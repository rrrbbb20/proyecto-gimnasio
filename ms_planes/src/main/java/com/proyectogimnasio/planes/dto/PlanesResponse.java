package com.proyectogimnasio.planes.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanesResponse {
    private Long id;
    private String nombrePlan;
    private BigDecimal precioPlan;
    private String descripcionPlan;
    private String beneficios;
    private Long idPago;

}
