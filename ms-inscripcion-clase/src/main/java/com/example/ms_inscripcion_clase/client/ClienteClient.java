package com.example.ms_inscripcion_clase.client;

import com.example.ms_inscripcion_clase.dto.ApiResponse;
import com.example.ms_inscripcion_clase.dto.ClaseResponse;
import com.example.ms_inscripcion_clase.dto.ClienteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ClienteClient {

    private final WebClient webClient;

    private final String BASE_URL = "http://localhost:8083/api/v1/clientes/";

    public ClienteResponse getCliente(Long id, String token) {

        ApiResponse<ClienteResponse> response = webClient.get()
                .uri(BASE_URL + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<ApiResponse<ClienteResponse >>() {})
                .block();

        return response != null ? response.getData() : null;
    }

}
