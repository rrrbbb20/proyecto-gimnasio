package com.example.ms_inscripcion_clase.controller;


import com.example.ms_inscripcion_clase.dto.ApiResponse;
import com.example.ms_inscripcion_clase.dto.ClaseResponse;
import com.example.ms_inscripcion_clase.dto.InscripcionClaseRequest;
import com.example.ms_inscripcion_clase.dto.InscripcionClaseResponse;
import com.example.ms_inscripcion_clase.service.InscripcionClaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(
        name = "inscripcion-clases",
        description = "Endpoints para gestion de inscripciones a clases"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inscripcion-clases")
public class InscripcionClaseController {

    private final InscripcionClaseService service;

    @Operation(
            summary = "registrar inscripcion a clase ",
            description = "Recibe los datos necesarios para crear la inscripcion a una clase, se valida y realiza la creación."
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InscripcionClaseResponse>> add
            (@Valid @RequestBody InscripcionClaseRequest ic ,@Parameter(hidden = true)@RequestHeader("Authorization") String token){

        return ResponseEntity.status(201).body
                (ApiResponse.<InscripcionClaseResponse>builder()
                .success(true)
                .message("Creado correctamente")
                .data(service.add(ic,token))
                .build());
    }
    @Operation(
            summary = "encontrar una inscripcion proporcionando su id ",
            description = "permite retornar la inscripcion buscada si existe "
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<InscripcionClaseResponse>> findById(
            @Parameter(description = "id de la inscripcion a buscar", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(hidden = true)@RequestHeader("Authorization")String token){
        return ResponseEntity.status(200).body
                (ApiResponse.<InscripcionClaseResponse>builder()
                        .success(true)
                        .message("Inscripcion Encontrada")
                        .data(service.findById(id,token))
                        .build());

    }
    /*@PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InscripcionClaseResponse>> update(@PathVariable Long id,
                                                                        @Valid @RequestBody InscripcionClaseRequest ir,
                                                                        @RequestHeader("Authorization")String token){
        return ResponseEntity.status(200).body
                (ApiResponse.<InscripcionClaseResponse>builder()
                        .success(true)
                        .message("Inscripcion actualizada")
                        .data(service.update(id, ir,token))
                        .build());



    }*/
    @Operation(
            summary = "obtener a todas las inscripciones a clases registradas ",
            description = "permite retornar una lista de todos las inscripciones a clases que se encuentran registradas "
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<List<InscripcionClaseResponse>>> getAll(@Parameter(hidden = true)@RequestHeader("Authorization") String token){

        return ResponseEntity.ok(ApiResponse.<List<InscripcionClaseResponse>>builder()
                .success(true)
                .data(service.getAll(token))
                .build()
        );
    }
    @Operation(
            summary = "eliminar una inscripcion a clase registrada",
            description = "permite eliminar una inscripcion a clase mediante su id "
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "id de la inscripcion a clase que se desea eliminar", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(hidden = true) @RequestHeader("Authorization") String token){
        service.delete(id,token);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Inscripcion a Clase eliminada")
                .build()
        );
    }
    @Operation(
            summary = "retornar una lista de clases almacenadas proporcionando su nombre",
            description = "si la clase buscada existe retornara una lista de las clases encontradas por ese nombre "
    )
    @GetMapping("/buscar-clase-por-nombre/{nombre}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<List<ClaseResponse>>> findClasePorNombre(
            @Parameter(description = "nombre de la clase a buscar", example = "tenis", required = true)
            @PathVariable String nombre,
            @Parameter(hidden = true)@RequestHeader("Authorization")String token){
        return ResponseEntity.status(200).body
                (ApiResponse.<List<ClaseResponse>>builder()
                        .success(true)
                        .message("Clase Encontrada")
                        .data(service.buscarClasePorNombre(nombre,token))
                        .build());

    }
}
