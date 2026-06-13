package com.proyectogimnasio.rutina.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectogimnasio.rutina.dto.ApiResponse;
import com.proyectogimnasio.rutina.dto.DetallesEjercicioResponse;
import com.proyectogimnasio.rutina.dto.RutinaRequest;
import com.proyectogimnasio.rutina.dto.RutinaResponse;
import com.proyectogimnasio.rutina.service.RutinaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RutinaController.class)
public class RutinaControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private ObjectMapper objectMapper;

    @MockitoBean
    private RutinaService service;

    @MockitoBean
    private com.proyectogimnasio.rutina.security.JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeCrearRutinaExitosamente() throws Exception {
        // Arrange
        RutinaRequest request = new RutinaRequest();
        request.setNombreRutina("Definición Extrema");
        request.setDescripcionRutina("Circuitos rápidos");
        request.setDetalles(new ArrayList<>());

        RutinaResponse responseMock = RutinaResponse.builder()
                .id(1L)
                .nombreRutina("Definición Extrema")
                .descripcionRutina("Circuitos rápidos")
                .detalles(new ArrayList<>())
                .build();

        when(service.addRutina(any(RutinaRequest.class), any())).thenReturn(responseMock);
        ApiResponse<RutinaResponse> apiResponse = ApiResponse.<RutinaResponse>builder()
                .success(true)
                .message("Rutina armada y creada exitosamente")
                .data(responseMock)
                .error(null)
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v3/rutinas")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Rutina armada y creada exitosamente"))
                .andExpect(jsonPath("$.data.nombreRutina").value("Definición Extrema"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void debeObtenerRutinaConHateoas() throws Exception {
        // Arrange
        RutinaResponse mockResponse = RutinaResponse.builder()
                .id(1L)
                .nombreRutina("Fuerza 5x5")
                .descripcionRutina("Multiarticulares pesados")
                .detalles(List.of(
                        DetallesEjercicioResponse.builder()
                                .id(10L)
                                .ejercicioId(2L)
                                .nombreEjercicio("Peso Muerto")
                                .zonaEjercitada("Espalda/Piernas")
                                .numeroEjercicios(5)
                                .duracionRutina("20 min")
                                .tiempoDescanso("3 min")
                                .build()
                ))
                .build();

        when(service.findRutina(anyLong(), any())).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v3/rutinas/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombreRutina").value("Fuerza 5x5"))
                .andExpect(jsonPath("$.data.detalles[0].nombreEjercicio").value("Peso Muerto"))

                // Validación de enlaces HATEOAS construidos en el Controller
                .andExpect(jsonPath("$.data.links[?(@.rel=='self')].href").exists())
                .andExpect(jsonPath("$.data.links[?(@.rel=='all')].href").exists())
                .andExpect(jsonPath("$.data.links[?(@.rel=='delete')].href").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeEliminarRutinaExitosamente() throws Exception {
        // Arrange
        doNothing().when(service).deleteRutina(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v3/rutinas/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Rutina eliminada correctamente"));
    }
    @Test
    @WithMockUser(roles = "USER")
    void debeListarTodasLasRutinas() throws Exception {
        // Arrange
        RutinaResponse r1 = RutinaResponse.builder().id(1L).nombreRutina("Rutina Cardio").descripcionRutina("Bajar grasa").detalles(new ArrayList<>()).build();
        RutinaResponse r2 = RutinaResponse.builder().id(2L).nombreRutina("Rutina Fuerza").descripcionRutina("Ganar poder").detalles(new ArrayList<>()).build();

        when(service.getRutinas(any())).thenReturn(List.of(r1, r2));
        // Act & Assert
        mockMvc.perform(get("/api/v3/rutinas")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].nombreRutina").value("Rutina Cardio"))
                .andExpect(jsonPath("$.data[1].nombreRutina").value("Rutina Fuerza"));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void debeActualizarRutinaExitosamente() throws Exception {
        // Arrange
        RutinaRequest request = new RutinaRequest();
        request.setNombreRutina("Fuerza Modificada");
        request.setDescripcionRutina("Cambio de ejercicios");
        request.setDetalles(new ArrayList<>());

        RutinaResponse responseMock = RutinaResponse.builder()
                .id(1L)
                .nombreRutina("Fuerza Modificada")
                .descripcionRutina("Cambio de ejercicios")
                .detalles(new ArrayList<>())
                .build();

        when(service.updateRutina(anyLong(), any(RutinaRequest.class), any())).thenReturn(responseMock);

        // Act & Assert
        mockMvc.perform(put("/api/v3/rutinas/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombreRutina").value("Fuerza Modificada"));
    }

}