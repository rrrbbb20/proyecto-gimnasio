package com.example.ms_clase.client;

import com.example.ms_clase.dto.ApiResponse;
import com.example.ms_clase.dto.EntrenadorResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class EntrenadorClient {


    private final WebClient webClient;

    private final String BASE_URL = "http://localhost:8083/api/entrenadores/";

    public EntrenadorResponse getEntrenador(Long id, String token) {

        ApiResponse<EntrenadorResponse> response = webClient.get()
                .uri(BASE_URL + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<ApiResponse<EntrenadorResponse>>() {})
                .block();

        return response != null ? response.getData() : null;
    }
}
