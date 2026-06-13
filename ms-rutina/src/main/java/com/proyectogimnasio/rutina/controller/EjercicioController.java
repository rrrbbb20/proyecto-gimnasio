package com.proyectogimnasio.rutina.controller;

import com.proyectogimnasio.rutina.dto.ApiResponse;
import com.proyectogimnasio.rutina.dto.EjercicioResponse;
import com.proyectogimnasio.rutina.dto.EjercicioRequest;
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
    public ResponseEntity<ApiResponse<EjercicioResponse>> addEjercicios(@Valid @RequestBody EjercicioRequest e,String token){

        return ResponseEntity.status(201).body(
                ApiResponse.<EjercicioResponse>builder().success(true)
                        .message("Ejercicio creado")
                        .data(service.addEjercicio(e, token)).build()

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
    public ResponseEntity<ApiResponse<EntityModel<EjercicioResponse>>> findEjercicios(@PathVariable("id") Long id, String token){
        EjercicioResponse detalles = service.findEjercicio(id, token);
        EntityModel<EjercicioResponse> recurso = EntityModel.of(detalles);

        recurso.add(linkTo(methodOn(EjercicioController.class).findEjercicios(id, token)).withSelfRel());
        recurso.add(linkTo(methodOn(EjercicioController.class).getEjercicios(token)).withRel("all"));
        recurso.add(linkTo(methodOn(EjercicioController.class).updateEjercicios(id, null, token)).withRel("update"));
        recurso.add(linkTo(methodOn(EjercicioController.class).deleteEjercicios(id)).withRel("delete"));

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
    public ResponseEntity<ApiResponse<List<EjercicioResponse>>> getEjercicios(String token){

        return ResponseEntity.status(200).body(
                ApiResponse.<List<EjercicioResponse>>builder().success(true)
                        .data(service.getEjercicios(token)).build()
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
    public ResponseEntity<ApiResponse<EjercicioResponse>> updateEjercicios(@PathVariable("id") Long id, @Valid @RequestBody EjercicioRequest e, String token) {

        return ResponseEntity.ok(

                ApiResponse.<EjercicioResponse>builder().success(true)
                        .data(service.updateEjercicio(id,e, token)).build()

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

}
