package com.example.ms_sede.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SedeResponse {
    private Long id;
    private String direccion;
    private Integer horaApertura;
    private Integer horaCierre;
}
