package com.example.ms_planes.controller;

import com.example.ms_planes.dto.ApiResponse;
import com.example.ms_planes.dto.PlanesRequest;
import com.example.ms_planes.dto.PlanesResponse;
import com.example.ms_planes.service.PlanesService;
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
    public ResponseEntity<ApiResponse<PlanesResponse>> addPlan(@Valid @RequestBody PlanesRequest p){

        return ResponseEntity.status(201).body(
                ApiResponse.<PlanesResponse>builder().success(true)
                        .message("Plan creado")
                        .data(planesService.addPlan(p)).build()

        );

    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<PlanesResponse>> findByIdPlan(@PathVariable Long id){
        return ResponseEntity.status(200).body(
                ApiResponse.<PlanesResponse>builder().success(true).message("Encontrado")
                        .data(planesService.findByIdPlan(id)).build()
        );
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<PlanesResponse>>> getAllPlanes(){

        return ResponseEntity.status(200).body(
                ApiResponse.<List<PlanesResponse>>builder().success(true)
                        .data(planesService.getAllPlanes()).build()
        );

    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PlanesResponse>> updatePlan(@PathVariable Long id, @Valid @RequestBody PlanesRequest p) {

        return ResponseEntity.ok(

                ApiResponse.<PlanesResponse>builder().success(true)
                        .data(planesService.updatePlan(id,p)).build()

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
