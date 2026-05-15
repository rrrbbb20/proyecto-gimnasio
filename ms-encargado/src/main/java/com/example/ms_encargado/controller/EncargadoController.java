package com.example.ms_encargado.controller;

import com.example.ms_encargado.dto.ApiResponse;
import com.example.ms_encargado.dto.EncargadoRequest;
import com.example.ms_encargado.dto.EncargadoResponse;
import com.example.ms_encargado.service.EncargadoService;
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

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EncargadoResponse>> add(@Valid @RequestBody EncargadoRequest e) {

        return ResponseEntity.status(201).body(
                ApiResponse.<EncargadoResponse>builder().success(true)
                        .message("Encargado creado")
                        .data(service.add(e)).build()
        );
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<EncargadoResponse>> findById(@PathVariable Long id){
        return ResponseEntity.status(200).body(
                ApiResponse.<EncargadoResponse>builder().success(true).message("Encontrado")
                        .data(service.findById(id)).build()
        );
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<EncargadoResponse>>> getAll(){

        return ResponseEntity.status(200).body(
                ApiResponse.<List<EncargadoResponse>>builder().success(true)
                        .data(service.getAll()).build()
        );

    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EncargadoResponse>> update(@PathVariable Long id, @Valid @RequestBody EncargadoRequest e) {

        return ResponseEntity.ok(

                ApiResponse.<EncargadoResponse>builder().success(true)
                        .data(service.update(id,e)).build()

        );

    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(

                ApiResponse.<Void>builder().success(true).message("Encargado Eliminado").build()
        );
    }
}
