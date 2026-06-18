package com.proyectogimnasio.planes.client;

import com.proyectogimnasio.planes.dto.ApiResponse;
import com.proyectogimnasio.planes.dto.ClienteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ClienteClient {
    private final WebClient webClient;
    private final String baseUrl;

    // Inyectamos la URL configurada dinámicamente desde el yml
    public ClienteClient(WebClient webClient, @Value("${cliente.service.url}") String baseUrl) {
        this.webClient = webClient;
        this.baseUrl = baseUrl;
    }

    public ClienteResponse getCliente(Long id) {
        ApiResponse<ClienteResponse> response = webClient.get()
                .uri(baseUrl + "/api/v3/clientes/" + id )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<ClienteResponse>>() {})
                .block();

        return response != null ? response.getData() : null;
    }
}