package com.proyectogimnasio.planes.dto;

import com.proyectogimnasio.cliente.model.Cliente;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PagosRequest {
    @NotBlank(message = "Debe poner un tipo de pago valido")
    private String tipoPago;
    @NotNull(message="El campo del numero no puede ser vacio")
    private Double numTarjeta;
    @NotNull(message = "Debe poner una fecha valida")
    private String fechaVencimiento;
    @NotNull(message = "El cvc es obligatorio")
    @Length(max = 3)
    private Integer cvc;
    @NotBlank(message = "Debe agregar una direccion valida")
    private String direccionFacturacion;
    @NotBlank(message = "Debe agregar un codigo postal")
    private String codigoPostal;
    @Valid
    @NotBlank(message = "Debe agregar un cliente existente")
    private Cliente cliente;
}