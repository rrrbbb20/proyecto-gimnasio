package com.example.ms_encargado.controller;

import com.example.ms_encargado.dto.ApiResponse;
import com.example.ms_encargado.dto.EncargadoRequest;
import com.example.ms_encargado.dto.EncargadoResponse;
import com.example.ms_encargado.service.EncargadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<EncargadoResponse>> add(@Valid @RequestBody EncargadoRequest e) {

        return ResponseEntity.status(201).body(
                ApiResponse.<EncargadoResponse>builder().success(true)
                        .message("Encargado creado")
                        .data(service.add(e)).build()
        );
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
    public ResponseEntity<ApiResponse<EncargadoResponse>> findById(@PathVariable Long id){
        return ResponseEntity.status(200).body(
                ApiResponse.<EncargadoResponse>builder().success(true).message("Encontrado")
                        .data(service.findById(id)).build()
        );
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
    public ResponseEntity<ApiResponse<List<EncargadoResponse>>> getAll(){

        return ResponseEntity.status(200).body(
                ApiResponse.<List<EncargadoResponse>>builder().success(true)
                        .data(service.getAll()).build()
        );

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
    public ResponseEntity<ApiResponse<EncargadoResponse>> update(@PathVariable Long id, @Valid @RequestBody EncargadoRequest e) {

        return ResponseEntity.ok(

                ApiResponse.<EncargadoResponse>builder().success(true)
                        .data(service.update(id,e)).build()

        );

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
