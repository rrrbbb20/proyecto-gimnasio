package com.example.ms_clase.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
public class EntrenadorResponse {

    private Long id;
    private String nombreCompleto;
    private String run;
    private LocalDate fechaNacimiento;
}
