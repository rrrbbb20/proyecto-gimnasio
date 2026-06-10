package com.proyectogimnasio.planes.controller;

import com.proyectogimnasio.planes.dto.ApiResponse;
import com.proyectogimnasio.planes.dto.PlanesRequest;
import com.proyectogimnasio.planes.dto.PlanesResponse;
import com.proyectogimnasio.planes.service.PlanesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/planes")
@RequiredArgsConstructor
public class PlanesController {

    private final PlanesService planesService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PlanesResponse>> addPlan(@Valid @RequestBody PlanesRequest p, String token){

        return ResponseEntity.status(201).body(
                ApiResponse.<PlanesResponse>builder().success(true)
                        .message("Plan creado")
                        .data(planesService.addPlan(p, token)).build()

        );

    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<PlanesResponse>> findByIdPlan(@PathVariable Long id, String token){
        return ResponseEntity.status(200).body(
                ApiResponse.<PlanesResponse>builder().success(true).message("Encontrado")
                        .data(planesService.findByIdPlan(id, token)).build()
        );
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<PlanesResponse>>> getAllPlanes(String token){

        return ResponseEntity.status(200).body(
                ApiResponse.<List<PlanesResponse>>builder().success(true)
                        .data(planesService.getAllPlanes(token)).build()
        );

    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PlanesResponse>> updatePlan(@PathVariable Long id, @Valid @RequestBody PlanesRequest p, String token) {

        return ResponseEntity.ok(

                ApiResponse.<PlanesResponse>builder().success(true)
                        .data(planesService.updatePlan(id,p, token)).build()

        );

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePlan(@PathVariable Long id){
        planesService.deletePlan(id);
        return ResponseEntity.ok(

                ApiResponse.<Void>builder().success(true).message("Plan Eliminado").build()
        );
    }
}