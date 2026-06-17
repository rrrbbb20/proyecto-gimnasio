package com.proyectogimnasio.planes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proyectogimnasio.planes.dto.PlanesRequest;
import com.proyectogimnasio.planes.dto.PlanesResponse;
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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlanesController.class)
class PlanesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private PlanesService planesService;

    private PlanesRequest planesRequest;
    private PlanesResponse planesResponse;

    @MockitoBean
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        planesRequest = new PlanesRequest();
        planesRequest.setNombrePlan("Plan Premium");
        planesRequest.setPrecioPlan(new BigDecimal("45000"));
        planesRequest.setDescripcionPlan("Acceso total");
        planesRequest.setBeneficios("Piscina + Gym");

        planesResponse = PlanesResponse.builder()
                .id(1L)
                .nombrePlan("Plan Premium")
                .precioPlan(new BigDecimal("45000"))
                .descripcionPlan("Acceso total")
                .beneficios("Piscina + Gym")
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addPlan_CuandoAdminYValido_DebeRetornar201YPlan() throws Exception {
        when(planesService.addPlan(any(PlanesRequest.class))).thenReturn(planesResponse);

        mockMvc.perform(post("/api/v3/planes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .param("token", "Bearer token-valido")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(planesRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.nombrePlan").value("Plan Premium"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void findByIdPlan_CuandoUser_DebeRetornarPlanConHateoas() throws Exception {
        when(planesService.findByIdPlan(eq(1L))).thenReturn(planesResponse);

        mockMvc.perform(get("/api/v3/planes/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .param("token", "Bearer token-valido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Plan obtenido"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data._links.self.href").exists())
                .andExpect(jsonPath("$.data._links.all.href").exists());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllPlanes_DebeRetornarListaDePlanes() throws Exception {
        when(planesService.getAllPlanes()).thenReturn(List.of(planesResponse));

        mockMvc.perform(get("/api/v3/planes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .param("token", "Bearer token-valido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].nombrePlan").value("Plan Premium"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePlan_CuandoAdmin_DebeActualizarYRetornarPlan() throws Exception {
        when(planesService.updatePlan(eq(1L), any(PlanesRequest.class))).thenReturn(planesResponse);

        mockMvc.perform(put("/api/v3/planes/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .param("token", "Bearer token-valido")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(planesRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombrePlan").value("Plan Premium"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePlan_CuandoAdmin_DebeRetornarOkConMensaje() throws Exception {
        doNothing().when(planesService).deletePlan(1L);

        mockMvc.perform(delete("/api/v3/planes/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Plan Eliminado"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deletePlan_CuandoUserNoAutorizado_DebeRetornar403() throws Exception {
        mockMvc.perform(delete("/api/v3/planes/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}