package com.proyectogimnasio.planes.dto;




import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagosResponse {
    private Long id;
    private String tipoPago;
    private String numTarjeta;
    private String fechaVencimiento;
    private Integer cvc;
    private String direccionFacturacion;
    private String codigoPostal;
    private Long idCliente;
}
