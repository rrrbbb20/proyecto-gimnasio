package com.example.ms_planes.controller;

import com.example.ms_planes.dto.ApiResponse;
import com.example.ms_planes.dto.PagosRequest;
import com.example.ms_planes.dto.PagosResponse;
import com.example.ms_planes.service.PlanesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
public class PagosController {
    private final PlanesService pagosService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PagosResponse>> addPago(@Valid @RequestBody PagosRequest p){

        return ResponseEntity.status(201).body(
                ApiResponse.<PagosResponse>builder().success(true)
                        .message("Plan creado")
                        .data(pagosService.addPago(p)).build()

        );

    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<PagosResponse>> findByIdPago(@PathVariable Long id){
        return ResponseEntity.status(200).body(
                ApiResponse.<PagosResponse>builder().success(true).message("Encontrado")
                        .data(pagosService.findByIdPago(id)).build()
        );
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<PagosResponse>>> getAllPagos(){

        return ResponseEntity.status(200).body(
                ApiResponse.<List<PagosResponse>>builder().success(true)
                        .data(pagosService.getAllPagos()).build()
        );

    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PagosResponse>> updatePago(@PathVariable Long id, @Valid @RequestBody PagosRequest p) {

        return ResponseEntity.ok(

                ApiResponse.<PagosResponse>builder().success(true)
                        .data(pagosService.updatePago(id,p)).build()

        );

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePago(@PathVariable Long id){
        pagosService.deletePago(id);
        return ResponseEntity.ok(

                ApiResponse.<Void>builder().success(true).message("Plan Eliminado").build()
        );
    }
}
