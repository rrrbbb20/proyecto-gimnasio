package com.example.ms_inventario.controller;


import com.example.ms_inventario.dto.ApiResponse;
import com.example.ms_inventario.dto.InventarioRequest;
import com.example.ms_inventario.dto.InventarioResponse;
import com.example.ms_inventario.service.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InventarioResponse>> add(@Valid @RequestBody InventarioRequest i){

        return ResponseEntity.status(201).body(
                ApiResponse.<InventarioResponse>builder().success(true)
                        .message("Entrenador creado")
                        .data(service.add(i)).build()

        );
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<InventarioResponse>> findById(@PathVariable Long id){
        return ResponseEntity.status(200).body(
                ApiResponse.<InventarioResponse>builder().success(true)
                        .message("Inventario encontrado")
                        .data(service.findById(id)).build()
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<InventarioResponse>>> getAll(){

        return ResponseEntity.status(200).body(
                ApiResponse.<List<InventarioResponse>>builder().success(true)
                        .message("El inventario se muestra a continuación")
                        .data(service.getAll()).build()
        );
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InventarioResponse>> update(@PathVariable Long id, @Valid @RequestBody InventarioRequest i) {

        return ResponseEntity.ok(

                ApiResponse.<InventarioResponse>builder().success(true)
                        .message("Inventario actualizado")
                        .data(service.update(id, i)).build()

        );
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(

                ApiResponse.<Void>builder().success(true).message("Objeto del Inventario Eliminado").build()
        );
    }
}
