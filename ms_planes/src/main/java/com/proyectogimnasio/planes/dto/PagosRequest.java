package com.proyectogimnasio.planes.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PagosRequest {
    @NotBlank(message = "Debe poner un tipo de pago valido")
    private String tipoPago;
    @NotNull(message="El campo del numero no puede ser vacio")
    private String numTarjeta;
    @NotNull(message = "Debe poner una fecha valida")
    private String fechaVencimiento;
    @NotNull(message = "El cvc es obligatorio")
    @Min(value = 100, message = "El CVC debe tener al menos 3 dígitos")
    @Max(value = 999, message = "El CVC no puede tener más de 3 dígitos")
    private Integer cvc;
    @NotBlank(message = "Debe agregar una direccion valida")
    private String direccionFacturacion;
    @NotBlank(message = "Debe agregar un codigo postal")
    private String codigoPostal;
    @Valid
    @NotNull(message = "Debe agregar un cliente existente")
    private Long idCliente;
}