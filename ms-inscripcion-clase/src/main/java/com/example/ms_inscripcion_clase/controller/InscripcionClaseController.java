package com.example.ms_inscripcion_clase.controller;


import com.example.ms_inscripcion_clase.dto.ApiResponse;
import com.example.ms_inscripcion_clase.dto.ClaseResponse;
import com.example.ms_inscripcion_clase.dto.InscripcionClaseRequest;
import com.example.ms_inscripcion_clase.dto.InscripcionClaseResponse;
import com.example.ms_inscripcion_clase.service.InscripcionClaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc
        .WebMvcLinkBuilder.*;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(
        name = "inscripcion-clases",
        description = "Endpoints para gestion de inscripciones a clases"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inscripcion-clases")
public class InscripcionClaseController {

    private final InscripcionClaseService service;

    @Operation(
            summary = "registrar inscripcion a clase ",
            description = "Recibe los datos necesarios para crear la inscripcion a una clase, se valida y realiza la creación."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Inscripción creada correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en los datos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token invalido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "No tienes permisos suficientes"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "La clase o el cliente proporcionados no existen "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El cliente seleccionado ya esta inscrito en la clase")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<ApiResponse<InscripcionClaseResponse>>> add
            (@Valid @RequestBody InscripcionClaseRequest ic ,@Parameter(hidden = true)@RequestHeader("Authorization") String token){

        InscripcionClaseResponse inscripcion = service.add(ic, token);

        ApiResponse<InscripcionClaseResponse> base =
                ApiResponse.<InscripcionClaseResponse>builder()
                        .success(true)
                        .message("Inscripción creada")
                        .data(inscripcion)
                        .build();

        EntityModel<ApiResponse<InscripcionClaseResponse>> recurso =
                EntityModel.of(base);

        recurso.add(linkTo(methodOn(InscripcionClaseController.class)
                .findById(inscripcion.getIdInscripcion(), null)).withSelfRel()); //cambie token por null

        recurso.add(linkTo(methodOn(InscripcionClaseController.class)
                .getAll(null)).withRel("all")); //cambie token por null

        return ResponseEntity.status(201).body(recurso);
    }
    @Operation(
            summary = "encontrar una inscripcion proporcionando su id ",
            description = "permite retornar la inscripcion buscada si existe "
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Inscripcion a clase encontrada "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token invalido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "No tienes permisos suficientes"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No se encontro el id de inscripcion buscado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<EntityModel<ApiResponse<InscripcionClaseResponse>>> findById(
            @Parameter(description = "id de la inscripcion a buscar", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(hidden = true)@RequestHeader("Authorization")String token){
        ApiResponse<InscripcionClaseResponse> base =
                ApiResponse.<InscripcionClaseResponse>builder()
                        .success(true)
                        .message("Inscripción encontrada")
                        .data(service.findById(id, token))
                        .build();

        EntityModel<ApiResponse<InscripcionClaseResponse>> recurso =
                EntityModel.of(base);

        recurso.add(linkTo(methodOn(InscripcionClaseController.class)
                .findById(id, null)).withSelfRel()); //token por null

        recurso.add(linkTo(methodOn(InscripcionClaseController.class)
                .getAll(null)).withRel("all"));//cambie token por null

        recurso.add(linkTo(methodOn(InscripcionClaseController.class)
                .delete(id, null)).withRel("delete"));//cambie token por null

        return ResponseEntity.ok(recurso);

    }
    /*@PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InscripcionClaseResponse>> update(@PathVariable Long id,
                                                                        @Valid @RequestBody InscripcionClaseRequest ir,
                                                                        @RequestHeader("Authorization")String token){
        return ResponseEntity.status(200).body
                (ApiResponse.<InscripcionClaseResponse>builder()
                        .success(true)
                        .message("Inscripcion actualizada")
                        .data(service.update(id, ir,token))
                        .build());



    }*/
    @Operation(
            summary = "obtener a todas las inscripciones a clases registradas ",
            description = "permite retornar una lista de todos las inscripciones a clases que se encuentran registradas "
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "se devuelve listado de inscripciones correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token invalido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "No tienes permisos suficientes")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<EntityModel<ApiResponse<List<InscripcionClaseResponse>>>> getAll(@Parameter(hidden = true)@RequestHeader("Authorization") String token){

        ApiResponse<List<InscripcionClaseResponse>> base =
                ApiResponse.<List<InscripcionClaseResponse>>builder()
                        .success(true)
                        .message("Listado de inscripciones")
                        .data(service.getAll(token))
                        .build();

        EntityModel<ApiResponse<List<InscripcionClaseResponse>>> recurso =
                EntityModel.of(base);

        recurso.add(linkTo(methodOn(InscripcionClaseController.class)
                .getAll(null)).withSelfRel()); //cambie token por null

        recurso.add(linkTo(methodOn(InscripcionClaseController.class)
                .add(null, null)).withRel("create"));

        return ResponseEntity.ok(recurso);
    }
    @Operation(
            summary = "eliminar una inscripcion a clase registrada",
            description = "permite eliminar una inscripcion a clase mediante su id "
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Inscripción eliminada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token invalido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "No tienes permisos suficientes"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No se encontro la clase que buscabas")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "id de la inscripcion a clase que se desea eliminar", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(hidden = true) @RequestHeader("Authorization") String token){
        service.delete(id, token);


        return ResponseEntity.noContent().build();
    }
    @Operation(
            summary = "retornar una lista de clases almacenadas proporcionando su nombre",
            description = "si la clase buscada existe retornara una lista de las clases encontradas por ese nombre "
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Clases coincidentes localizadas con éxito"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado. Rol no autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No se localizó ninguna clase activa que coincida con el nombre proporcionado")
    })
    @GetMapping("/buscar-clase-por-nombre/{nombre}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<EntityModel<ApiResponse<List<ClaseResponse>>>> findClasePorNombre(
            @Parameter(description = "nombre de la clase a buscar", example = "tenis", required = true)
            @PathVariable String nombre,
            @Parameter(hidden = true)@RequestHeader("Authorization")String token){
        ApiResponse<List<ClaseResponse>> base =
                ApiResponse.<List<ClaseResponse>>builder()
                        .success(true)
                        .message("Clases encontradas")
                        .data(service.buscarClasePorNombre(nombre, token))
                        .build();

        EntityModel<ApiResponse<List<ClaseResponse>>> recurso =
                EntityModel.of(base);

        recurso.add(linkTo(methodOn(InscripcionClaseController.class)
                .findClasePorNombre(nombre, null)).withSelfRel());//cambie token por null

        recurso.add(linkTo(methodOn(InscripcionClaseController.class)
                .getAll(token)).withRel("all"));

        return ResponseEntity.ok(recurso);

    }
}
