package com.example.ms_inventario.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InventarioRequest {
    @NotBlank
    private String nombre;
    @NotBlank
    private String descripcion;
    @NotBlank
    private double precio;
    @NotBlank
    private LocalDate fechaRegistro;
}
