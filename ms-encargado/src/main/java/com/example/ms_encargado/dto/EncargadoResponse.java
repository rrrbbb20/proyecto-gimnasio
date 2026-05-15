package com.example.ms_encargado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EncargadoResponse {
    private Long id;
    private String nombreCompleto;
    private String run;
    private String direccion;
    private LocalDate fechaNacimiento;
}
