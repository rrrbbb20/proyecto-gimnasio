package com.example.ms_mantenimiento.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MantenimientoRequest {

    @NotBlank
    private String empresa;
    @NotBlank
    private String descripcionMantenimiento;
    @NotNull
    private LocalDate fechaMantenimiento;
    @NotNull
    @Min(0)
    private Double precio;
}
