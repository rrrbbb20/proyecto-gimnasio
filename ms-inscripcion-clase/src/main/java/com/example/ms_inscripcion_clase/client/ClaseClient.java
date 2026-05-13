package com.example.ms_inscripcion_clase.client;


import com.example.ms_inscripcion_clase.dto.ApiResponse;
import com.example.ms_inscripcion_clase.dto.ClaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ClaseClient {
    private final WebClient webClient;

    private final String BASE_URL = "http://localhost:8084/api/v1/clases/";

    public ClaseResponse getClase(Long id, String token) {

        ApiResponse<ClaseResponse> response = webClient.get()
                .uri(BASE_URL + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<ApiResponse<ClaseResponse>>() {})
                .block();

        return response != null ? response.getData() : null;
    }

}
