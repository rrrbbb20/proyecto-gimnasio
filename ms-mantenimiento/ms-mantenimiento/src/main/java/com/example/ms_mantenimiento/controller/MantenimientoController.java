package com.example.ms_mantenimiento.controller;

import com.example.ms_mantenimiento.dto.ApiResponse;
import com.example.ms_mantenimiento.dto.MantenimientoRequest;
import com.example.ms_mantenimiento.dto.MantenimientoResponse;
import com.example.ms_mantenimiento.service.MantenimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mantenimiento")
@RequiredArgsConstructor
public class MantenimientoController {

    private final MantenimientoService service;

    @Operation(
            summary = "Agregar mantenimiento",
            description = "Agrega un mantenimiento a la lista. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Mantenimiento agregado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Mantenimiento no agregado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<ApiResponse<MantenimientoResponse>>> add(@Valid @RequestBody MantenimientoRequest m) {

        MantenimientoResponse mantenimiento = service.add(m);

        ApiResponse<MantenimientoResponse> respuestaBase =
                ApiResponse.<MantenimientoResponse>builder()
                        .success(true)
                        .message("Mantenimiento creado")
                        .data(mantenimiento)
                        .build();

        EntityModel<ApiResponse<MantenimientoResponse>> recurso =
                EntityModel.of(respuestaBase);

        recurso.add(linkTo(methodOn(MantenimientoController.class)
                .findById(mantenimiento.getId())).withSelfRel());

        recurso.add(linkTo(methodOn(MantenimientoController.class)
                .getAll()).withRel("all"));

        return ResponseEntity.status(201).body(recurso);
    }

    @Operation(
            summary = "Obtener mantenimiento por id",
            description = "Obtiene los datos del mantenimiento con el id ingresado. Requiere rol ADMIN o USER."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Mantenimiento encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<EntityModel<ApiResponse<MantenimientoResponse>>> findById(
            @Parameter(description = "id del mantenimiento a buscar", example = "1", required = true) @PathVariable Long id) {

        ApiResponse<MantenimientoResponse> base =
                ApiResponse.<MantenimientoResponse>builder()
                        .success(true)
                        .message("Mantenimiento encontrado")
                        .data(service.findById(id))
                        .build();

        EntityModel<ApiResponse<MantenimientoResponse>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(MantenimientoController.class)
                .findById(id)).withSelfRel());

        recurso.add(linkTo(methodOn(MantenimientoController.class)
                .getAll()).withRel("all"));

        recurso.add(linkTo(methodOn(MantenimientoController.class)
                .delete(id)).withRel("delete"));

        return ResponseEntity.ok(recurso);
    }

    @Operation(
            summary = "Obtener lista de mantenimientos",
            description = "Obtiene la lista completa de mantenimientos. Requiere rol ADMIN o USER."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Mantenimientos encontrados"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Lista no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<EntityModel<ApiResponse<List<MantenimientoResponse>>>> getAll() {

        ApiResponse<List<MantenimientoResponse>> base =
                ApiResponse.<List<MantenimientoResponse>>builder()
                        .success(true)
                        .message("Listado de mantenimientos")
                        .data(service.getAll())
                        .build();

        EntityModel<ApiResponse<List<MantenimientoResponse>>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(MantenimientoController.class)
                .getAll()).withSelfRel());

        return ResponseEntity.ok(recurso);
    }

    @Operation(
            summary = "Actualizar mantenimiento por id",
            description = "Actualiza los atributos del mantenimiento con el id ingresado. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Mantenimiento actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<ApiResponse<MantenimientoResponse>>> update(
            @Parameter(description = "id del mantenimiento que se desea modificar", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody MantenimientoRequest i) {

        MantenimientoResponse mantenimiento = service.update(id, i);

        ApiResponse<MantenimientoResponse> base =
                ApiResponse.<MantenimientoResponse>builder()
                        .success(true)
                        .message("Mantenimiento actualizado")
                        .data(mantenimiento)
                        .build();

        EntityModel<ApiResponse<MantenimientoResponse>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(MantenimientoController.class)
                .findById(id)).withSelfRel());

        recurso.add(linkTo(methodOn(MantenimientoController.class)
                .getAll()).withRel("all"));

        recurso.add(linkTo(methodOn(MantenimientoController.class)
                .delete(id)).withRel("delete"));

        return ResponseEntity.ok(recurso);
    }

    @Operation(
            summary = "Eliminar mantenimiento por id",
            description = "Elimina el mantenimiento con el id ingresado. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Mantenimiento eliminado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Mantenimiento eliminado")
                        .build()
        );
    }
}
