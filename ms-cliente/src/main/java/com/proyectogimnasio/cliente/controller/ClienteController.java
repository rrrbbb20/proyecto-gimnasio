package com.proyectogimnasio.cliente.controller;

import com.proyectogimnasio.cliente.dto.ApiResponse;
import com.proyectogimnasio.cliente.dto.ClienteRequest;
import com.proyectogimnasio.cliente.dto.ClienteResponse;
import com.proyectogimnasio.cliente.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/clientes")
public class ClienteController {
    private final ClienteService service;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClienteResponse>> add(@Valid @RequestBody ClienteRequest c,
                                                            @RequestHeader("Authorization") String token){
        return ResponseEntity.status(201).body(
                ApiResponse.<ClienteResponse>builder().success(true)
                        .message("Cliente anadido")
                        .data(service.add(c,token)).build()
        );
    }
}
