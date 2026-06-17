package com.proyectogimnasio.planes.client;



import com.proyectogimnasio.planes.dto.ApiResponse;
import com.proyectogimnasio.planes.dto.ClienteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ClienteClient {
    private final WebClient webClient;

    private final String BASE_URL = "http://localhost:8083/api/v3/clientes/";

    public ClienteResponse getCliente(Long id) {
        ApiResponse<ClienteResponse> response = webClient.get()
                .uri(BASE_URL + id)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<ClienteResponse>>() {})
                .block();

        return response != null ? response.getData() : null;
    }
    }

