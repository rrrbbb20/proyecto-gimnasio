package com.example.ms_encargado.controller;

import com.example.ms_encargado.dto.ApiResponse;
import com.example.ms_encargado.dto.EncargadoRequest;
import com.example.ms_encargado.dto.EncargadoResponse;
import com.example.ms_encargado.service.EncargadoService;
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
@RequestMapping("/api/v1/encargado")
@RequiredArgsConstructor
public class EncargadoController {

    private final EncargadoService service;


    @Operation(
            summary = "Agregar encargado",
            description = "Agrega encargado a la lista. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Encargado agregaedo"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Encargado no agregado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<ApiResponse<EncargadoResponse>>> add(@Valid @RequestBody EncargadoRequest e) {

        EncargadoResponse encargado = service.add(e);

        ApiResponse<EncargadoResponse> respuestaBase =
                ApiResponse.<EncargadoResponse>builder()
                        .success(true)
                        .message("Encargado creado")
                        .data(encargado)
                        .build();

        EntityModel<ApiResponse<EncargadoResponse>> recurso =
                EntityModel.of(respuestaBase);

        recurso.add(linkTo(methodOn(EncargadoController.class)
                .findById(encargado.getId(),null)).withSelfRel());

        recurso.add(linkTo(methodOn(EncargadoController.class)
                .getAll(null)).withRel("all"));

        return ResponseEntity.status(201).body(recurso);

    }
    @Operation(
            summary = "Obtener lista de encargados por id",
            description = "Obtiene los datos del encargado con el id ingresado. Requiere rol ADMIN o USER."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Encargado encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Encargado no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<EntityModel<ApiResponse<EncargadoResponse>>> findById(
            @Parameter(description = "id del encargado a buscar", example = "1", required = true)@PathVariable Long id,
            @Parameter(hidden = true)@RequestHeader("Authorization")String token){
        ApiResponse<EncargadoResponse> base =
                ApiResponse.<EncargadoResponse>builder()
                        .success(true)
                        .message("Encargado encontrado")
                        .data(service.findById(id, token))
                        .build();

        EntityModel<ApiResponse<EncargadoResponse>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(EncargadoController.class)
                .findById(id, null)).withSelfRel());

        recurso.add(linkTo(methodOn(EncargadoController.class)
                .getAll(null)).withRel("all"));

        //recurso.add(linkTo(methodOn(EncargadoController.class)
        //        .update(id, null,null)).withRel("update")); //cambie token por null

        recurso.add(linkTo(methodOn(EncargadoController.class)
                .delete(id)).withRel("delete"));

        return ResponseEntity.ok(recurso);
    }
    @Operation(
            summary = "Obtener lista de encargados",
            description = "Obtiene la lista completa de los encargados. Requiere rol ADMIN o USER."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Encargados encontrados"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Lista no encontada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<EntityModel<ApiResponse<List<EncargadoResponse>>>> getAll(
            @Parameter(hidden = true)@RequestHeader("Authorization")
            String token){

        ApiResponse<List<EncargadoResponse>> base =
                ApiResponse.<List<EncargadoResponse>>builder()
                        .success(true)
                        .message("Listado de encargados")
                        .data(service.getAll())
                        .build();

        EntityModel<ApiResponse<List<EncargadoResponse>>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(EncargadoController.class)
                .getAll(null)).withSelfRel()); //token por null

        //recurso.add(linkTo(methodOn(EncargadoController.class)
        //        .add(null)).withRel("create"));//token por null

        return ResponseEntity.ok(recurso);
    }
    @Operation(
            summary = "Actualizar atributos del encargado de la id ingresada",
            description = "Actualiza atributos del encargado de id ingresado. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Encargado Actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Encargado no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<ApiResponse<EncargadoResponse>>> update(
            @Parameter(description = "id del encargado que se desea modificar", example = "1", required = true)@PathVariable Long id,
            @Valid @RequestBody EncargadoRequest e,
            @Parameter(hidden = true)@RequestHeader("Authorization")String token){

        EncargadoResponse clase = service.update(id, e, token);

        ApiResponse<EncargadoResponse> base =
                ApiResponse.<EncargadoResponse>builder()
                        .success(true)
                        .message("Encargado actualizado")
                        .data(clase)
                        .build();

        EntityModel<ApiResponse<EncargadoResponse>> recurso = EntityModel.of(base);

        recurso.add(linkTo(methodOn(EncargadoController.class)
                .findById(id, null)).withSelfRel()); //token por null

        recurso.add(linkTo(methodOn(EncargadoController.class)
                .getAll(null)).withRel("all")); //token por null

        recurso.add(linkTo(methodOn(EncargadoController.class)
                .delete(id)).withRel("delete"));

        return ResponseEntity.ok(recurso);
    }
    @Operation(
            summary = "Elimina un encargado por id",
            description = "Elimina un encargado del id ingresado respectivamente. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Encargado eliminado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Encargado no encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(

                ApiResponse.<Void>builder().success(true).message("Encargado Eliminado").build()
        );
    }
}
