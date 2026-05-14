package com.example.ms_mantenimiento.controller;

import com.example.ms_mantenimiento.dto.ApiResponse;
import com.example.ms_mantenimiento.dto.MantenimientoRequest;
import com.example.ms_mantenimiento.dto.MantenimientoResponse;
import com.example.ms_mantenimiento.service.MantenimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/mantenimiento")
@RequiredArgsConstructor
public class MantenimientoController {

    private final MantenimientoService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MantenimientoResponse>> add(@Valid @RequestBody MantenimientoRequest m){

        return ResponseEntity.status(201).body(
                ApiResponse.<MantenimientoResponse>builder().success(true)
                        .message("Entrenador creado")
                        .data(service.add(m)).build()

        );
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<MantenimientoResponse>> findById(@PathVariable Long id){
        return ResponseEntity.status(200).body(
                ApiResponse.<MantenimientoResponse>builder().success(true)
                        .message("Inventario encontrado")
                        .data(service.findById(id)).build()
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<MantenimientoResponse>>> getAll(){

        return ResponseEntity.status(200).body(
                ApiResponse.<List<MantenimientoResponse>>builder().success(true)
                        .message("Los mantenimiento se muestra a continuación")
                        .data(service.getAll()).build()
        );
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MantenimientoResponse>> update(@PathVariable Long id, @Valid @RequestBody MantenimientoRequest i) {

        return ResponseEntity.ok(

                ApiResponse.<MantenimientoResponse>builder().success(true)
                        .message("Mantenimiento actualizado")
                        .data(service.update(id, i)).build()

        );
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(

                ApiResponse.<Void>builder().success(true).message("Objeto del Inventario de id = "+ id +" Eliminado").build()
        );
    }
}
