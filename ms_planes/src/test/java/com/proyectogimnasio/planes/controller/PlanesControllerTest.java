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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlanesController.class)
public class PlanesControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private ObjectMapper objectMapper;

    @MockitoBean
    private PlanesService planesService;

    private PlanesRequest requestValido;
    private PlanesResponse responseMock;

    @MockitoBean
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        // Inicializamos un objeto válido de acuerdo a las restricciones de validación (@NotBlank, @NotNull) de PlanesRequest
        requestValido = new PlanesRequest();
        requestValido.setNombrePlan("Plan Fuerza Premium");
        requestValido.setPrecioPlan(new BigDecimal("35000"));
        requestValido.setDescripcionPlan("Acceso total a sala de pesas y musculación");
        requestValido.setBeneficios("Evaluación de Kinesiólogo + Rutina personalizada");
        requestValido.setIdPago(1L);

        // Mock de respuesta esperada desde la capa Service
        responseMock = PlanesResponse.builder()
                .id(1L)
                .nombrePlan("Plan Fuerza Premium")
                .precioPlan(new BigDecimal("35000"))
                .descripcionPlan("Acceso total a sala de pesas y musculación")
                .beneficios("Evaluación de Kinesiólogo + Rutina personalizada")
                .idPago(1L)
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void debeAgregarPlanCuandoAdminYRequestValido() throws Exception {
        when(planesService.addPlan(any(PlanesRequest.class), any())).thenReturn(responseMock);

        mockMvc.perform(post("/api/v3/planes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido)))
                .andExpect(status().isCreated()) // Espera 201 de acuerdo a tu controlador
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Plan creado"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.nombrePlan").value("Plan Fuerza Premium"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void debeDevolverBadRequestCuandoCamposSonNulos() throws Exception {
        PlanesRequest requestInvalido = new PlanesRequest(); // Al estar vacío, rompe las validaciones del DTO

        mockMvc.perform(post("/api/v3/planes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest()); // Espera 400 Bad Request
    }


    @Test
    @WithMockUser(roles = "USER")
    public void debeObtenerPlanPorIdConEnlacesHateoas() throws Exception {
        when(planesService.findByIdPlan(anyLong(), any())).thenReturn(responseMock);

        mockMvc.perform(get("/api/v3/planes/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Espera 200 OK
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Plan obtenido"))
                // Valida los links estructurales HATEOAS autogenerados por el EntityModel
                .andExpect(jsonPath("$.data.links[?(@.rel=='self')].href").exists())
                .andExpect(jsonPath("$.data.links[?(@.rel=='all')].href").exists())
                .andExpect(jsonPath("$.data.links[?(@.rel=='update')].href").exists())
                .andExpect(jsonPath("$.data.links[?(@.rel=='delete')].href").exists());
    }


    @Test
    @WithMockUser(roles = "USER")
    public void debeListarTodosLosPlanes() throws Exception {
        when(planesService.getAllPlanes(any())).thenReturn(List.of(responseMock));

        mockMvc.perform(get("/api/v3/planes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1L));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void debeActualizarPlanCuandoAdminYRequestValido() throws Exception {
        when(planesService.updatePlan(anyLong(), any(PlanesRequest.class), any())).thenReturn(responseMock);

        mockMvc.perform(put("/api/v3/planes/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void debeEliminarPlanCuandoAdmin() throws Exception {
        doNothing().when(planesService).deletePlan(anyLong());

        mockMvc.perform(delete("/api/v3/planes/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Plan Eliminado"));
    }


}