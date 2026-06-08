package com.example.ms_inventario.controller;


import com.example.ms_inventario.dto.ApiResponse;
import com.example.ms_inventario.dto.InventarioRequest;
import com.example.ms_inventario.dto.InventarioResponse;
import com.example.ms_inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InventarioResponse>> add(
            @Valid @RequestBody InventarioRequest i,
            @RequestHeader("Authorization")String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<InventarioResponse>builder().success(true)
                        .message("Entrenador creado")
                        .data(service.add(i,token)).build()

        );
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<InventarioResponse>> findById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(200).body(
                ApiResponse.<InventarioResponse>builder().success(true)
                        .message("Inventario encontrado")
                        .data(service.findById(id,token)).build()
        );
    }

    @Operation(
            summary = "Obtener lista de equipamientos",
            description = "Obtiene la lista completa de los equipamientos. Requiere rol ADMIN o USER."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Equipamientos encontrados"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Lista no encontada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<InventarioResponse>>> getAll(
    @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(200).body(
                ApiResponse.<List<InventarioResponse>>builder().success(true)
                        .message("El inventario se muestra a continuación")
                        .data(service.getAll(token)).build()
        );
    }
    @Operation(
            summary = "Actualizar atributos del equipamiento de la id ingresada",
            description = "Actualiza atributos del inventario de id ingresado. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Equipamiento Actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Equipamiento no enfcontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InventarioResponse>> update(@PathVariable Long id, @Valid @RequestBody InventarioRequest i,
                                                                  @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(

                ApiResponse.<InventarioResponse>builder().success(true)
                        .message("Inventario actualizado")
                        .data(service.update(id, i, token)).build()

        );
    }
    @Operation(
            summary = "Elimina un equipamiento por id",
            description = "Elimina un equipamiento del id ingresado respectivamente. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Equipamiento eliminado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Equipamiento no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(

                ApiResponse.<Void>builder().success(true).message("Objeto del Inventario de id = "+ id +" Eliminado").build()
        );
    }
}
