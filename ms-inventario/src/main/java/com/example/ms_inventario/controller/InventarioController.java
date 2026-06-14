package com.example.ms_inventario.controller;


import com.example.ms_inventario.dto.ApiResponse;
import com.example.ms_inventario.dto.InventarioRequest;
import com.example.ms_inventario.dto.InventarioResponse;
import com.example.ms_inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService service;

    @Operation(
            summary = "Agregar equipamiento",
            description = "Agrega equipamiento a la lista. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Equipamiento agregaedo"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Equipamiento no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<ApiResponse<InventarioResponse>>> add(
            @Valid @RequestBody InventarioRequest i,
            @Parameter(hidden = true)@RequestHeader("Authorization") String token) {

        InventarioResponse inventario = service.add(i,token);

        ApiResponse<InventarioResponse> respuestaBase =
                ApiResponse.<InventarioResponse>builder()
                        .success(true)
                        .message("Equipamiento agregado")
                        .data(inventario)
                        .build();

        EntityModel<ApiResponse<InventarioResponse>> recurso =
                EntityModel.of(respuestaBase);

        recurso.add(linkTo(methodOn(InventarioController.class)
                .findById(inventario.getId(),null)).withSelfRel());

        recurso.add(linkTo(methodOn(InventarioController.class)
                .getAll(null)).withRel("all"));

        return ResponseEntity.status(201).body(recurso);

    }
    @Operation(
            summary = "Obtener lista de equipamientos por id",
            description = "Obtiene la lista del equipamiento con el id ingresado. Requiere rol ADMIN o USER."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Equipamiento encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Equipamiento no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<EntityModel<ApiResponse<InventarioResponse>>> findById(
            @Parameter(description = "id del equipamiento a buscar", example = "1", required = true)@PathVariable Long id,
            @Parameter(hidden = true)@RequestHeader("Authorization")String token){
        ApiResponse<InventarioResponse> base =
                ApiResponse.<InventarioResponse>builder()
                        .success(true)
                        .message("Equipamiento encontrado")
                        .data(service.findById(id, token))
                        .build();

        EntityModel<ApiResponse<InventarioResponse>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(InventarioController.class)
                .findById(id, null)).withSelfRel());

        recurso.add(linkTo(methodOn(InventarioController.class)
                .getAll(null)).withRel("all"));

        recurso.add(linkTo(methodOn(InventarioController.class)
                .update(id, null, null)).withRel("update")); //cambie token por null

        recurso.add(linkTo(methodOn(InventarioController.class)
                .delete(id)).withRel("delete"));

        return ResponseEntity.ok(recurso);
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
    public ResponseEntity<EntityModel<ApiResponse<List<InventarioResponse>>>> getAll(
            @Parameter(hidden = true)@RequestHeader("Authorization")
            String token){

        ApiResponse<List<InventarioResponse>> base =
                ApiResponse.<List<InventarioResponse>>builder()
                        .success(true)
                        .message("Listado de clases")
                        .data(service.getAll(token))
                        .build();

        EntityModel<ApiResponse<List<InventarioResponse>>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(InventarioController.class)
                .getAll(null)).withSelfRel()); //token por null

        recurso.add(linkTo(methodOn(InventarioController.class)
                .add(null, null)).withRel("create"));//token por null

        return ResponseEntity.ok(recurso);
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
    public ResponseEntity<EntityModel<ApiResponse<InventarioResponse>>>update(
            @Parameter(description = "id del equipamiento que se desea modificar", example = "1", required = true)@PathVariable Long id,
            @Valid @RequestBody InventarioRequest c,
            @Parameter(hidden = true)@RequestHeader("Authorization")String token){

        InventarioResponse clase = service.update(id, c, token);

        ApiResponse<InventarioResponse> base =
                ApiResponse.<InventarioResponse>builder()
                        .success(true)
                        .message("Inventario actualizado")
                        .data(clase)
                        .build();

        EntityModel<ApiResponse<InventarioResponse>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(InventarioController.class)
                .findById(id, null)).withSelfRel()); //token por null

        recurso.add(linkTo(methodOn(InventarioController.class)
                .getAll(null)).withRel("all")); //token por null

        recurso.add(linkTo(methodOn(InventarioController.class)
                .delete(id)).withRel("delete"));

        return ResponseEntity.ok(recurso);
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
