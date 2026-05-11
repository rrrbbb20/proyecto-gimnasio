package com.example.ms_cliente.controller;

import com.example.ms_cliente.dto.ApiResponse;
import com.example.ms_cliente.dto.ClienteRequest;
import com.example.ms_cliente.dto.ClienteResponse;
import com.example.ms_cliente.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClienteResponse>> add(@Valid @RequestBody ClienteRequest e){

        return ResponseEntity.status(201).body(
                ApiResponse.<ClienteResponse>builder().success(true)
                        .message("Entrenador creado")
                        .data(service.add(e)).build()

        );

    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<ClienteResponse>> findById(@PathVariable Long id){
        return ResponseEntity.status(200).body(
                ApiResponse.<ClienteResponse>builder().success(true).message("Encontrado")
                        .data(service.findById(id)).build()
        );
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<ClienteResponse>>> getAll(){

        return ResponseEntity.status(200).body(
                ApiResponse.<List<ClienteResponse>>builder().success(true)
                        .data(service.getAll()).build()
        );

    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClienteResponse>> update(@PathVariable Long id, @Valid @RequestBody ClienteRequest e) {

        return ResponseEntity.ok(

                ApiResponse.<ClienteResponse>builder().success(true)
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
