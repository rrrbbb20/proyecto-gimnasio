package com.example.ms_inscripcion_clase.controller;


import com.example.ms_inscripcion_clase.dto.ApiResponse;
import com.example.ms_inscripcion_clase.dto.InscripcionClaseRequest;
import com.example.ms_inscripcion_clase.dto.InscripcionClaseResponse;
import com.example.ms_inscripcion_clase.service.InscripcionClaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inscripcion-clases")
public class InscripcionClaseController {

    private final InscripcionClaseService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InscripcionClaseResponse>> add
            (@Valid @RequestBody InscripcionClaseRequest ic ,@RequestHeader("Authorization") String token){

        return ResponseEntity.status(201).body
                (ApiResponse.<InscripcionClaseResponse>builder()
                .success(true)
                .message("Creado correctamente")
                .data(service.add(ic,token))
                .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<InscripcionClaseResponse>> findById(@PathVariable Long id,
                                                                          @RequestHeader("Authorization")String token){
        return ResponseEntity.status(200).body
                (ApiResponse.<InscripcionClaseResponse>builder()
                        .success(true)
                        .message("Inscripcion Encontrada")
                        .data(service.findById(id,token))
                        .build());

    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InscripcionClaseResponse>> update(@PathVariable Long id,
                                                                        @Valid @RequestBody InscripcionClaseRequest ir,
                                                                        @RequestHeader("Authorization")String token){
        return ResponseEntity.status(200).body
                (ApiResponse.<InscripcionClaseResponse>builder()
                        .success(true)
                        .message("Inscripcion actualizada")
                        .data(service.update(id, ir,token))
                        .build());



    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<List<InscripcionClaseResponse>>> getAll(@RequestHeader("Authorization") String token){

        return ResponseEntity.ok(ApiResponse.<List<InscripcionClaseResponse>>builder()
                .success(true)
                .data(service.getAll(token))
                .build()
        );
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Inscripcion a Clase eliminada")
                .build()
        );
    }

}
