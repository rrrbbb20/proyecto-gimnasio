package com.example.ms_sede.controller;

import com.example.ms_sede.dto.EncargadoResponse;
import com.example.ms_sede.dto.SedeRequest;
import com.example.ms_sede.dto.SedeResponse;
import com.example.ms_sede.service.SedeService;


//
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(SedeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SedeControllerTest {
    @Autowired
    private MockMvc mockMvc;



    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    @MockitoBean
    private SedeService service;

    @MockitoBean
    private com.example.ms_sede.security.JwtUtil jwtUtil;

    @Test
    public void debeListarSedes() throws Exception {

        EncargadoResponse encargadoResponse = EncargadoResponse.builder()
                .id(1L).nombreCompleto("Juan Diaz").build();

        SedeResponse sede =
                SedeResponse.builder()
                        .id(1L)
                        .nombre("Antonio Varas")
                        .direccion("E.Yanez").horaApertura(800).horaCierre(2100)
                        .infoEncargado(encargadoResponse)
                        .build();


        when(service.listar(anyString()))
                .thenReturn(List.of(sede));

        mockMvc.perform(
                        get("/api/v1/sede")
                                .header("Authorization", "Bearer token-prueba")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Listado de sedes"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].nombre").value("Antonio Varas"))
                .andExpect(jsonPath("$.data[0].direccion").value("E.Yanez"))
                .andExpect(jsonPath("$.data[0].infoEncargado.nombreCompleto")
                        .value("Juan Diaz"));
    }

    @Test
    public void debeBuscarSedePorId() throws Exception {

        EncargadoResponse encargado =
                EncargadoResponse.builder()
                        .id(1L)
                        .nombreCompleto("Juan Diaz")
                        .build();

        SedeResponse sede =
                SedeResponse.builder()
                        .id(1L)
                        .nombre("Antonio Varas")
                        .direccion("E.Yanez")
                        .horaApertura(800)
                        .horaCierre(2100)
                        .infoEncargado(encargado)
                        .build();

        when(service.obtenerSede(eq(1L), anyString()))
                .thenReturn(sede);

        mockMvc.perform(
                        get("/api/v1/sede/1")
                                .header("Authorization", "Bearer token-prueba")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Sede encontrada"))
                .andExpect(jsonPath("$.data.id")
                        .value(1))
                .andExpect(jsonPath("$.data.nombre")
                        .value("Antonio Varas"))
                .andExpect(jsonPath("$.data.direccion")
                        .value("E.Yanez"))
                .andExpect(jsonPath("$.data.horaApertura")
                        .value(800))
                .andExpect(jsonPath("$.data.horaCierre")
                        .value(2100))
                .andExpect(jsonPath("$.data.infoEncargado.nombreCompleto")
                        .value("Juan Diaz"));
    }

    @Test
    public void debeCrearSede() throws Exception {

        SedeRequest request = new SedeRequest();
        request.setNombre("Antonio Varas");
        request.setDireccion("E.Yanez");
        request.setHoraApertura(800);
        request.setHoraCierre(2100);
        request.setIdEncargado(1L);

        EncargadoResponse encargado =
                EncargadoResponse.builder()
                        .id(1L)
                        .nombreCompleto("Juan Diaz")
                        .build();

        SedeResponse response =
                SedeResponse.builder()
                        .id(1L)
                        .nombre("Antonio Varas")
                        .direccion("E.Yanez")
                        .horaApertura(800)
                        .horaCierre(2100)
                        .infoEncargado(encargado)
                        .build();

        when(service.add(any(SedeRequest.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/sede")
                                .header("Authorization", "Bearer token-prueba")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Sede creada"))
                .andExpect(jsonPath("$.data.nombre")
                        .value("Antonio Varas"))
                .andExpect(jsonPath("$.data.direccion")
                        .value("E.Yanez"));
    }

    @Test
    public void debeActualizarSede() throws Exception {

        SedeRequest request = new SedeRequest();
        request.setNombre("Antonio Varas Actualizada");
        request.setDireccion("Nueva Direccion");
        request.setHoraApertura(900);
        request.setHoraCierre(2200);
        request.setIdEncargado(1L);

        EncargadoResponse encargado =
                EncargadoResponse.builder()
                        .id(1L)
                        .nombreCompleto("Juan Diaz")
                        .build();

        SedeResponse actualizado =
                SedeResponse.builder()
                        .id(1L)
                        .nombre("Antonio Varas Actualizada")
                        .direccion("Nueva Direccion")
                        .horaApertura(900)
                        .horaCierre(2200)
                        .infoEncargado(encargado)
                        .build();

        when(service.actualizarSede(
                eq(1L),
                any(SedeRequest.class),
                anyString()))
                .thenReturn(actualizado);

        mockMvc.perform(
                        put("/api/v1/sede/1")
                                .header("Authorization", "Bearer token-prueba")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Sede actualizada"))
                .andExpect(jsonPath("$.data.nombre")
                        .value("Antonio Varas Actualizada"))
                .andExpect(jsonPath("$.data.direccion")
                        .value("Nueva Direccion"));
    }

    @Test
    public void debeEliminarSede() throws Exception {

        doNothing()
                .when(service)
                .eliminarSede(1L);

        mockMvc.perform(
                        delete("/api/v1/sede/1")
                                .header("Authorization", "Bearer token-prueba")
                )
                .andExpect(status().isOk());
    }

    @Test
    public void notFoundSedeEliminar() throws Exception {

        doThrow(new EntityNotFoundException(
                "No se puede eliminar sede, no encontrado"))
                .when(service)
                .eliminarSede(1L);

        mockMvc.perform(
                        delete("/api/v1/sede/1")
                                .header("Authorization", "Bearer token-prueba")
                )
                .andExpect(status().isNotFound());
    }
}

