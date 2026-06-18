package com.proyectogimnasio.cliente.controller;

import com.proyectogimnasio.cliente.dto.ApiResponse;
import com.proyectogimnasio.cliente.dto.ClienteRequest;
import com.proyectogimnasio.cliente.dto.ClienteResponse;
import com.proyectogimnasio.cliente.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Clientes", description = "Operaciones relacionadas con clientes")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/clientes")
public class ClienteController {

    private final ClienteService service;

    @Operation(summary = "Agregar un Cliente", description = "Agrega un cliente. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Cliente Creado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EntityModel<ClienteResponse>>> add(@Valid @RequestBody ClienteRequest c) {
        ClienteResponse nuevoCliente = service.add(c);
        EntityModel<ClienteResponse> recurso = crearRecursoCliente(nuevoCliente);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<EntityModel<ClienteResponse>>builder()
                        .success(true)
                        .message("Cliente creado exitosamente")
                        .data(recurso)
                        .build()
        );
    }

    @Operation(summary = "Obtener todos los clientes", description = "Recupera la lista de todos los clientes registrados.")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<ClienteResponse>>>> getAll() {
        List<ClienteResponse> clientes = service.getAll();

        // Convertimos cada ClienteResponse en un EntityModel con sus propios links individuales
        List<EntityModel<ClienteResponse>> clientesConLinks = clientes.stream()
                .map(this::crearRecursoCliente)
                .toList();

        // Creamos la colección HATEOAS y le añadimos el link al propio endpoint de la lista
        CollectionModel<EntityModel<ClienteResponse>> coleccionRecursos = CollectionModel.of(clientesConLinks);
        coleccionRecursos.add(linkTo(methodOn(ClienteController.class).getAll()).withSelfRel());

        return ResponseEntity.ok(ApiResponse.<CollectionModel<EntityModel<ClienteResponse>>>builder()
                .success(true)
                .data(coleccionRecursos)
                .build()
        );
    }

        @Operation(
                summary = "Obtener cliente por ID",
                description = "Busca un cliente usando su id. Requiere rol USER o ADMIN."
        )
        @ApiResponses(value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cliente obtenido"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
        })

        @GetMapping("/{id}") // El único GET para /{id}
        @PreAuthorize("hasAnyRole('USER','ADMIN')")
        public ResponseEntity<ApiResponse<EntityModel<ClienteResponse>>> obtener(@PathVariable Long id) {
            ClienteResponse cliente = service.findById(id);
            EntityModel<ClienteResponse> recurso = crearRecursoCliente(cliente);

            return ResponseEntity.ok(
                    ApiResponse.<EntityModel<ClienteResponse>>builder()
                            .success(true)
                            .message("Cliente obtenido")
                            .data(recurso)
                            .build()
            );
        }

        @Operation(summary = "Actualizar cliente por ID", description = "Modifica los datos de un cliente existente.")
        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<EntityModel<ClienteResponse>>> update(@PathVariable Long id, @Valid @RequestBody ClienteRequest c) {
            ClienteResponse clienteActualizado = service.update(id, c);
            EntityModel<ClienteResponse> recurso = crearRecursoCliente(clienteActualizado);

            return ResponseEntity.ok(ApiResponse.<EntityModel<ClienteResponse>>builder()
                    .success(true)
                    .message("Cliente actualizado exitosamente")
                    .data(recurso)
                    .build()
            );
        }

        @Operation(summary = "Eliminar cliente por ID", description = "Elimina un cliente usando su identificador. Requiere rol ADMIN.")
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<Void>> delete (@PathVariable Long id){
            service.delete(id);
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .success(true)
                    .message("Cliente eliminado")
                    .build()
            );
        }
        private EntityModel<ClienteResponse> crearRecursoCliente (ClienteResponse cliente){
            EntityModel<ClienteResponse> recurso = EntityModel.of(cliente);
            Long id = cliente.getId(); // Asegúrate de que ClienteResponse tenga un método getId()

            recurso.add(linkTo(methodOn(ClienteController.class).obtener(id)).withSelfRel());
            recurso.add(linkTo(methodOn(ClienteController.class).getAll()).withRel("all"));
            recurso.add(linkTo(methodOn(ClienteController.class).update(id, null)).withRel("update"));
            recurso.add(linkTo(methodOn(ClienteController.class).delete(id)).withRel("delete"));

            return recurso;
        }
    }
