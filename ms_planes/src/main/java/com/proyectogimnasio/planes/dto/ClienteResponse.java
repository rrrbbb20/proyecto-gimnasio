package com.proyectogimnasio.planes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteResponse {
    private Long id;
    private String nombres;
    private String apellidos;
}
