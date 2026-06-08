package com.example.ms_clase.controller;


import com.example.ms_clase.dto.ApiResponse;
import com.example.ms_clase.dto.ClaseRequest;
import com.example.ms_clase.dto.ClaseResponse;
import com.example.ms_clase.service.ClaseService;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.hateoas.EntityModel;
import static
        org.springframework.hateoas.server.mvc
                .WebMvcLinkBuilder.*;
@Tag(
        name = "Clases",
        description = "Endpoints para gestion de Entrenadores"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clases")
public class ClaseController {

    private final ClaseService service;

    @Operation(
            summary = "anadir clase",
            description = "Recibe los datos de la clase, se valida y realiza la creación."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Clase añadida correctamentee"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validación fallida "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token invalido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "No tienes permisos suficientes"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "El Id del entrenador asignado no existe")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<ApiResponse<ClaseResponse>>> add(
            @Valid @RequestBody ClaseRequest c,
            @Parameter(hidden = true)@RequestHeader("Authorization") String token) {

        ClaseResponse clase = service.add(c, token);

        ApiResponse<ClaseResponse> base =
                ApiResponse.<ClaseResponse>builder()
                        .success(true)
                        .message("Clase creada")
                        .data(clase)
                        .build();

        EntityModel<ApiResponse<ClaseResponse>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(ClaseController.class)
                .findById(clase.getId(),null)).withSelfRel()); //token por null

        recurso.add(linkTo(methodOn(ClaseController.class)
                .getAll(null)).withRel("all")); //token por null

        return ResponseEntity.status(201).body(recurso);
    }
    @Operation(
            summary = "encontrar clase proporcionando su id ",
            description = "permite retornar la clase buscada si existe "
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Clase encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token invalido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "No tienes permisos suficientes"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Clase no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<EntityModel<ApiResponse<ClaseResponse>>> findById(
            @Parameter(description = "id de la clase a buscar", example = "1", required = true)@PathVariable Long id,
            @Parameter(hidden = true)@RequestHeader("Authorization")String token){
        ApiResponse<ClaseResponse> base =
                ApiResponse.<ClaseResponse>builder()
                        .success(true)
                        .message("Clase encontrada")
                        .data(service.findById(id, token))
                        .build();

        EntityModel<ApiResponse<ClaseResponse>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(ClaseController.class)
                .findById(id, null)).withSelfRel());

        recurso.add(linkTo(methodOn(ClaseController.class)
                .getAll(null)).withRel("all"));

        recurso.add(linkTo(methodOn(ClaseController.class)
                .update(id, null, null)).withRel("update")); //cambie token por null

        recurso.add(linkTo(methodOn(ClaseController.class)
                .delete(id)).withRel("delete"));

        return ResponseEntity.ok(recurso);
    }
    @Operation(
            summary = "obtener a todas las clases registrados ",
            description = "permite retornar una lista de todos las clases que se encuentran registradas "
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de clases mostrada correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token invalido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "No tienes permisos suficientes")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<EntityModel<ApiResponse<List<ClaseResponse>>>> getAll(
            @Parameter(hidden = true)@RequestHeader("Authorization")
            String token){

        ApiResponse<List<ClaseResponse>> base =
                ApiResponse.<List<ClaseResponse>>builder()
                        .success(true)
                        .message("Listado de clases")
                        .data(service.getAll(token))
                        .build();

        EntityModel<ApiResponse<List<ClaseResponse>>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(ClaseController.class)
                .getAll(null)).withSelfRel()); //token por null

        recurso.add(linkTo(methodOn(ClaseController.class)
                .add(null, null)).withRel("create"));//token por null

        return ResponseEntity.ok(recurso);
    }
    @Operation(
            summary = "actualizar datos de una clase",
            description = "permite actualizar datos de una clase en particular mediante su id "
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Clase actualizada correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos incorrectos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token invalido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "No tienes permisos suficientes"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No se encontro la clase o al entrenador para actualizar")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<ApiResponse<ClaseResponse>>> update(
            @Parameter(description = "id de la clase que se desea modificar", example = "1", required = true)@PathVariable Long id,
            @Valid @RequestBody ClaseRequest c,
            @Parameter(hidden = true)@RequestHeader("Authorization")String token){

        ClaseResponse clase = service.update(id, c, token);

        ApiResponse<ClaseResponse> base =
                ApiResponse.<ClaseResponse>builder()
                        .success(true)
                        .message("Clase actualizada")
                        .data(clase)
                        .build();

        EntityModel<ApiResponse<ClaseResponse>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(ClaseController.class)
                .findById(id, null)).withSelfRel()); //token por null

        recurso.add(linkTo(methodOn(ClaseController.class)
                .getAll(null)).withRel("all")); //token por null

        recurso.add(linkTo(methodOn(ClaseController.class)
                .delete(id)).withRel("delete"));

        return ResponseEntity.ok(recurso);
    }
    @Operation(
            summary = "eliminar una clase registrada",
            description = "permite eliminar una clase mediante su id "
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Clase eliminada correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token invalido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "No tienes permisos suficientes"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Clase a eliminar no encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<Void> delete(
            @Parameter(description = "id de la clase a eliminar", example = "1", required = true)
            @PathVariable Long id){
        service.delete(id);


        return ResponseEntity.noContent().build();
    }
    @Operation(
            summary = "restar el cupo de una clase mediante su id",
            description = "si se inscribe una persona a la clase se procedera a restarle un cupo a ella"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Se resta cupo de manera exitosa"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token invalido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "No tienes permisos suficientes"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Id de clase no encontrado")
    })
    @PatchMapping("/restar-cupo/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<ApiResponse<Void>>> personaInscrita(
            @Parameter(description = "id de la clase a la que se necesita restar cupo", example = "1", required = true)
            @PathVariable Long id){
        service.personaInscrita(id);
        ApiResponse<Void> base =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Se resta cupo")
                        .build();

        EntityModel<ApiResponse<Void>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(ClaseController.class)
                .findById(id, null)).withRel("clase"));

        recurso.add(linkTo(methodOn(ClaseController.class)
                .getAll(null)).withRel("all"));

        return ResponseEntity.ok(recurso);

    }
    @Operation(
            summary = "sumar el cupo a una clase mediante su id",
            description = "si una persona retira su iscripcion a la clase se procedera sumarle un cupo a ella"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Se suma cupo correctamenrte"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token invalido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "No tienes permisos suficientes"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "id de clase no encontrado")
    })
    @PatchMapping("/sumar-cupo/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<ApiResponse<Void>>> removerInscripcion(
            @Parameter(description = "id de la clase a la que se necesita sumar cupo", example = "1", required = true)
            @PathVariable Long id){
        service.removerInscripcion(id);

        ApiResponse<Void> base =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Se suma cupo")
                        .build();

        EntityModel<ApiResponse<Void>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(ClaseController.class)
                .findById(id, null)).withRel("clase"));

        recurso.add(linkTo(methodOn(ClaseController.class)
                .getAll(null)).withRel("all"));

        return ResponseEntity.ok(recurso);

    }
    @Operation(
            summary = "retornar una lista de clases proporcionando su nombre",
            description = "si la clase buscada existe retornara una lista de las clases encontradas por ese nombre "
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Busqueda correcta"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token invalido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "No tienes permisos suficientes")
    })
    @GetMapping("/buscar-por-nombre/{nombre}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<EntityModel<ApiResponse<List<ClaseResponse>>>> buscarPorNombre(
            @Parameter(description = "nombre de la clase a buscar", example = "tenis", required = true)
            @PathVariable String nombre,
            @Parameter(hidden = true)@RequestHeader("Authorization")
            String token){

        ApiResponse<List<ClaseResponse>> base =
                ApiResponse.<List<ClaseResponse>>builder()
                        .success(true)
                        .message("Búsqueda por nombre")
                        .data(service.buscarPorNombre(nombre, token))
                        .build();

        EntityModel<ApiResponse<List<ClaseResponse>>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(ClaseController.class)
                .buscarPorNombre(nombre, null)).withSelfRel());

        recurso.add(linkTo(methodOn(ClaseController.class)
                .getAll(null)).withRel("all"));

        return ResponseEntity.ok(recurso);
    }
}
