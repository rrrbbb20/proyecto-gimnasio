package com.proyectogimnasio.planes.controller;

import com.proyectogimnasio.planes.dto.ApiResponse;
import com.proyectogimnasio.planes.dto.SuscripcionRequest;
import com.proyectogimnasio.planes.dto.SuscripcionResponse;
import com.proyectogimnasio.planes.service.PlanesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v3/suscripciones")
@RequiredArgsConstructor
public class SuscripcionController {

    private final PlanesService planesService;

    @Operation(summary = "Crear y activar una suscripción para un cliente", description = "Agrega una suscripcion. Requiere rol ADMIN o USER.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Suscripcion Creada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<EntityModel<SuscripcionResponse>>> activarSuscripcion(@Valid @RequestBody SuscripcionRequest request) {
        SuscripcionResponse response = planesService.crearSuscripcion(request);
        EntityModel<SuscripcionResponse> recurso = crearRecursoSuscripcion(response);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<EntityModel<SuscripcionResponse>>builder()
                        .success(true)
                        .message("Suscripción activada con éxito")
                        .data(recurso)
                        .build()
        );
    }

    @Operation(summary = "Obtener el listado de todas las suscripciones", description = "Retorna todas las suscripciones registradas. Requiere rol USER o ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<SuscripcionResponse>>>> obtenerTodas() {
        List<SuscripcionResponse> suscripciones = planesService.getAllSuscripciones();

        List<EntityModel<SuscripcionResponse>> suscripcionesConLinks = suscripciones.stream()
                .map(this::crearRecursoSuscripcion)
                .toList();

        CollectionModel<EntityModel<SuscripcionResponse>> coleccionRecursos = CollectionModel.of(suscripcionesConLinks);
        coleccionRecursos.add(linkTo(methodOn(SuscripcionController.class).obtenerTodas()).withSelfRel());

        return ResponseEntity.ok(
                ApiResponse.<CollectionModel<EntityModel<SuscripcionResponse>>>builder()
                        .success(true)
                        .data(coleccionRecursos)
                        .build()
        );
    }

    @Operation(summary = "Obtener la suscripción de un cliente específico por su ID", description = "Busca la suscripcion de un cliente usando su id. Requiere rol USER o ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Suscripcion obtenida"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Suscripcion no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<EntityModel<SuscripcionResponse>>> obtenerPorCliente(@PathVariable Long idCliente) {
        SuscripcionResponse response = planesService.getSuscripcionByCliente(idCliente);
        EntityModel<SuscripcionResponse> recurso = crearRecursoSuscripcion(response);

        return ResponseEntity.ok(
                ApiResponse.<EntityModel<SuscripcionResponse>>builder()
                        .success(true)
                        .data(recurso)
                        .build()
        );
    }

    @Operation(summary = "Actualizar el estado de una suscripción (Ej: ACTIVA, VENCIDA, CANCELADA)", description = "Actualiza el estado de una suscripcion. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Estado de suscripcion Actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "suscripcion no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EntityModel<SuscripcionResponse>>> cambiarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        SuscripcionResponse actualizada = planesService.updateSuscripcion(id, nuevoEstado);
        EntityModel<SuscripcionResponse> recurso = crearRecursoSuscripcion(actualizada);

        return ResponseEntity.ok(
                ApiResponse.<EntityModel<SuscripcionResponse>>builder()
                        .success(true)
                        .message("Estado actualizado correctamente")
                        .data(recurso)
                        .build()
        );
    }

    @Operation(summary = "Eliminar un registro de suscripción por su ID", description = "Elimina una suscripcion usando su identificador. Requiere rol ADMIN.")
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


    private EntityModel<SuscripcionResponse> crearRecursoSuscripcion(SuscripcionResponse suscripcion) {
        EntityModel<SuscripcionResponse> recurso = EntityModel.of(suscripcion);
        Long id = suscripcion.getId(); // Asegúrate de que SuscripcionResponse tenga implementado getId()

        // Enlace al recurso individual (Self)
        recurso.add(linkTo(methodOn(SuscripcionController.class).cambiarEstado(id, null)).withSelfRel());

        // Enlaces de relación semántica
        recurso.add(linkTo(methodOn(SuscripcionController.class).obtenerTodas()).withRel("all"));
        recurso.add(linkTo(methodOn(SuscripcionController.class).cambiarEstado(id, null)).withRel("update-status"));
        recurso.add(linkTo(methodOn(SuscripcionController.class).eliminarSuscripcion(id)).withRel("delete"));

        // Link contextual por si se requiere buscar directamente la suscripción vinculada al cliente específico
        if (suscripcion.getIdCliente() != null) { // Asumiendo que expone la propiedad del ID del cliente
            recurso.add(linkTo(methodOn(SuscripcionController.class).obtenerPorCliente(suscripcion.getIdCliente())).withRel("by-cliente"));
        }

        return recurso;
    }
}