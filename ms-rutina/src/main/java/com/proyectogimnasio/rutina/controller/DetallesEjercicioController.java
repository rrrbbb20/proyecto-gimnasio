package com.proyectogimnasio.rutina.controller;

import com.proyectogimnasio.rutina.dto.*;
import com.proyectogimnasio.rutina.service.RutinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/detallesejercicios")
@RequiredArgsConstructor
public class DetallesEjercicioController {
    private final RutinaService service;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")

    @Operation(
            summary = "Agregar un detalles de las rutinas",
            description = "Agrega un detalles de las rutinas. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Detalles Creados"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    public ResponseEntity<ApiResponse<DetallesEjercicioResponse>> addDetalles(@Valid @RequestBody DetallesEjercicioRequest d, String token ){
        return ResponseEntity.status(201).body(
                ApiResponse.<DetallesEjercicioResponse>builder().success(true)
                        .message("Detalles creados")
                        .data(service.addDetalles(d, token)).build()

        );
    }
    @Operation(
            summary = "Obtener detalles de las rutinas por ID",
            description = "Busca un detalles de las rutinas usando su id. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Detalles obtenidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Detalles no encontrados"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<EntityModel<DetallesEjercicioResponse>>> findDetalles(@PathVariable("id")Long id, String token){
        DetallesEjercicioResponse detalles = service.findDetalles(id, token);
        EntityModel<DetallesEjercicioResponse> recurso = EntityModel.of(detalles);

        recurso.add(linkTo(methodOn(DetallesEjercicioController.class).findDetalles(id, token)).withSelfRel());
        recurso.add(linkTo(methodOn(DetallesEjercicioController.class).getDetalles(token)).withRel("all"));
        recurso.add(linkTo(methodOn(DetallesEjercicioController.class).updateDetalles(id, null, token)).withRel("update"));
        recurso.add(linkTo(methodOn(DetallesEjercicioController.class).deleteDetalles(id)).withRel("delete"));

        return ResponseEntity.ok(
                ApiResponse.<EntityModel<DetallesEjercicioResponse>>builder()
                        .success(true)
                        .message("Detalles obtenidos")
                        .data(recurso)
                        .build()
        );

    }
    @Operation(
            summary = "Listar detalles de rutinas",
            description = "Retorna todos los detalles de rutinas registrados. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<DetallesEjercicioResponse>>> getDetalles(String token){
        return ResponseEntity.status(200).body(
                ApiResponse.<List<DetallesEjercicioResponse>>builder().success(true)
                        .data(service.getDetalles(token)).build()
        );

    }
    @Operation(
            summary = "Actualizar detalles de las rutinas por ID ",
            description = "Busca un detalles de las rutinas usando su id. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Detalles Actualizados"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Detalles no encontrados"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DetallesEjercicioResponse>> updateDetalles(@PathVariable("id") Long id, @Valid @RequestBody DetallesEjercicioRequest d, String token) {
        return ResponseEntity.ok(

                ApiResponse.<DetallesEjercicioResponse>builder().success(true)
                        .data(service.updateDetalles(id,d, token)).build()

        );

    }@Operation(
            summary = "Eliminar detalles por ID",
            description = "Elimina detalles de las rutinas usando su identificador. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Detalles eliminado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Detalles no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteDetalles(@PathVariable("id") Long id){
        service.deleteDetalles(id);
        return ResponseEntity.ok(

                ApiResponse.<Void>builder().success(true).message("Detalles Eliminados").build()
        );
    }

}
