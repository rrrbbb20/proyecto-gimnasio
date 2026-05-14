package com.example.ms_rutina.controller;

import com.example.ms_rutina.dto.ApiResponse;
import com.example.ms_rutina.dto.EjercicioRequest;
import com.example.ms_rutina.dto.EjercicioResponse;
import com.example.ms_rutina.service.RutinaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/ejercicios")
@RequiredArgsConstructor
public class EjercicioController {
    private final RutinaService ejercicioService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EjercicioResponse>> addEjercicio(@Valid @RequestBody EjercicioRequest e){

        return ResponseEntity.status(201).body(
                ApiResponse.<EjercicioResponse>builder().success(true)
                        .message("Ejercicio creado")
                        .data(ejercicioService.addEjercicio(e)).build()

        );

    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<EjercicioResponse>> findById(@PathVariable("id") Long idEjercicio){
        return ResponseEntity.status(200).body(
                ApiResponse.<EjercicioResponse>builder().success(true).message("Encontrado")
                        .data(ejercicioService.findByIdEjercicio(idEjercicio)).build()
        );
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<EjercicioResponse>>> getAll(){

        return ResponseEntity.status(200).body(
                ApiResponse.<List<EjercicioResponse>>builder().success(true)
                        .data(ejercicioService.getAllEjercicios()).build()
        );

    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EjercicioResponse>> update(@PathVariable("id") Long idEjercicio, @Valid @RequestBody EjercicioRequest e) {

        return ResponseEntity.ok(

                ApiResponse.<EjercicioResponse>builder().success(true)
                        .data(ejercicioService.updateEjercicio(idEjercicio,e)).build()

        );

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") Long idEjercicio){
        ejercicioService.deleteEjercicio(idEjercicio);
        return ResponseEntity.ok(

                ApiResponse.<Void>builder().success(true).message("Ejercicio Eliminado").build()
        );
    }

}
