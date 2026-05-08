package com.example.ms_entrenador.controller;


import com.example.ms_entrenador.dto.ApiResponse;
import com.example.ms_entrenador.dto.EntrenadorRequest;
import com.example.ms_entrenador.dto.EntrenadorResponse;
import com.example.ms_entrenador.service.EntrenadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/entrenadores")
public class EntrenadorController {

    private final EntrenadorService service;


    public ResponseEntity<ApiResponse<EntrenadorResponse>> add(EntrenadorRequest e){

        return ResponseEntity.status(201).body(
            ApiResponse.<EntrenadorResponse>builder().success(true)
                    .message("Entrenador creado")
                    .data(service.add(e)).build()

        );

    }

    public ResponseEntity<ApiResponse<EntrenadorResponse>> findById(Long id){
        return ResponseEntity.status(200).body(
                ApiResponse.<EntrenadorResponse>builder().success(true).message("Encontrado")
                        .data(service.findById(id)).build()
        );
    }

    public ResponseEntity<ApiResponse<List<EntrenadorResponse>>> getAll(){

        return ResponseEntity.status(200).body(
                ApiResponse.<List<EntrenadorResponse>>builder().success(true)
                        .data(service.getAll()).build()
        );

    }

    public ResponseEntity<ApiResponse<EntrenadorResponse>> update(Long id, EntrenadorRequest e) {

        return ResponseEntity.ok(

                ApiResponse.<EntrenadorResponse>builder().success(true)
                        .data(service.update(id,e)).build()

        );

    }


    public ResponseEntity<ApiResponse<Void>> delete(Long id){

        return ResponseEntity.ok(

                ApiResponse.<Void>builder().success(true).message("Entrenador Eliminado").build()
        );
    }


}
