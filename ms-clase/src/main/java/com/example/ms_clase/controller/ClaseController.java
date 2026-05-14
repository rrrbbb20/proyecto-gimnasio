package com.example.ms_clase.controller;


import com.example.ms_clase.dto.ApiResponse;
import com.example.ms_clase.dto.ClaseRequest;
import com.example.ms_clase.dto.ClaseResponse;
import com.example.ms_clase.service.ClaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clases")
public class ClaseController {

    private final ClaseService service;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClaseResponse>> add(@Valid @RequestBody ClaseRequest c,
                                                          @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<ClaseResponse>builder().success(true)
                        .message("Clase anadida")
                        .data(service.add(c,token)).build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<ClaseResponse>> findById(@PathVariable Long id,
                                                               @RequestHeader("Authorization")String token){
        return ResponseEntity.ok(ApiResponse.<ClaseResponse>builder().success(true)
                .message("Clase Encontrada")
                .data(service.findById(id,token))
                .build()
        );
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<List<ClaseResponse>>> getAll(@RequestHeader("Authorization")
                                                                   String token){

        return ResponseEntity.ok(ApiResponse.<List<ClaseResponse>>builder()
                .success(true)
                .data(service.getAll(token))
                .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClaseResponse>> update(@PathVariable Long id,
                                                             @Valid @RequestBody ClaseRequest c,
                                                             @RequestHeader("Authorization")String token){

        return ResponseEntity.ok(ApiResponse.<ClaseResponse>builder()
                .success(true)
                .message("Clase Actualizada")
                .data(service.update(id,c,token))
                .build()
        );
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Clase eliminada")
                .build()
        );
    }

    @PatchMapping("/restar-cupo/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> personaInscrita(@PathVariable Long id,String token){
        service.personaInscrita(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder().message("Se Resta cupo").success(true)

                        .build()

        );

    }

    @PatchMapping("/sumar-cupo/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> removerInscripcion(@PathVariable Long id,String token){
        service.removerInscripcion(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder().message("Se suma cupo").success(true)

                        .build()

        );

    }
}
