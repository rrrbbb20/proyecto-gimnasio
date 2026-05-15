package com.example.ms_sede.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_sede.dto.EncargadoResponse;
import com.example.ms_sede.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EncargadoClient {

    private final WebClient webClient;

    private final String BASE_URL = "http://localhost:8090/api/v1/encargado/";

    public EncargadoResponse obtenerEncargado(Long id, String token) {

        ApiResponse<EncargadoResponse> response = webClient.get()
                .uri(BASE_URL + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<ApiResponse<EncargadoResponse>>() {})
                .block();

        return response != null ? response.getData() : null;
    }
}