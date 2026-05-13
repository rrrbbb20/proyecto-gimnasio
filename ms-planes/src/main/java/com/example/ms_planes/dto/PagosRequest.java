package com.example.ms_planes.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

@Data
public class PagosRequest {
    @NotBlank(message = "Debe poner un tipo de pago valido")
    private String tipoPago;
    @NotNull(message="El campo del numero no puede ser vacio")
    private Integer numeroTarjeta;
    @NotNull(message = "Debe poner una fecha valida")
    private String fechaCaducidad;
    private Integer cvc;
}
