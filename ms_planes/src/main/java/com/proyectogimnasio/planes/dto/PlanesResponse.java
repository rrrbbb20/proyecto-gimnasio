package com.proyectogimnasio.planes.dto;

import com.proyectogimnasio.planes.model.Pagos;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanesResponse {
    private Long id;
    private String nombrePlan;
    private Integer precioPlan;
    private String descripcionPlan;
    private String beneficios;
    private Pagos tipoPago;

}
