package com.example.ms_sede.controller;


import com.example.ms_sede.dto.ApiResponse;
import com.example.ms_sede.dto.SedeDTO;
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
    public ResponseEntity<ApiResponse<Sede>> agregarSede(@Valid @RequestBody SedeDTO dto) {

        Sede sede = sedeService.createSede(dto);

        return ResponseEntity.status(201).body(
                ApiResponse.<Sede>builder()
                        .success(true)
                        .message("Sede agregada")
                        .data(sede)
                        .build()
        );
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<Sede>>> listar() {

        return ResponseEntity.ok(
                ApiResponse.<List<Sede>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(sedeService.listar())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<Sede>> obtener(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.<Sede>builder()
                        .success(true)
                        .message("Autor obtenido")
                        .data(sedeService.obtenerSede(id))
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Sede>> actualizarSede(@PathVariable Long id,
                                                         @Valid @RequestBody SedeDTO dto) {

        Sede sede = sedeService.actualizarSede(id, dto);

        return ResponseEntity.ok(
                ApiResponse.<Sede>builder()
                        .success(true)
                        .message("Autor actualizado")
                        .data(sede)
                        .build()
        );
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminarSede(@PathVariable Long id) {

        sedeService.eliminarSede(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Autor eliminado")
                        .build()
        );
    }
}
