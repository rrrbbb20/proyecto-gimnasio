package com.proyectogimnasio.planes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuscripcionResponse {
    private Long id;
    private Long idCliente;
    private PlanesResponse plan;
    private PagosResponse pago;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
}