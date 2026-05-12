package com.example.ms_cliente.dto;

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
    private String nombreCompletoCliente;
    private String run;
    private LocalDate fechaNacCliente;
    private String tipoPlan;

}
