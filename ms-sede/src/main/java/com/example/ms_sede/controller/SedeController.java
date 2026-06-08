package com.example.ms_sede.controller;


import com.example.ms_sede.dto.ApiResponse;
import com.example.ms_sede.dto.SedeRequest;
import com.example.ms_sede.dto.SedeResponse;
import com.example.ms_sede.model.Sede;
import com.example.ms_sede.service.SedeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Sedes", description = "Operaciones relacionadas con sedes")
@RestController
@RequestMapping("api/v1/sede")
@RequiredArgsConstructor
public class SedeController {

    private final SedeService sedeService;


    @Operation(
            summary = "Agregar sede",
            description = "Agregar sede ingresando los atributos. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sede Agregada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Sede no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SedeResponse>> agregarSede(@Valid @RequestBody SedeRequest dto,
                                                                 @RequestHeader("Authorization")String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<SedeResponse>builder().success(true)
                        .message("Sede agregada")
                        .data(sedeService.createSede(dto,token)).build()
        );
    }

    @Operation(
            summary = "Listar sedes",
            description = "Retorna todas las sedes registradas. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<SedeResponse>>> listar(@RequestHeader("Authorization") String token) {

        return ResponseEntity.status(200).body(
                ApiResponse.<List<SedeResponse>>builder().success(true)
                        .message("Las sedes se muestra a continuación")
                        .data(sedeService.listar(token)).build()
        );
    }


    @Operation(
            summary = "Obtener sede por id",
            description = "Retorna la sede del id ingresado respectivamente. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Sede no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<SedeResponse>> obtener(@PathVariable Long id,
                                                     @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(200).body(
                ApiResponse.<SedeResponse>builder().success(true)
                        .message("Sede encontrada")
                        .data(sedeService.obtenerSede(id,token)).build()
        );
    }


    @Operation(
            summary = "Actualizar sede por id",
            description = "Permite actualizar los atributos de la sede de id ingresado. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sede actualizada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Sede no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SedeResponse>> actualizarSede(@PathVariable Long id,
                                                         @Valid @RequestBody SedeRequest dto,
                                                                    @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(

                ApiResponse.<SedeResponse>builder().success(true)
                        .message("Sede actualizada")
                        .data(sedeService.actualizarSede(id, dto, token)).build()
        );
    }

    @Operation(
            summary = "Elimina una sede por id",
            description = "Elimina la sede del id ingresado respectivamente. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sede eliminada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Sede no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminarSede(@PathVariable Long id) {

        sedeService.eliminarSede(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Sede eliminada con exito")
                        .build()
        );
    }
}
