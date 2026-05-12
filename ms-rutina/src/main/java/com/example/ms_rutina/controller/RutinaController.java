package com.example.ms_rutina.controller;

import com.example.ms_rutina.dto.ApiResponse;
import com.example.ms_rutina.dto.RutinaRequest;
import com.example.ms_rutina.dto.RutinaResponse;
import com.example.ms_rutina.service.RutinaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/rutinas")
@RequiredArgsConstructor
public class RutinaController {
    private final RutinaService rutinaService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RutinaResponse>> add(@Valid @RequestBody RutinaRequest r){

        return ResponseEntity.status(201).body(
                ApiResponse.<RutinaResponse>builder().success(true)
                        .message("Rutina creada")
                        .data(rutinaService.add(r)).build()

        );

    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<RutinaResponse>> findById(@PathVariable Long id){
        return ResponseEntity.status(200).body(
                ApiResponse.<RutinaResponse>builder().success(true).message("Encontrado")
                        .data(rutinaService.findById(id)).build()
        );
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<RutinaResponse>>> getAll(){

        return ResponseEntity.status(200).body(
                ApiResponse.<List<RutinaResponse>>builder().success(true)
                        .data(rutinaService.getAll()).build()
        );

    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RutinaResponse>> update(@PathVariable Long id, @Valid @RequestBody RutinaRequest r) {

        return ResponseEntity.ok(

                ApiResponse.<RutinaResponse>builder().success(true)
                        .data(rutinaService.update(id,r)).build()

        );

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        rutinaService.delete(id);
        return ResponseEntity.ok(

                ApiResponse.<Void>builder().success(true).message("Entrenador Eliminado").build()
        );
    }
}
