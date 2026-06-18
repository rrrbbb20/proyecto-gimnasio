package com.proyectogimnasio.rutina.controller;

import com.proyectogimnasio.rutina.dto.ApiResponse;
import com.proyectogimnasio.rutina.dto.RutinaResponse;
import com.proyectogimnasio.rutina.dto.RutinaRequest;
import com.proyectogimnasio.rutina.service.RutinaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/api/v3/rutinas")
@RequiredArgsConstructor
public class RutinaController {

    private final RutinaService service;

    @Operation(summary = "Agregar una rutina", description = "Agrega una rutina. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Rutina Creada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EntityModel<RutinaResponse>>> addRutinas(@Valid @RequestBody RutinaRequest r){
        RutinaResponse nuevaRutina = service.addRutina(r);
        EntityModel<RutinaResponse> recurso = crearRecursoRutina(nuevaRutina);

        return ResponseEntity.status(201).body(
                ApiResponse.<EntityModel<RutinaResponse>>builder()
                        .success(true)
                        .message("Rutina armada y creada exitosamente")
                        .data(recurso)
                        .build()
        );
    }

    @Operation(summary = "Obtener rutina por ID", description = "Busca una rutina usando su id. Requiere rol USER o ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Rutina obtenida"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rutina no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<EntityModel<RutinaResponse>>> findRutinas(@PathVariable Long id){
        RutinaResponse rutina = service.findRutina(id);
        EntityModel<RutinaResponse> recurso = crearRecursoRutina(rutina);

        return ResponseEntity.ok(
                ApiResponse.<EntityModel<RutinaResponse>>builder()
                        .success(true)
                        .message("Rutina obtenida")
                        .data(recurso)
                        .build()
        );
    }

    @Operation(summary = "Listar rutinas", description = "Retorna todas las rutinas registrados. Requiere rol USER o ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<RutinaResponse>>>> getRutinas(){
        List<RutinaResponse> rutinas = service.getRutinas();

        List<EntityModel<RutinaResponse>> rutinasConLinks = rutinas.stream()
                .map(this::crearRecursoRutina)
                .toList();

        CollectionModel<EntityModel<RutinaResponse>> coleccionRecursos = CollectionModel.of(rutinasConLinks);
        coleccionRecursos.add(linkTo(methodOn(RutinaController.class).getRutinas()).withSelfRel());

        return ResponseEntity.status(200).body(
                ApiResponse.<CollectionModel<EntityModel<RutinaResponse>>>builder()
                        .success(true)
                        .data(coleccionRecursos)
                        .build()
        );
    }

    @Operation(summary = "Actualizar rutina por ID ", description = "Busca una rutina usando su id. Requiere rol USER o ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Rutina Actualizada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rutina no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EntityModel<RutinaResponse>>> updateRutinas(@PathVariable Long id, @Valid @RequestBody RutinaRequest r) {
        RutinaResponse rutinaActualizada = service.updateRutina(id, r);
        EntityModel<RutinaResponse> recurso = crearRecursoRutina(rutinaActualizada);

        return ResponseEntity.ok(
                ApiResponse.<EntityModel<RutinaResponse>>builder()
                        .success(true)
                        .data(recurso)
                        .build()
        );
    }

    @Operation(summary = "Eliminar rutina por ID", description = "Elimina una rutina usando su identificador. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Rutina eliminada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rutina no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRutinas(@PathVariable Long id){
        service.deleteRutina(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Rutina eliminada correctamente")
                        .build()
        );
    }


    private EntityModel<RutinaResponse> crearRecursoRutina(RutinaResponse rutina) {
        EntityModel<RutinaResponse> recurso = EntityModel.of(rutina);
        Long id = rutina.getId(); // Verifica que RutinaResponse cuente con el método getId()

        recurso.add(linkTo(methodOn(RutinaController.class).findRutinas(id)).withSelfRel());
        recurso.add(linkTo(methodOn(RutinaController.class).getRutinas()).withRel("all"));
        recurso.add(linkTo(methodOn(RutinaController.class).updateRutinas(id, null)).withRel("update"));
        recurso.add(linkTo(methodOn(RutinaController.class).deleteRutinas(id)).withRel("delete"));

        return recurso;
    }
}