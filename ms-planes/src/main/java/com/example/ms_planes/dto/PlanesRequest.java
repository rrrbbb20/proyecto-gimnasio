package com.example.ms_planes.dto;

import com.example.ms_planes.model.Pagos;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlanesRequest {
    @NotBlank(message = "El nombre no puede quedar vacio")
    private String nombrePlan;
    @NotNull(message = "Debe ingresar el precio del plan")
    private Integer precioPlan;
    @Valid
    @NotNull(message = "El id debe ser valido")
    private Pagos Pagos;

}
