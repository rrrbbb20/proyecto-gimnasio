package com.example.ms_mantenimiento.controller;

import com.example.ms_mantenimiento.dto.MantenimientoRequest;
import com.example.ms_mantenimiento.dto.MantenimientoResponse;
import com.example.ms_mantenimiento.security.JwtUtil;
import com.example.ms_mantenimiento.service.MantenimientoService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MantenimientoController.class)
@AutoConfigureMockMvc(addFilters = false)
class MantenimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper =
            new ObjectMapper()
                    .registerModule(
                            new JavaTimeModule());

    @MockitoBean
    private MantenimientoService service;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void debeListarMantenimientos() throws Exception {

        MantenimientoResponse mantenimiento =
                MantenimientoResponse.builder()
                        .id(1L)
                        .empresa("Empresa SPA")
                        .descripcionMantenimiento("Mantenimiento de maquina")
                        .fechaMantenimiento(LocalDate.parse("2024-02-13"))
                        .precio(15000.0)
                        .build();

        when(service.getAll())
                .thenReturn(List.of(mantenimiento));

        mockMvc.perform(
                        get("/api/v1/mantenimiento")
                                .header("Authorization", "Bearer token-prueba")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.data[0].id")
                        .value(1))
                .andExpect(jsonPath("$.data[0].empresa")
                        .value("Empresa SPA"))
                .andExpect(jsonPath("$.data[0].descripcionMantenimiento")
                        .value("Mantenimiento de maquina"))
                .andExpect(jsonPath("$.data[0].precio")
                        .value(15000.0))
                .andExpect(jsonPath("$.message")
                        .value("Listado de mantenimientos"));
    }

    @Test
    void debeBuscarMantenimientoPorId() throws Exception {

        MantenimientoResponse mantenimiento =
                MantenimientoResponse.builder()
                        .id(1L)
                        .empresa("Empresa SPA")
                        .descripcionMantenimiento("Mantenimiento de maquina")
                        .fechaMantenimiento(LocalDate.parse("2024-02-13"))
                        .precio(15000.0)
                        .build();

        when(service.findById(eq(1L)))
                .thenReturn(mantenimiento);

        mockMvc.perform(
                        get("/api/v1/mantenimiento/1")
                                .header("Authorization", "Bearer token-prueba")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.data.id")
                        .value(1))
                .andExpect(jsonPath("$.data.empresa")
                        .value("Empresa SPA"))
                .andExpect(jsonPath("$.data.descripcionMantenimiento")
                        .value("Mantenimiento de maquina"))
                .andExpect(jsonPath("$.message")
                        .value("Mantenimiento encontrado"));
    }

    @Test
    void debeCrearMantenimiento() throws Exception {

        MantenimientoRequest request = new MantenimientoRequest();
        request.setEmpresa("Empresa SPA");
        request.setDescripcionMantenimiento("Mantenimiento de maquina");
        request.setFechaMantenimiento(LocalDate.parse("2024-02-13"));
        request.setPrecio(15000.0);

        MantenimientoResponse response =
                MantenimientoResponse.builder()
                        .id(1L)
                        .empresa("Empresa SPA")
                        .descripcionMantenimiento("Mantenimiento de maquina")
                        .fechaMantenimiento(LocalDate.parse("2024-02-13"))
                        .precio(15000.0)
                        .build();

        when(service.add(any(MantenimientoRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/mantenimiento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.data.empresa")
                        .value("Empresa SPA"))
                .andExpect(jsonPath("$.data.descripcionMantenimiento")
                        .value("Mantenimiento de maquina"))
                .andExpect(jsonPath("$.message")
                        .value("Mantenimiento creado"));
    }

    @Test
    void debeActualizarMantenimiento() throws Exception {

        MantenimientoRequest request = new MantenimientoRequest();
        request.setEmpresa("Empresa Nueva");
        request.setDescripcionMantenimiento("Cambio de pieza");
        request.setFechaMantenimiento(LocalDate.parse("2025-04-14"));
        request.setPrecio(20000.0);

        MantenimientoResponse actualizado =
                MantenimientoResponse.builder()
                        .id(1L)
                        .empresa("Empresa Nueva")
                        .descripcionMantenimiento("Cambio de pieza")
                        .fechaMantenimiento(LocalDate.parse("2025-04-14"))
                        .precio(20000.0)
                        .build();

        when(service.update(eq(1L), any(MantenimientoRequest.class)))
                .thenReturn(actualizado);

        mockMvc.perform(
                        put("/api/v1/mantenimiento/1")
                                .header("Authorization", "Bearer token-prueba")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.empresa")
                        .value("Empresa Nueva"))
                .andExpect(jsonPath("$.data.descripcionMantenimiento")
                        .value("Cambio de pieza"))
                .andExpect(jsonPath("$.message")
                        .value("Mantenimiento actualizado"));
    }

    @Test
    void debeEliminarMantenimiento() throws Exception {

        doNothing()
                .when(service)
                .delete(1L);

        mockMvc.perform(
                        delete("/api/v1/mantenimiento/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Mantenimiento eliminado"));
    }
}
