package com.proyectogimnasio.planes.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SuscripcionRequest {
    @NotNull(message = "El id del cliente es obligatorio")
    private Long idCliente;

    @NotNull(message = "El id del plan es obligatorio")
    private Long idPlan;

    @NotNull(message = "Debe enviar los datos de pago")
    private PagosRequest pago;
}