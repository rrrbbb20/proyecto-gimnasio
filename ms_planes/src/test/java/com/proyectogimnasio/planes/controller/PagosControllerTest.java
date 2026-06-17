package com.proyectogimnasio.planes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proyectogimnasio.planes.dto.PagosRequest;
import com.proyectogimnasio.planes.dto.PagosResponse;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PagosController.class)
class PagosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private PlanesService pagosService;

    private PagosRequest requestValido;
    private PagosResponse responseMock;

    @MockitoBean
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        requestValido = new PagosRequest();
        requestValido.setTipoPago("Tarjeta de Credito");
        requestValido.setNumTarjeta("1111222233334444");
        requestValido.setFechaVencimiento("10/30");
        requestValido.setCvc(123);
        requestValido.setDireccionFacturacion("Calle Falsa 123");
        requestValido.setCodigoPostal("8320000");
        requestValido.setIdCliente(1L);

        responseMock = PagosResponse.builder()
                .id(1L)
                .tipoPago("Tarjeta de Credito")
                .numTarjeta("1111222233334444")
                .fechaVencimiento("10/30")
                .cvc(123)
                .direccionFacturacion("Calle Falsa 123")
                .codigoPostal("8320000")
                .idCliente(1L)
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void debeAgregarPagoCuandoAdminYRequestValido() throws Exception {
        when(pagosService.addPago(any(PagosRequest.class))).thenReturn(responseMock);

        mockMvc.perform(post("/api/v2/pagos")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .param("token", "Bearer token-valido")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Plan creado"))
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void debeDevolverBadRequestCuandoCamposSonNulos() throws Exception {
        PagosRequest requestInvalido = new PagosRequest();

        mockMvc.perform(post("/api/v2/pagos")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void debeObtenerPagoPorIdConEnlacesHateoas() throws Exception {
        when(pagosService.findByIdPago(anyLong())).thenReturn(responseMock);

        mockMvc.perform(get("/api/v2/pagos/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .param("token", "Bearer token-valido")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Metodo de Pago obtenido"))
                .andExpect(jsonPath("$.data._links.self.href").exists())
                .andExpect(jsonPath("$.data._links.all.href").exists())
                .andExpect(jsonPath("$.data._links.update.href").exists())
                .andExpect(jsonPath("$.data._links.delete.href").exists());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void debeListarTodosLosPagos() throws Exception {
        when(pagosService.getAllPagos()).thenReturn(List.of(responseMock));

        mockMvc.perform(get("/api/v2/pagos")
                        .header("Authorization", "Bearer token-valido")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void debeActualizarPagoCuandoAdminYRequestValido() throws Exception {
        when(pagosService.updatePago(anyLong(), any(PagosRequest.class))).thenReturn(responseMock);

        mockMvc.perform(put("/api/v2/pagos/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .param("token", "Bearer token-valido")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void debeEliminarPagoCuandoAdmin() throws Exception {
        doNothing().when(pagosService).deletePago(anyLong());

        mockMvc.perform(delete("/api/v2/pagos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Método de pago eliminado"));
    }
}