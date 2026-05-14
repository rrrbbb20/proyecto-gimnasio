package com.example.ms_inventario.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_inventario.dto.MantenimientoResponse;
import com.example.ms_inventario.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MantenimientoClient {

    private final WebClient webClient;

    private final String BASE_URL = "http://localhost:8089/api/v1/mantenimiento/";

    public MantenimientoResponse obtenerMantenimiento(Long id, String token) {

        ApiResponse<MantenimientoResponse> response = webClient.get()
                .uri(BASE_URL + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<ApiResponse<MantenimientoResponse>>() {})
                .block();

        return response != null ? response.getData() : null;
    }
}