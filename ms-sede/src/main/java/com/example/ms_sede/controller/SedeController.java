package com.example.ms_sede.controller;


import com.example.ms_sede.dto.ApiResponse;
import com.example.ms_sede.dto.SedeRequest;
import com.example.ms_sede.dto.SedeResponse;
import com.example.ms_sede.model.Sede;
import com.example.ms_sede.service.SedeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/sede")
@RequiredArgsConstructor
public class SedeController {

    private final SedeService sedeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SedeResponse>> agregarSede(@Valid @RequestBody SedeRequest dto,
                                                                 @RequestHeader("Authorization")String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<SedeResponse>builder().success(true)
                        .message("Sede agregada")
                        .data(sedeService.createSede(dto,token)).build()
        );
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<SedeResponse>>> listar(@RequestHeader("Authorization") String token) {

        return ResponseEntity.status(200).body(
                ApiResponse.<List<SedeResponse>>builder().success(true)
                        .message("Las sedes se muestra a continuación")
                        .data(sedeService.listar(token)).build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<SedeResponse>> obtener(@PathVariable Long id,
                                                     @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(200).body(
                ApiResponse.<SedeResponse>builder().success(true)
                        .message("Sede encontrada")
                        .data(sedeService.obtenerSede(id,token)).build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SedeResponse>> actualizarSede(@PathVariable Long id,
                                                         @Valid @RequestBody SedeRequest dto,
                                                                    @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(

                ApiResponse.<SedeResponse>builder().success(true)
                        .message("Sede actualizada")
                        .data(sedeService.actualizarSede(id, dto, token)).build()
        );
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminarSede(@PathVariable Long id) {

        sedeService.eliminarSede(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Sede eliminada con exito")
                        .build()
        );
    }
}
