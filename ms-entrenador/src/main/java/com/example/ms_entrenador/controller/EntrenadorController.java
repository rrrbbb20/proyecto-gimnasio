package com.example.ms_entrenador.controller;


import com.example.ms_entrenador.dto.ApiResponse;
import com.example.ms_entrenador.dto.EntrenadorRequest;
import com.example.ms_entrenador.dto.EntrenadorResponse;
import com.example.ms_entrenador.service.EntrenadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(
        name = "Entrenadores",
        description = "Endpoints para gestion de Entrenadores"
)
@RestController
@RequestMapping("/api/v1/entrenadores")
@RequiredArgsConstructor
public class EntrenadorController {

    private final EntrenadorService service;

    @Operation(
            summary = "anadir entrenador",
            description = "Recibe los datos del entrenador, valida el cumplimiento de las reglas de negocio y realiza la creación."
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EntrenadorResponse>> add(@Valid @RequestBody EntrenadorRequest e){

        return ResponseEntity.status(201).body(
            ApiResponse.<EntrenadorResponse>builder().success(true)
                    .message("Entrenador creado")
                    .data(service.add(e)).build()

        );

    }
    @Operation(
            summary = "encontrar entrenador proporcionando su id ",
            description = "permite retornar al entrenador buscado si existe "
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EntrenadorResponse>> findById(@PathVariable Long id){
        return ResponseEntity.status(200).body(
                ApiResponse.<EntrenadorResponse>builder().success(true).message("Encontrado")
                        .data(service.findById(id)).build()
        );
    }
    @Operation(
            summary = "obtener a todos los entrenadores registrados ",
            description = "permite retornar una lista de todos los entrenadores que se encuentran registrados "
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<EntrenadorResponse>>> getAll(){

        return ResponseEntity.status(200).body(
                ApiResponse.<List<EntrenadorResponse>>builder().success(true)
                        .data(service.getAll()).build()
        );

    }
    @Operation(
            summary = "actualizar a un entrenador",
            description = "permite actualizar datos de un entrenador mediante su busqueda por id "
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EntrenadorResponse>> update(@PathVariable Long id, @Valid @RequestBody EntrenadorRequest e) {

        return ResponseEntity.ok(

                ApiResponse.<EntrenadorResponse>builder().success(true)
                        .data(service.update(id,e)).build()

        );

    }
    @Operation(
            summary = "eliminar a un entrenador registrado",
            description = "permite eliminar a un entrenado mediante su id "
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(

                ApiResponse.<Void>builder().success(true).message("Entrenador Eliminado").build()
        );
    }


}
