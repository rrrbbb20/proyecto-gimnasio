package com.proyectogimnasio.cliente.client;


import com.proyectogimnasio.planes.dto.ApiResponse;
import com.proyectogimnasio.planes.dto.PlanesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class PlanesClient {
    private final WebClient webClient;

    private final String BASE_URL = "http://localhost:8087/api/v2/planes/";

    public PlanesResponse getPlan(Long id, String token) {

        ApiResponse<PlanesResponse> response = webClient.get()
                .uri(BASE_URL + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<ApiResponse<PlanesResponse>>() {})
                .block();

        return response != null ? response.getData() : null;
    }
}
