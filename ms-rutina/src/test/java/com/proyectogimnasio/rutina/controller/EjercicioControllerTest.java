package com.proyectogimnasio.rutina.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectogimnasio.rutina.dto.ApiResponse;
import com.proyectogimnasio.rutina.dto.EjercicioRequest;
import com.proyectogimnasio.rutina.dto.EjercicioResponse;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EjercicioController.class)
class EjercicioControllerTest {

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
    void debeCrearEjercicioGlobalExitosamente() throws Exception {
        // Arrange
        EjercicioRequest request = new EjercicioRequest();
        request.setNombreEjercicio("Cruce de Poleas");
        request.setZonaEjercitada("Pecho");
        request.setRepeticiones(12);

        EjercicioResponse responseMock = EjercicioResponse.builder()
                .id(5L)
                .nombreEjercicio("Cruce de Poleas")
                .zonaEjercitada("Pecho")
                .repeticiones(12)
                .build();

        when(service.addEjercicio(any(EjercicioRequest.class))).thenReturn(responseMock);

        // Act & Assert
        mockMvc.perform(post("/api/v3/ejercicios")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(request))) // Arreglo JSON correcto
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Ejercicios creados exitosamente en el catálogo masivo"))
                // CAMBIO AQUÍ: Al ser una lista, la respuesta de $.data será un arreglo,
                // por lo que usamos la sintaxis de índices de JSONPath [0]
                .andExpect(jsonPath("$.data.content[0].nombreEjercicio").value("Cruce de Poleas"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void debeObtenerEjercicioConHateoas() throws Exception {
        // Arrange
        EjercicioResponse responseMock = EjercicioResponse.builder()
                .id(1L)
                .nombreEjercicio("Flexiones")
                .zonaEjercitada("Pecho/Tríceps")
                .repeticiones(20)
                .build();

        when(service.findEjercicio(anyLong())).thenReturn(responseMock);

        // Act & Assert
        mockMvc.perform(get("/api/v3/ejercicios/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombreEjercicio").value("Flexiones"))

                // Validación estricta de HATEOAS mapeado en el Controlador
                .andExpect(jsonPath("$.data.links[?(@.rel=='self')].href").exists())
                .andExpect(jsonPath("$.data.links[?(@.rel=='all')].href").exists())
                .andExpect(jsonPath("$.data.links[?(@.rel=='delete')].href").exists());
    }

    @Test
    @WithMockUser(roles = "USER")
    void debeListarTodosLosEjercicios() throws Exception {
        // Arrange
        List<EjercicioResponse> listaMock = List.of(
                new EjercicioResponse(1L, "A", "Zona A", 10),
                new EjercicioResponse(2L, "B", "Zona B", 12)
        );
        when(service.getEjercicios()).thenReturn(listaMock);

        // Act & Assert
        mockMvc.perform(get("/api/v3/ejercicios")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].nombreEjercicio").value("A"))
                .andExpect(jsonPath("$.data.content[1].nombreEjercicio").value("B"));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void debeActualizarEjercicioExitosamente() throws Exception {
        // Arrange
        EjercicioRequest request = new EjercicioRequest();
        request.setNombreEjercicio("Curl de Bíceps");
        request.setZonaEjercitada("Bíceps");
        request.setRepeticiones(15);

        EjercicioResponse responseMock = EjercicioResponse.builder()
                .id(1L)
                .nombreEjercicio("Curl de Bíceps")
                .zonaEjercitada("Bíceps")
                .repeticiones(15)
                .build();

        when(service.updateEjercicio(anyLong(), any(EjercicioRequest.class))).thenReturn(responseMock);

        // Act & Assert
        mockMvc.perform(put("/api/v3/ejercicios/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombreEjercicio").value("Curl de Bíceps"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeEliminarEjercicioExitosamente() throws Exception {
        // Arrange
        doNothing().when(service).deleteEjercicio(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/api/v3/ejercicios/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Ejercicio Eliminado"));
    }
}