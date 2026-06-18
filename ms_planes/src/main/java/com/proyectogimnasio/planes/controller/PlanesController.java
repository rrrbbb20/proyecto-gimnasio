package com.proyectogimnasio.planes.controller;

import com.proyectogimnasio.planes.dto.ApiResponse;
import com.proyectogimnasio.planes.dto.PlanesRequest;
import com.proyectogimnasio.planes.dto.PlanesResponse;
import com.proyectogimnasio.planes.service.PlanesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v3/planes")
@RequiredArgsConstructor
public class PlanesController {

    private final PlanesService planesService;

    @Operation(summary = "Agregar un plan", description = "Agrega un plan. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Plan Creado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EntityModel<PlanesResponse>>> addPlan(@Valid @RequestBody PlanesRequest p){
        PlanesResponse nuevoPlan = planesService.addPlan(p);
        EntityModel<PlanesResponse> recurso = crearRecursoPlan(nuevoPlan);

        return ResponseEntity.status(201).body(
                ApiResponse.<EntityModel<PlanesResponse>>builder()
                        .success(true)
                        .message("Plan creado")
                        .data(recurso)
                        .build()
        );
    }

    @Operation(summary = "Obtener plan por ID", description = "Busca un plan usando su id. Requiere rol USER o ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Plan obtenido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Plan no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<EntityModel<PlanesResponse>>> obtenerPlan(@PathVariable Long id){
        PlanesResponse plan = planesService.findByIdPlan(id);
        EntityModel<PlanesResponse> recurso = crearRecursoPlan(plan);

        return ResponseEntity.ok(
                ApiResponse.<EntityModel<PlanesResponse>>builder()
                        .success(true)
                        .message("Plan obtenido")
                        .data(recurso)
                        .build()
        );
    }

    @Operation(summary = "Listar planes", description = "Retorna todos los planes registrados. Requiere rol USER o ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<PlanesResponse>>>> getAllPlanes(){
        List<PlanesResponse> planes = planesService.getAllPlanes();

        List<EntityModel<PlanesResponse>> planesConLinks = planes.stream()
                .map(this::crearRecursoPlan)
                .toList();

        CollectionModel<EntityModel<PlanesResponse>> coleccionRecursos = CollectionModel.of(planesConLinks);
        coleccionRecursos.add(linkTo(methodOn(PlanesController.class).getAllPlanes()).withSelfRel());

        return ResponseEntity.status(200).body(
                ApiResponse.<CollectionModel<EntityModel<PlanesResponse>>>builder()
                        .success(true)
                        .data(coleccionRecursos)
                        .build()
        );
    }

    @Operation(summary = "Actualizar plan por ID ", description = "Busca un plan usando su id. Requiere rol USER o ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Plan Actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Plan no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EntityModel<PlanesResponse>>> updatePlan(@PathVariable Long id, @Valid @RequestBody PlanesRequest p) {
        PlanesResponse planActualizado = planesService.updatePlan(id, p);
        EntityModel<PlanesResponse> recurso = crearRecursoPlan(planActualizado);

        return ResponseEntity.ok(
                ApiResponse.<EntityModel<PlanesResponse>>builder()
                        .success(true)
                        .data(recurso)
                        .build()
        );
    }

    @Operation(summary = "Eliminar plan por ID", description = "Elimina un plan usando su identificador. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Plan eliminado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Plan no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePlan(@PathVariable Long id){
        planesService.deletePlan(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Plan Eliminado")
                        .build()
        );
    }

    private EntityModel<PlanesResponse> crearRecursoPlan(PlanesResponse plan) {
        EntityModel<PlanesResponse> recurso = EntityModel.of(plan);
        Long id = plan.getId(); // Verifica que PlanesResponse exponga el método getId()

        recurso.add(linkTo(methodOn(PlanesController.class).obtenerPlan(id)).withSelfRel());
        recurso.add(linkTo(methodOn(PlanesController.class).getAllPlanes()).withRel("all"));
        recurso.add(linkTo(methodOn(PlanesController.class).updatePlan(id, null)).withRel("update"));
        recurso.add(linkTo(methodOn(PlanesController.class).deletePlan(id)).withRel("delete"));

        return recurso;
    }
}