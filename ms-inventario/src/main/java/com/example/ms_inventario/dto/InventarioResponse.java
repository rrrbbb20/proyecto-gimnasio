package com.example.ms_inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventarioResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private double precio;
    private LocalDate fechaRegistro;
    private MantenimientoResponse infoMantenimiento;
}
