package com.proyectogimnasio.rutina.controller;

import com.proyectogimnasio.rutina.dto.ApiResponse;
import com.proyectogimnasio.rutina.dto.EjercicioResponse;
import com.proyectogimnasio.rutina.dto.EjercicioRequest;
import com.proyectogimnasio.rutina.service.RutinaService;
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
@RequestMapping("/api/v3/ejercicios")
@RequiredArgsConstructor
public class EjercicioController {
    private final RutinaService service;
    @Operation(
            summary = "Agregar un ejercicio",
            description = "Agrega un ejercicio. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Ejercicio Creado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<EjercicioResponse>>>> addEjercicios(@Valid @RequestBody List<EjercicioRequest> ejerciciosRequest){

        List<EntityModel<EjercicioResponse>> ejerciciosCreados = ejerciciosRequest.stream()
                .map(service::addEjercicio)
                .map(this::crearRecursoEjercicio)
                .toList();

        CollectionModel<EntityModel<EjercicioResponse>> coleccionRecursos = CollectionModel.of(ejerciciosCreados);
        coleccionRecursos.add(linkTo(methodOn(EjercicioController.class).getEjercicios()).withRel("all"));

        return ResponseEntity.status(201).body(
                ApiResponse.<CollectionModel<EntityModel<EjercicioResponse>>>builder()
                        .success(true)
                        .message("Ejercicios creados exitosamente en el catálogo masivo")
                        .data(coleccionRecursos)
                        .build()
        );
    }
    @Operation(
            summary = "Obtener ejercicio por ID",
            description = "Busca un ejercicio usando su id. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ejercicio obtenido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ejercicio no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<EntityModel<EjercicioResponse>>> findEjercicios(@PathVariable("id") Long id){
        EjercicioResponse detalles = service.findEjercicio(id);
        EntityModel<EjercicioResponse> recurso = crearRecursoEjercicio(detalles);

        return ResponseEntity.ok(
                ApiResponse.<EntityModel<EjercicioResponse>>builder()
                        .success(true)
                        .message("Detalles obtenidos")
                        .data(recurso)
                        .build()
        );
    }
    @Operation(
            summary = "Listar ejercicios",
            description = "Retorna todos los ejercicios registrados. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<EjercicioResponse>>>> getEjercicios(){
        List<EjercicioResponse> ejercicios = service.getEjercicios();

        List<EntityModel<EjercicioResponse>> ejerciciosConLinks = ejercicios.stream()
                .map(this::crearRecursoEjercicio)
                .toList();

        CollectionModel<EntityModel<EjercicioResponse>> coleccionRecursos = CollectionModel.of(ejerciciosConLinks);
        coleccionRecursos.add(linkTo(methodOn(EjercicioController.class).getEjercicios()).withSelfRel());

        return ResponseEntity.status(200).body(
                ApiResponse.<CollectionModel<EntityModel<EjercicioResponse>>>builder()
                        .success(true)
                        .data(coleccionRecursos)
                        .build()
        );
    }
    @Operation(
            summary = "Actualizar ejercicio por ID ",
            description = "Busca un ejercicio usando su id. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Ejercicio Actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ejercicio no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EntityModel<EjercicioResponse>>> updateEjercicios(@PathVariable("id") Long id, @Valid @RequestBody EjercicioRequest e) {
        EjercicioResponse ejercicioActualizado = service.updateEjercicio(id, e);
        EntityModel<EjercicioResponse> recurso = crearRecursoEjercicio(ejercicioActualizado);

        return ResponseEntity.ok(
                ApiResponse.<EntityModel<EjercicioResponse>>builder()
                        .success(true)
                        .data(recurso)
                        .build()
        );
    }
    @Operation(
            summary = "Eliminar ejercicio por ID",
            description = "Elimina un ejercicio usando su identificador. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Ejercicio eliminado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ejercicio no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEjercicios(@PathVariable("id") Long idEjercicio){
        service.deleteEjercicio(idEjercicio);
        return ResponseEntity.ok(

                ApiResponse.<Void>builder().success(true).message("Ejercicio Eliminado").build()
        );
    }
    private EntityModel<EjercicioResponse> crearRecursoEjercicio(EjercicioResponse ejercicio) {
        EntityModel<EjercicioResponse> recurso = EntityModel.of(ejercicio);
        Long id = ejercicio.getId();

        recurso.add(linkTo(methodOn(EjercicioController.class).findEjercicios(id)).withSelfRel());
        recurso.add(linkTo(methodOn(EjercicioController.class).getEjercicios()).withRel("all"));
        recurso.add(linkTo(methodOn(EjercicioController.class).updateEjercicios(id, null)).withRel("update"));
        recurso.add(linkTo(methodOn(EjercicioController.class).deleteEjercicios(id)).withRel("delete"));

        return recurso;
    }

}
