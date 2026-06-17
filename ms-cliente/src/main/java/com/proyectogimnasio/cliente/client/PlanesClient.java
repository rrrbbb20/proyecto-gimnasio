package com.proyectogimnasio.cliente.client;

import com.proyectogimnasio.cliente.dto.ApiResponse;
import com.proyectogimnasio.cliente.dto.PlanesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class PlanesClient {
    private final WebClient webClient;
    private final String BASE_URL = "http://localhost:8087/api/v3/planes/";

    public PlanesResponse getPlan(Long id) {
        ApiResponse<PlanesResponse> response = webClient.get()
                .uri(BASE_URL + id)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<PlanesResponse>>() {})
                .block();

        return response != null ? response.getData() : null;
    }

    public Object activarSuscripcion(Object suscripcionRequest) {
        return webClient.post()
                .uri("http://localhost:8087/api/v1/suscripciones")
                .bodyValue(suscripcionRequest)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}