package com.proyectogimnasio.planes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proyectogimnasio.planes.dto.SuscripcionRequest;
import com.proyectogimnasio.planes.dto.SuscripcionResponse;
import com.proyectogimnasio.planes.security.JwtUtil;
import com.proyectogimnasio.planes.service.PlanesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SuscripcionController.class)
class SuscripcionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private PlanesService planesService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private SuscripcionRequest requestValido;
    private SuscripcionResponse responseMock;
    private final String tokenValido = "Bearer token-valido";

    @BeforeEach
    void setUp() {
        requestValido = new SuscripcionRequest();
        requestValido.setIdCliente(1L);
        requestValido.setIdPlan(2L);

        responseMock = SuscripcionResponse.builder()
                .id(10L)
                .idCliente(1L)
                .estado("ACTIVA")
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusMonths(6))
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(roles = "USER")
    void activarSuscripcion_CuandoRequestValido_DebeRetornar201YSuscripcion() throws Exception {
        when(planesService.crearSuscripcion(any(SuscripcionRequest.class))).thenReturn(responseMock);

        mockMvc.perform(post("/api/v1/suscripciones")
                        .header(HttpHeaders.AUTHORIZATION, tokenValido)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Suscripción activada con éxito"))
                .andExpect(jsonPath("$.data.id").value(10L))
                .andExpect(jsonPath("$.data.idCliente").value(1L))
                .andExpect(jsonPath("$.data.estado").value("ACTIVA"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void obtenerTodas_CuandoAdmin_DebeRetornarListado() throws Exception {
        when(planesService.getAllSuscripciones()).thenReturn(List.of(responseMock));

        mockMvc.perform(get("/api/v1/suscripciones")
                        .header(HttpHeaders.AUTHORIZATION, tokenValido))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(10L));
    }

    @Test
    @WithMockUser(roles = "USER")
    void obtenerTodas_CuandoUserNoAutorizado_DebeRetornar403() throws Exception {
        mockMvc.perform(get("/api/v1/suscripciones")
                        .header(HttpHeaders.AUTHORIZATION, tokenValido))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void obtenerPorCliente_CuandoRolUser_DebeRetornarSuscripcion() throws Exception {
        when(planesService.getSuscripcionByCliente(eq(1L))).thenReturn(responseMock);

        mockMvc.perform(get("/api/v1/suscripciones/cliente/1")
                        .header(HttpHeaders.AUTHORIZATION, tokenValido))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.idCliente").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cambiarEstado_CuandoAdmin_DebeActualizarYRetornarSuscripcion() throws Exception {
        responseMock.setEstado("CANCELADA");
        when(planesService.updateSuscripcion(eq(10L), eq("CANCELADA"))).thenReturn(responseMock);

        mockMvc.perform(put("/api/v1/suscripciones/10/estado")
                        .header(HttpHeaders.AUTHORIZATION, tokenValido)
                        .param("nuevoEstado", "CANCELADA")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Estado actualizado correctamente"))
                .andExpect(jsonPath("$.data.estado").value("CANCELADA"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void cambiarEstado_CuandoUserNoAutorizado_DebeRetornar403() throws Exception {
        mockMvc.perform(put("/api/v1/suscripciones/10/estado")
                        .header(HttpHeaders.AUTHORIZATION, tokenValido)
                        .param("nuevoEstado", "CANCELADA")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void eliminarSuscripcion_CuandoAdmin_DebeRetornarOkConMensaje() throws Exception {
        doNothing().when(planesService).deleteSuscripcion(anyLong());

        mockMvc.perform(delete("/api/v1/suscripciones/10")
                        .header(HttpHeaders.AUTHORIZATION, tokenValido)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Suscripción eliminada correctamente del sistema"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void eliminarSuscripcion_CuandoUserNoAutorizado_DebeRetornar403() throws Exception {
        mockMvc.perform(delete("/api/v1/suscripciones/10")
                        .header(HttpHeaders.AUTHORIZATION, tokenValido)
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}