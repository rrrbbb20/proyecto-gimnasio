package com.proyectogimnasio.cliente.dto;

import com.proyectogimnasio.cliente.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteResponse extends Cliente {
    private Long id;
    private String nombres;
    private String apellidos;
    private String run;
    private String correo;
    private Long idPlan;
}
