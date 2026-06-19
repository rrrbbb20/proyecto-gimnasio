package com.example.ms_mantenimiento.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MantenimientoResponse {
    private Long id;
    private String empresa;
    private String descripcionMantenimiento;
    private LocalDate fechaMantenimiento;
    private Double precio;
}
