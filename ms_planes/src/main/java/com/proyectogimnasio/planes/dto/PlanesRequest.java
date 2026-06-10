package com.proyectogimnasio.planes.dto;

import com.proyectogimnasio.planes.model.Pagos;
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
    @NotBlank(message = "Debe agregar una descripcion valida")
    private String descripcionPlan;
    @NotBlank(message = "Debe agregar los beneficios")
    private String beneficios;
    @Valid
    @NotNull(message = "El id debe ser valido")
    private Pagos Pago;
}
