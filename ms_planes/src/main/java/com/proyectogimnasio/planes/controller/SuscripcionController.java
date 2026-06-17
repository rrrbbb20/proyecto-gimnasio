package com.proyectogimnasio.planes.controller;

import com.proyectogimnasio.planes.dto.ApiResponse;
import com.proyectogimnasio.planes.dto.SuscripcionRequest;
import com.proyectogimnasio.planes.dto.SuscripcionResponse;
import com.proyectogimnasio.planes.service.PlanesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suscripciones")
@RequiredArgsConstructor
public class SuscripcionController {

    private final PlanesService planesService;


    @Operation(
            summary = "Crear y activar una suscripción para un cliente",
            description = "Agrega una suscripcion. Requiere rol ADMIN o USER."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Suscripcion Creada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<SuscripcionResponse>> activarSuscripcion(@Valid @RequestBody SuscripcionRequest request) {

        SuscripcionResponse response = planesService.crearSuscripcion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<SuscripcionResponse>builder()
                        .success(true)
                        .message("Suscripción activada con éxito")
                        .data(response)
                        .build()
        );
    }

    @Operation(
            summary = "Obtener el listado de todas las suscripciones",
            description = "Retorna todas las suscripciones registradas. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<SuscripcionResponse>>> obtenerTodas() {
        return ResponseEntity.ok(
                ApiResponse.<List<SuscripcionResponse>>builder()
                        .success(true)
                        .data(planesService.getAllSuscripciones())
                        .build()
        );
    }

    @Operation(
            summary = "Obtener la suscripción de un cliente específico por su ID",
            description = "Busca la suscripcion de un cliente usando su id. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Suscripcion obtenida"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Suscripcion no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<SuscripcionResponse>> obtenerPorCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(
                ApiResponse.<SuscripcionResponse>builder()
                        .success(true)
                        .data(planesService.getSuscripcionByCliente(idCliente))
                        .build()
        );
    }

    @Operation(
            summary = "Actualizar el estado de una suscripción (Ej: ACTIVA, VENCIDA, CANCELADA)",
            description = "Actualiza el estado de una suscripcion. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Estado de suscripcion Actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "suscripcion no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SuscripcionResponse>> cambiarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {

        return ResponseEntity.ok(
                ApiResponse.<SuscripcionResponse>builder()
                        .success(true)
                        .message("Estado actualizado correctamente")
                        .data(planesService.updateSuscripcion(id, nuevoEstado))
                        .build()
        );
    }

    @Operation(
            summary = "Eliminar un registro de suscripción por su ID",
            description = "Elimina una suscripcion usando su identificador. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Suscripcion eliminada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Suscripcion no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminarSuscripcion(@PathVariable Long id) {
        planesService.deleteSuscripcion(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Suscripción eliminada correctamente del sistema")
                        .build()
        );
    }
}