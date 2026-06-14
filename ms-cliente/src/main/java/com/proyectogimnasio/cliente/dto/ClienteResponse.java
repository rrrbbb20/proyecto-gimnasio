package com.proyectogimnasio.cliente.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteResponse {
    private Long id;
    private String nombres;
    private String apellidos;
    private String run;
    private String correo;
    private LocalDate fechaNac;
    private Long idPlan;

    private PlanesResponse detallesPlan;
}
