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
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService service;

    @MockitoBean
    private JwtUtil jwtUtil;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    private Map<String, Object> getPagoMock() {
        return Map.of(
                "tipoPago", "CREDITO",
                "numTarjeta", "4555111122223333",
                "fechaVencimiento", "11/30",
                "cvc", 999,
                "direccionFacturacion", "Av. Providencia 1234",
                "codigoPostal", "7500000"
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeAgregarClienteCuandoRolEsAdmin() throws Exception {
        // Arrange
        ClienteRequest request = new ClienteRequest();
        request.setNombres("Cristobal");
        request.setApellidos("Gomez");
        request.setRun("12345678-9");
        request.setCorreo("cristobal@gmail.com");
        request.setFechaNac(LocalDate.of(2000, 1, 1));
        request.setIdPlan(1L);
        request.setPago(getPagoMock());

        ClienteResponse responseMock = ClienteResponse.builder()
                .id(1L)
                .nombres("Cristobal")
                .apellidos("Gomez")
                .run("12345678-9")
                .correo("cristobal@gmail.com")
                .fechaNac(LocalDate.of(2000, 1, 1))
                .idPlan(1L)
                .build();

        when(service.add(any(ClienteRequest.class))).thenReturn(responseMock);

        // Act & Assert
        mockMvc.perform(post("/api/v3/clientes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombres").value("Cristobal"));

        verify(service, times(1)).add(any(ClienteRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void debeRetornarForbiddenAlAgregarClienteCuandoRolEsUser() throws Exception {
        // Arrange: Rellenar los campos obligatorios para engañar a la validación
        ClienteRequest request = new ClienteRequest();
        request.setNombres("Cristobal");
        request.setApellidos("Silva");
        request.setRun("12345678-9");
        request.setCorreo("cristobal@email.com");
        request.setFechaNac(LocalDate.of(1995, 5, 5));
        request.setIdPlan(1L);
        request.setPago(getPagoMock());

        // Act & Assert
        mockMvc.perform(post("/api/v3/clientes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden()); // Ahora sí llegará aquí y dará 403

        verify(service, never()).add(any(ClienteRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void debeObtenerClientePorIdCuandoRolEsUser() throws Exception {
        // Arrange
        Long idCliente = 1L;
        ClienteResponse responseMock = ClienteResponse.builder()
                .id(idCliente)
                .nombres("Vicentito")
                .build();

        when(service.findById(eq(idCliente))).thenReturn(responseMock);

        // Act & Assert
        mockMvc.perform(get("/api/v3/clientes/{id}", idCliente)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombres").value("Vicentito"));

        verify(service, times(1)).findById(eq(idCliente));
    }

    @Test
    @WithMockUser(roles = "USER")
    void debeListarTodosLosClientesCuandoRolEsUser() throws Exception {
        // Arrange
        List<ClienteResponse> listaMock = List.of(
                ClienteResponse.builder().id(1L).nombres("vicentito").build()
        );

        when(service.getAll()).thenReturn(listaMock);

        // Act & Assert
        mockMvc.perform(get("/api/v3/clientes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].nombres").value("vicentito"));

        verify(service, times(1)).getAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeActualizarClienteCuandoRolEsAdmin() throws Exception {
        // Arrange
        Long idCliente = 1L;
        ClienteRequest request = new ClienteRequest();
        request.setNombres("Cristobal Modificado");
        request.setApellidos("Gomez");
        request.setRun("12345678-9");
        request.setCorreo("cristobal.nuevo@gmail.com");
        request.setFechaNac(LocalDate.of(2000, 1, 1));
        request.setIdPlan(2L);
        request.setPago(getPagoMock());

        ClienteResponse responseMock = ClienteResponse.builder()
                .id(idCliente)
                .nombres("Cristobal Modificado")
                .apellidos("Gomez")
                .run("12345678-9")
                .correo("cristobal.nuevo@gmail.com")
                .fechaNac(LocalDate.of(2000, 1, 1))
                .idPlan(2L)
                .build();

        when(service.update(eq(idCliente), any(ClienteRequest.class))).thenReturn(responseMock);

        // Act & Assert
        mockMvc.perform(put("/api/v3/clientes/{id}", idCliente)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-valido")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombres").value("Cristobal Modificado"))
                .andExpect(jsonPath("$.data.idPlan").value(2L));

        verify(service, times(1)).update(eq(idCliente), any(ClienteRequest.class));
    }

}