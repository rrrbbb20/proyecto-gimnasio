package com.proyectogimnasio.cliente.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proyectogimnasio.cliente.dto.ClienteRequest;
import com.proyectogimnasio.cliente.dto.ClienteResponse;
import com.proyectogimnasio.cliente.security.JwtUtil;
import com.proyectogimnasio.cliente.service.ClienteService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService service;

    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void debeAgregarCliente() throws Exception {
        ClienteRequest request = new ClienteRequest();
        request.setNombres("Cristobal");
        request.setApellidos("Gomez");
        request.setRun("12345678-9");
        request.setCorreo("cristobal@gmail.com");
        request.setFechaNac(LocalDate.of(2000, 1, 1));
        request.setIdPlan(1L);

        ClienteResponse response = ClienteResponse.builder()
                .id(1L)
                .nombres("Cristobal")
                .apellidos("Gomez")
                .build();

        when(service.add(any(ClienteRequest.class), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/v3/clientes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombres").value("Cristobal"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void debeObtenerClienteConHateoas() throws Exception {
        // Arrange
        ClienteResponse clienteMock = ClienteResponse.builder()
                .id(1L)
                .nombres("Cristobal")
                .apellidos("Gomez")
                .idPlan(1L)
                .correo("cristobal@gmail.com")
                .run("12345678-9")
                .fechaNac(LocalDate.of(2000, 1, 1))
                .build();

        when(service.findById(anyLong(), anyString())).thenReturn(clienteMock);

        // Act & Assert
        mockMvc.perform(get("/api/v3/clientes/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-de-prueba-valido")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cliente obtenido"))
                // Validaciones de hipermedios HATEOAS estructurales
                .andExpect(jsonPath("$.data.links[?(@.rel=='self')].href").exists())
                .andExpect(jsonPath("$.data.links[?(@.rel=='all')].href").exists())
                .andExpect(jsonPath("$.data.links[?(@.rel=='update')].href").exists())
                .andExpect(jsonPath("$.data.links[?(@.rel=='delete')].href").exists());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void debeListarClientes() throws Exception {
        ClienteResponse clienteMock = ClienteResponse.builder().id(1L).nombres("Cristobal").build();
        when(service.getAll(anyString())).thenReturn(List.of(clienteMock));

        mockMvc.perform(get("/api/v3/clientes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].nombres").value("Cristobal"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void debeEliminarCliente() throws Exception {
        doNothing().when(service).delete(anyLong());

        mockMvc.perform(delete("/api/v3/clientes/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cliente eliminado"));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void debeActualizarCliente() throws Exception {
        // Arrange
        Long idCliente = 1L;

        ClienteRequest request = new ClienteRequest();
        request.setNombres("Cristobal Modificado");
        request.setApellidos("Gomez");
        request.setRun("12345678-9");
        request.setCorreo("cristobal.nuevo@gmail.com");
        request.setFechaNac(LocalDate.of(2000, 1, 1));
        request.setIdPlan(2L); // Cambiando el plan, por ejemplo

        ClienteResponse responseMock = ClienteResponse.builder()
                .id(idCliente)
                .nombres("Cristobal Modificado")
                .apellidos("Gomez")
                .run("12345678-9")
                .correo("cristobal.nuevo@gmail.com")
                .fechaNac(LocalDate.of(2000, 1, 1))
                .idPlan(2L)
                .build();

        // Simulamos que el servicio recibe cualquier request, el ID y cualquier token, y retorna el mock corregido
        when(service.update(eq(idCliente), any(ClienteRequest.class), anyString())).thenReturn(responseMock);

        // Act & Assert
        mockMvc.perform(put("/api/v3/clientes/{id}", idCliente)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .with(csrf()) // Necesario ya que es un método de escritura (PUT) protegido
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombres").value("Cristobal Modificado"))
                .andExpect(jsonPath("$.data.idPlan").value(2L));

        // Verificamos que el controlador realmente invoque al servicio con los parámetros correctos
        verify(service, times(1)).update(eq(idCliente), any(ClienteRequest.class), anyString());
    }
}