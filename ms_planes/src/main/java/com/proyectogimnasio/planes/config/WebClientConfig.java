package com.proyectogimnasio.planes.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient webClient() {
        return WebClient.builder()
                .filter((request, next) -> {
                    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    if (attributes != null) {
                        HttpServletRequest currentRequest = attributes.getRequest();
                        String authHeader = currentRequest.getHeader(HttpHeaders.AUTHORIZATION);
                        if (authHeader != null && !authHeader.isEmpty()) {
                            ClientRequest filteredRequest = ClientRequest.from(request)
                                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                                    .build();
                            return next.exchange(filteredRequest);
                        }
                    }
                    return next.exchange(request);
                })
                .build();
    }

    private ExchangeFilterFunction bearerTokenFilter() {
        return (request, next) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getCredentials() != null) {
                String token = authentication.getCredentials().toString();

                ClientRequest filtered = ClientRequest.from(request)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build();
                return next.exchange(filtered);
            }

            return next.exchange(request);
        };
    }
}