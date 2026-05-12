package com.example.ms_planes.dto;


import com.example.ms_planes.model.Pagos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanesResponse {
    private Long id;
    private String nombrePlan;
    private Integer precioPlan;
    private Pagos idPago;

}
