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
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@Tag(name = "Sedes", description = "Operaciones relacionadas con sedes")
@RestController
@RequestMapping("api/v1/sede")
@RequiredArgsConstructor
public class SedeController {

    private final SedeService service;


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
    public ResponseEntity<EntityModel<ApiResponse<SedeResponse>>> agregarSede(
            @Valid @RequestBody SedeRequest s,
            @Parameter(hidden = true)@RequestHeader("Authorization") String token) {

        SedeResponse clase = service.add(s, token);

        ApiResponse<SedeResponse> base =
                ApiResponse.<SedeResponse>builder()
                        .success(true)
                        .message("Sede creada")
                        .data(clase)
                        .build();

        EntityModel<ApiResponse<SedeResponse>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(SedeController.class)
                .obtenerSede(clase.getId(),null)).withSelfRel()); //token por null

        recurso.add(linkTo(methodOn(SedeController.class)
                .listar(null)).withRel("all")); //token por null

        return ResponseEntity.status(201).body(recurso);
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
    public ResponseEntity<EntityModel<ApiResponse<List<SedeResponse>>>> listar(
            @Parameter(hidden = true)@RequestHeader("Authorization")
            String token){

        ApiResponse<List<SedeResponse>> base =
                ApiResponse.<List<SedeResponse>>builder()
                        .success(true)
                        .message("Listado de sedes")
                        .data(service.listar(token))
                        .build();

        EntityModel<ApiResponse<List<SedeResponse>>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(SedeController.class)
                .listar(null)).withSelfRel()); //token por null

        recurso.add(linkTo(methodOn(SedeController.class)
                .agregarSede(null, null)).withRel("create"));//token por null

        return ResponseEntity.ok(recurso);
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
    public ResponseEntity<EntityModel<ApiResponse<SedeResponse>>> obtenerSede(
            @Parameter(description = "id de la sede a buscar", example = "1", required = true)@PathVariable Long id,
            @Parameter(hidden = true)@RequestHeader("Authorization")String token){
        ApiResponse<SedeResponse> base =
                ApiResponse.<SedeResponse>builder()
                        .success(true)
                        .message("Sede encontrada")
                        .data(service.obtenerSede(id, token))
                        .build();

        EntityModel<ApiResponse<SedeResponse>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(SedeController.class)
                .obtenerSede(id,null)).withSelfRel());

        recurso.add(linkTo(methodOn(SedeController.class)
                .listar(null)).withRel("all"));

        recurso.add(linkTo(methodOn(SedeController.class)
                .actualizarSede(id, null, null)).withRel("update")); //cambie token por null

        recurso.add(linkTo(methodOn(SedeController.class)
                .eliminarSede(id)).withRel("delete"));

        return ResponseEntity.ok(recurso);
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
                        .data(service.actualizarSede(id, dto, token)).build()
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

        service.eliminarSede(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Sede eliminada con exito")
                        .build()
        );
    }
}
