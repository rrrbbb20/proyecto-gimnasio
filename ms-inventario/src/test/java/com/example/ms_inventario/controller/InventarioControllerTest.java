package com.example.ms_inventario.controller;

import com.example.ms_inventario.dto.InventarioRequest;
import com.example.ms_inventario.dto.InventarioResponse;
import com.example.ms_inventario.dto.MantenimientoResponse;
import com.example.ms_inventario.service.InventarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventarioController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @MockitoBean
    private InventarioService service;

    @MockitoBean
    private com.example.ms_inventario.security.JwtUtil jwtUtil;

    // ─── Helper: MantenimientoResponse de prueba ─────────────────────────────
    private MantenimientoResponse mantenimientoMock() {
        MantenimientoResponse m = new MantenimientoResponse();
        m.setId(10L);
        return m;
    }

    // ─── Helper: InventarioResponse de prueba ────────────────────────────────
    private InventarioResponse responseMock() {
        return InventarioResponse.builder()
                .id(1L)
                .nombre("Pelota")
                .descripcion("Pelota de fútbol")
                .precio(29.99)
                .fechaRegistro(LocalDate.parse("2024-01-15"))
                .infoMantenimiento(mantenimientoMock())
                .build();
    }

    // ─── Helper: InventarioRequest de prueba ─────────────────────────────────
    private InventarioRequest requestMock() {
        InventarioRequest req = new InventarioRequest();
        req.setNombre("Pelota");
        req.setDescripcion("Pelota de fútbol");
        req.setPrecio(29.99);
        req.setFechaRegistro(LocalDate.parse("2024-01-15"));
        req.setIdMantenimiento(10L);
        return req;
    }

    // ════════════════════════════════════════════════════════════════
    // GET /api/v2/inventario
    // ════════════════════════════════════════════════════════════════

    @Test
    public void debeListarInventarios() throws Exception {

        when(service.getAll(anyString()))
                .thenReturn(List.of(responseMock()));

        mockMvc.perform(
                        get("/api/v2/inventario")
                                .header("Authorization", "Bearer test-token")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Listado de clases"))
                .andExpect(jsonPath("$.data[0].id")
                        .value(1))
                .andExpect(jsonPath("$.data[0].nombre")
                        .value("Pelota"))
                .andExpect(jsonPath("$.data[0].precio")
                        .value(29.99));
    }

    // ════════════════════════════════════════════════════════════════
    // GET /api/v2/inventario/{id}
    // ════════════════════════════════════════════════════════════════

    @Test
    public void debeBuscarInventarioPorId() throws Exception {

        when(service.findById(1L, "Bearer test-token"))
                .thenReturn(responseMock());

        mockMvc.perform(
                        get("/api/v2/inventario/1")
                                .header("Authorization", "Bearer test-token")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Equipamiento encontrado"))
                .andExpect(jsonPath("$.data.id")
                        .value(1))
                .andExpect(jsonPath("$.data.nombre")
                        .value("Pelota"));
    }

    // ════════════════════════════════════════════════════════════════
    // POST /api/v2/inventario
    // ════════════════════════════════════════════════════════════════

    @Test
    public void debeCrearInventario() throws Exception {

        when(service.add(
                any(InventarioRequest.class),
                anyString()))
                .thenReturn(responseMock());

        mockMvc.perform(
                        post("/api/v2/inventario")
                                .header("Authorization", "Bearer test-token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writeValueAsString(requestMock())
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Equipamiento agregado"))
                .andExpect(jsonPath("$.data.nombre")
                        .value("Pelota"))
                .andExpect(jsonPath("$.data.descripcion")
                        .value("Pelota de fútbol"));
    }

    // ════════════════════════════════════════════════════════════════
    // PUT /api/v2/inventario/{id}
    // ════════════════════════════════════════════════════════════════

    @Test
    public void debeActualizarInventario() throws Exception {

        InventarioRequest req = new InventarioRequest();
        req.setNombre("Conos");
        req.setDescripcion("Conos de entrenamiento");
        req.setPrecio(15.00);
        req.setFechaRegistro(LocalDate.parse("2024-03-20"));
        req.setIdMantenimiento(10L);

        InventarioResponse respuesta =
                InventarioResponse.builder()
                        .id(1L)
                        .nombre("Conos")
                        .descripcion("Conos de entrenamiento")
                        .precio(15.00)
                        .fechaRegistro(LocalDate.parse("2024-03-20"))
                        .infoMantenimiento(mantenimientoMock())
                        .build();

        when(service.update(
                eq(1L),
                any(InventarioRequest.class),
                anyString()))
                .thenReturn(respuesta);

        mockMvc.perform(
                        put("/api/v2/inventario/1")
                                .header("Authorization", "Bearer test-token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writeValueAsString(req)
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Inventario actualizado"))
                .andExpect(jsonPath("$.data.nombre")
                        .value("Conos"))
                .andExpect(jsonPath("$.data.precio")
                        .value(15.00));
    }

    // ════════════════════════════════════════════════════════════════
    // DELETE /api/v2/inventario/{id}
    // ════════════════════════════════════════════════════════════════

    @Test
    public void debeEliminarInventario() throws Exception {

        doNothing()
                .when(service)
                .delete(1L);

        mockMvc.perform(
                        delete("/api/v2/inventario/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Objeto del Inventario de id = 1 Eliminado"));
    }
}