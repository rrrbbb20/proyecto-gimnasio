package com.proyectogimnasio.cliente.controller;

import com.proyectogimnasio.cliente.dto.ApiResponse;
import com.proyectogimnasio.cliente.dto.ClienteRequest;
import com.proyectogimnasio.cliente.dto.ClienteResponse;
import com.proyectogimnasio.cliente.model.Cliente;
import com.proyectogimnasio.cliente.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;
@Tag(name = "Clientes", description = "Operaciones relacionadas con clientes")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/clientes")
public class ClienteController {
    private final ClienteService service;



    @Operation(
            summary = "Agregar un Autor",
            description = "Agrega un autor. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Cliente Creado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClienteResponse>> add(@Valid @RequestBody ClienteRequest c,
                                                            @RequestHeader("Authorization") String token){
        return ResponseEntity.status(201).body(
                ApiResponse.<ClienteResponse>builder().success(true)
                        .message("Cliente anadido")
                        .data(service.add(c,token)).build()
        );
    }
    @Operation(
            summary = "Obtener cliente por ID",
            description = "Busca un cliente usando su identificador. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cliente obtenido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<ClienteResponse>> findById(@PathVariable Long id,
                                                               @RequestHeader("Authorization")String token){
        return ResponseEntity.ok(ApiResponse.<ClienteResponse>builder().success(true)
                .message("Cliente Encontrado")
                .data(service.findById(id,token))
                .build()
        );

    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClienteResponse>> update(@PathVariable Long id,
                                                             @Valid @RequestBody ClienteRequest c,
                                                             @RequestHeader("Authorization")String token){

        return ResponseEntity.ok(ApiResponse.<ClienteResponse>builder()
                .success(true)
                .message("Clase Actualizada")
                .data(service.update(id,c,token))
                .build()
        );
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<EntityModel<Cliente>>> obtener(@PathVariable Long id, String token) {

        Cliente cliente = service.findById(id, token);

        EntityModel<Cliente> recurso = EntityModel.of(cliente);

        recurso.add(
                linkTo(methodOn(ClienteController.class).obtener(id, token))
                        .withSelfRel()
        );

        recurso.add(
                linkTo(methodOn(ClienteController.class).getAll(token))
                        .withRel("all")
        );

        recurso.add(
                linkTo(methodOn(ClienteController.class).update(id, null, token))
                        .withRel("update")
        );

        recurso.add(
                linkTo(methodOn(ClienteController.class).delete(id))
                        .withRel("delete")
        );

        return ResponseEntity.ok(
                ApiResponse.<EntityModel<Cliente>>builder()
                        .success(true)
                        .message("Autor obtenido")
                        .data(recurso)
                        .build()
        );
    }
    @Operation(
            summary = "Listar clientes",
            description = "Retorna todos los clientes registrados. Requiere rol USER o ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<List<ClienteResponse>>> getAll(@RequestHeader("Authorization")
                                                                   String token){

        return ResponseEntity.ok(ApiResponse.<List<ClienteResponse>>builder()
                .success(true)
                .data(service.getAll(token))
                .build()
        );
    }
    @Operation(
            summary = "Eliminar autor por ID",
            description = "Elimina un autor usando su identificador. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Cliente eliminado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Cliente eliminado")
                .build()
        );
    }

}
