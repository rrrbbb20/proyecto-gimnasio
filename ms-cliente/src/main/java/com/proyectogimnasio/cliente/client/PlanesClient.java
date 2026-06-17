package com.proyectogimnasio.cliente.client;

import com.proyectogimnasio.cliente.dto.ApiResponse;
import com.proyectogimnasio.cliente.dto.PlanesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PlanesClient {
    private final WebClient webClient;
    private final String baseUrl;

    public PlanesClient(WebClient webClient, @Value("${planes.service.url:http://planes:8087}") String baseUrl) {
        this.webClient = webClient;
        this.baseUrl = baseUrl;
    }

    public PlanesResponse getPlan(Long id) {
        ApiResponse<PlanesResponse> response = webClient.get()
                .uri(baseUrl + "/api/v3/planes/" + id)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<PlanesResponse>>() {})
                .block();

        return response != null ? response.getData() : null;
    }

    public Object activarSuscripcion(Object suscripcionRequest) {
        return webClient.post()
                .uri(baseUrl + "/api/v3/suscripciones")
                .bodyValue(suscripcionRequest)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}