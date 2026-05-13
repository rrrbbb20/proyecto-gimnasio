package com.example.ms_inventario.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InventarioRequest {
    @NotBlank
    private String nombre;
    @NotBlank
    private String descripcion;
    @NotNull
    private double precio;
    @NotNull
    private LocalDate fechaRegistro;
}
