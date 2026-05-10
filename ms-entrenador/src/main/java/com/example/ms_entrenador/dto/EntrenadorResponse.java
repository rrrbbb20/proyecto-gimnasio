package com.example.ms_entrenador.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntrenadorResponse {

    private Long id;
    private String nombreCompleto;
    private String run;
    private LocalDate fechaNacimiento;

}
