package com.proyectogimnasio.planes.controller;

import com.proyectogimnasio.planes.dto.ApiResponse;
import com.proyectogimnasio.planes.dto.PagosRequest;
import com.proyectogimnasio.planes.dto.PagosResponse;
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
@RequestMapping("/api/v3/pagos")
@RequiredArgsConstructor
public class PagosController {

    private final PlanesService pagosService;

    @Operation(summary = "Agregar un metodo de pago", description = "Agrega un metodo de pago. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Metodo de pago Creado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EntityModel<PagosResponse>>> addPago(@Valid @RequestBody PagosRequest p){
        PagosResponse nuevoPago = pagosService.addPago(p);
        EntityModel<PagosResponse> recurso = crearRecursoPago(nuevoPago);

        return ResponseEntity.status(201).body(
                ApiResponse.<EntityModel<PagosResponse>>builder()
                        .success(true)
                        .message("Método de pago creado exitosamente")
                        .data(recurso)
                        .build()
        );
    }

    @Operation(summary = "Obtener metodo de pago por ID", description = "Busca un metodo de pago usando su id. Requiere rol USER o ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Metodo de pago obtenido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Metodo de pago no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<EntityModel<PagosResponse>>> obtenerPago(@PathVariable Long id){
        PagosResponse pago = pagosService.findByIdPago(id);
        EntityModel<PagosResponse> recurso = crearRecursoPago(pago);

        return ResponseEntity.ok(
                ApiResponse.<EntityModel<PagosResponse>>builder()
                        .success(true)
                        .message("Metodo de Pago obtenido")
                        .data(recurso)
                        .build()
        );
    }

    @Operation(summary = "Listar metodos de pago", description = "Retorna todos los metodos de pago registrados. Requiere rol USER o ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<PagosResponse>>>> getAllPagos(){
        List<PagosResponse> pagos = pagosService.getAllPagos();

        List<EntityModel<PagosResponse>> pagosConLinks = pagos.stream()
                .map(this::crearRecursoPago)
                .toList();

        CollectionModel<EntityModel<PagosResponse>> coleccionRecursos = CollectionModel.of(pagosConLinks);
        coleccionRecursos.add(linkTo(methodOn(PagosController.class).getAllPagos()).withSelfRel());

        return ResponseEntity.status(200).body(
                ApiResponse.<CollectionModel<EntityModel<PagosResponse>>>builder()
                        .success(true)
                        .data(coleccionRecursos)
                        .build()
        );
    }

    @Operation(summary = "Actualizar metodo de pago por ID ", description = "Actualiza un metodo de pago usando su id. Requiere rol USER o ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Metodo de pago Actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Metodo de pago no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EntityModel<PagosResponse>>> updatePago(@PathVariable Long id, @Valid @RequestBody PagosRequest p) {
        PagosResponse pagoActualizado = pagosService.updatePago(id, p);
        EntityModel<PagosResponse> recurso = crearRecursoPago(pagoActualizado);

        return ResponseEntity.ok(
                ApiResponse.<EntityModel<PagosResponse>>builder()
                        .success(true)
                        .data(recurso)
                        .build()
        );
    }

    @Operation(summary = "Eliminar metodo de pago por ID", description = "Elimina un metodo de pago usando su identificador. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Metodo de pago eliminado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Metodo de pago no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePago(@PathVariable Long id){
        pagosService.deletePago(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Método de pago eliminado")
                        .build()
        );
    }


    private EntityModel<PagosResponse> crearRecursoPago(PagosResponse pago) {
        EntityModel<PagosResponse> recurso = EntityModel.of(pago);
        Long id = pago.getId(); // Verifica que PagosResponse exponga el método getId()

        recurso.add(linkTo(methodOn(PagosController.class).obtenerPago(id)).withSelfRel());
        recurso.add(linkTo(methodOn(PagosController.class).getAllPagos()).withRel("all"));
        recurso.add(linkTo(methodOn(PagosController.class).updatePago(id, null)).withRel("update"));
        recurso.add(linkTo(methodOn(PagosController.class).deletePago(id)).withRel("delete"));

        return recurso;
    }
}