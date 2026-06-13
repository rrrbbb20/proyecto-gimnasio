package com.proyectogimnasio.cliente.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proyectogimnasio.cliente.dto.ClienteRequest;
import com.proyectogimnasio.cliente.dto.ClienteResponse;
import com.proyectogimnasio.cliente.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private ObjectMapper objectMapper;

    @MockitoBean
    private ClienteService service;

    @MockitoBean
    private com.proyectogimnasio.cliente.security.JwtUtil jwtUtil;


    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    @Test
    @WithMockUser(roles = "USER")
    void debeListarClientes() throws Exception {
        // Arrange
        ClienteResponse cliente = new ClienteResponse(1L, "vicentito", "garcia", "7.435.565-9", "vicentito.garcia1@gmail.com", LocalDate.of(2007, 12, 1), 1L);
        List<ClienteResponse> clientes = List.of(cliente);

        when(service.getAll(anyString())).thenReturn(clientes);

        // Act & Assert
        mockMvc.perform(get("/api/v3/clientes")
                        .header("Authorization", "Bearer token-valido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].nombres").value("vicentito"))
                .andExpect(jsonPath("$.data[0].apellidos").value("garcia"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeCrearCliente() throws Exception {
        // Arrange
        ClienteRequest dto = new ClienteRequest();
        dto.setNombres("vicentito");
        dto.setApellidos("garcia");
        dto.setRun("7.435.565-9");
        dto.setCorreo("vicentito.garcia1@gmail.com");
        dto.setFechaNac(LocalDate.of(2007, 12, 1));
        dto.setIdPlan(1L);

        ClienteResponse creado = new ClienteResponse(1L, "vicentito", "garcia", "7.435.565-9", "vicentito.garcia1@gmail.com", LocalDate.of(2007, 12, 1), 1L);
        when(service.add(any(ClienteRequest.class), anyString())).thenReturn(creado);

        // Act & Assert
        mockMvc.perform(post("/api/v3/clientes")
                        .header("Authorization", "Bearer token-valido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())) //
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cliente añadido"))
                .andExpect(jsonPath("$.data.nombres").value("vicentito"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeActualizarCliente() throws Exception {
        // Arrange
        ClienteRequest dto = new ClienteRequest();
        dto.setNombres("Cliente nuevo");
        dto.setApellidos("Garcia");
        dto.setRun("7.435.565-9");
        dto.setCorreo("vicentito.garcia1@gmail.com");
        dto.setFechaNac(LocalDate.of(2007, 12, 1));
        dto.setIdPlan(1L);

        ClienteResponse actualizado = new ClienteResponse(1L, "Cliente nuevo", "Garcia", "7.435.565-9", "vicentito.garcia1@gmail.com", LocalDate.of(2007, 12, 1), 1L);

        when(service.update(eq(1L), any(ClienteRequest.class), anyString())).thenReturn(actualizado);

        // Act & Assert
        mockMvc.perform(put("/api/v3/clientes/1")
                        .header("Authorization", "Bearer token-falso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cliente Actualizado"))
                .andExpect(jsonPath("$.data.nombres").value("Cliente nuevo"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeEliminarCliente() throws Exception {
        // Arrange
        doNothing().when(service).delete(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v3/clientes/1")
                        .header("Authorization", "Bearer token-valido")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cliente eliminado"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void debeObtenerClienteConHateoas() throws Exception {
        //Arrange
        ClienteResponse clienteMock = new ClienteResponse();
        clienteMock.setId(1L);
        clienteMock.setNombres("Cristobal");
        clienteMock.setApellidos("Gomez");

        when(service.findById(anyLong(), anyString())).thenReturn(clienteMock);

        // Act & Assert
        mockMvc.perform(get("/api/v3/clientes/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-de-prueba-valido")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cliente obtenido"))


                .andExpect(jsonPath("$.data.links[?(@.rel=='self')].href").exists())
                .andExpect(jsonPath("$.data.links[?(@.rel=='all')].href").exists())
                .andExpect(jsonPath("$.data.links[?(@.rel=='update')].href").exists())
                .andExpect(jsonPath("$.data.links[?(@.rel=='delete')].href").exists());
    }
}