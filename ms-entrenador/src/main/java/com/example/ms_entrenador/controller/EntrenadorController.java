package com.example.ms_entrenador.controller;


import com.example.ms_entrenador.dto.ApiResponse;
import com.example.ms_entrenador.dto.EntrenadorRequest;
import com.example.ms_entrenador.dto.EntrenadorResponse;
import com.example.ms_entrenador.service.EntrenadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/entrenadores")
@RequiredArgsConstructor
public class EntrenadorController {

    private final EntrenadorService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EntrenadorResponse>> add(@Valid @RequestBody EntrenadorRequest e){

        return ResponseEntity.status(201).body(
            ApiResponse.<EntrenadorResponse>builder().success(true)
                    .message("Entrenador creado")
                    .data(service.add(e)).build()

        );

    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<EntrenadorResponse>> findById(@PathVariable Long id){
        return ResponseEntity.status(200).body(
                ApiResponse.<EntrenadorResponse>builder().success(true).message("Encontrado")
                        .data(service.findById(id)).build()
        );
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<EntrenadorResponse>>> getAll(){

        return ResponseEntity.status(200).body(
                ApiResponse.<List<EntrenadorResponse>>builder().success(true)
                        .data(service.getAll()).build()
        );

    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EntrenadorResponse>> update(@PathVariable Long id, @Valid @RequestBody EntrenadorRequest e) {

        return ResponseEntity.ok(

                ApiResponse.<EntrenadorResponse>builder().success(true)
                        .data(service.update(id,e)).build()

        );

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(

                ApiResponse.<Void>builder().success(true).message("Entrenador Eliminado").build()
        );
    }


}
