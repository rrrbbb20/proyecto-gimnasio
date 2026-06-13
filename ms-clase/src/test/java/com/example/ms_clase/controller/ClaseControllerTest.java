package com.example.ms_clase.controller;

import com.example.ms_clase.dto.ClaseRequest;
import com.example.ms_clase.dto.ClaseResponse;
import com.example.ms_clase.dto.EntrenadorResponse;
import com.example.ms_clase.service.ClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
//
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(ClaseController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ClaseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private ClaseService service;

    @MockitoBean
    private com.example.ms_clase.security.JwtUtil jwtUtil;


    @Test
    void debeListarClases() throws Exception {

        EntrenadorResponse entrenador =
                new EntrenadorResponse();

        entrenador.setId(1L);

        ClaseResponse clase =
                ClaseResponse.builder()
                        .id(1L)
                        .nombreClase("tenis")
                        .descripcion("para todos")
                        .nivelDeClase("inicial")
                        .fechaRealizacion(LocalDate.parse("2026-08-16"))
                        .horaRealizacion(LocalTime.parse("16:00:00"))
                        .cupos(10)
                        .estado(true)
                        .entrenador(entrenador)
                        .build();

        when(service.getAll(anyString()))
                .thenReturn(List.of(clase));

        mockMvc.perform(
                        get("/api/v1/clases")
                                .header("Authorization", "1111")
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Listado de clases"))
                .andExpect(jsonPath("$.data[0].id")
                        .value(1))
                .andExpect(jsonPath("$.data[0].nombreClase")
                        .value("tenis"));
    }


    @Test
    void debeBuscarClasePorId() throws Exception {

        EntrenadorResponse entrenador =
                new EntrenadorResponse();

        entrenador.setId(1L);

        ClaseResponse clase =
                ClaseResponse.builder()
                        .id(1L)
                        .nombreClase("tenis")
                        .descripcion("para todos")
                        .nivelDeClase("inicial")
                        .fechaRealizacion(LocalDate.parse("2026-08-16"))
                        .horaRealizacion(LocalTime.parse("16:00:00"))
                        .cupos(10)
                        .estado(true)
                        .entrenador(entrenador)
                        .build();

        when(service.findById(1L, "1111"))
                .thenReturn(clase);

        mockMvc.perform(
                        get("/api/v1/clases/1")
                                .header("Authorization", "1111")
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Clase encontrada"))
                .andExpect(jsonPath("$.data.id")
                        .value(1))
                .andExpect(jsonPath("$.data.nombreClase")
                        .value("tenis"));
    }


    @Test
    void debeCrearClase() throws Exception {

        ClaseRequest request =
                new ClaseRequest();

        request.setNombreClase("tenis");
        request.setDescripcion("para todos");
        request.setNivelDeClase("inicial");
        request.setFechaRealizacion(
                LocalDate.parse("2026-08-16"));
        request.setHoraRealizacion(
                LocalTime.parse("16:00:00"));
        request.setCupos(10);
        request.setEstado(true);
        request.setIdEntrenador(1L);

        EntrenadorResponse entrenador =
                new EntrenadorResponse();

        entrenador.setId(1L);

        ClaseResponse respuesta =
                ClaseResponse.builder()
                        .id(1L)
                        .nombreClase("tenis")
                        .descripcion("para todos")
                        .nivelDeClase("inicial")
                        .fechaRealizacion(
                                LocalDate.parse("2026-08-16"))
                        .horaRealizacion(
                                LocalTime.parse("16:00:00"))
                        .cupos(10)
                        .estado(true)
                        .entrenador(entrenador)
                        .build();

        when(service.add(any(), anyString()))
                .thenReturn(respuesta);

        mockMvc.perform(
                        post("/api/v1/clases")
                                .header("Authorization",
                                        "1111")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writeValueAsString(request)
                                )
                )

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Clase creada"))
                .andExpect(jsonPath("$.data.nombreClase")
                        .value("tenis"));
    }


    @Test
    void debeActualizarClase() throws Exception {

        ClaseRequest request =
                new ClaseRequest();

        request.setNombreClase("futbol");
        request.setDescripcion("para todos");
        request.setNivelDeClase("inicial");
        request.setFechaRealizacion(LocalDate.parse("2026-08-16"));
        request.setHoraRealizacion(LocalTime.parse("16:00:00"));
        request.setCupos(10);
        request.setEstado(true);
        request.setIdEntrenador(1L);

        ClaseResponse respuesta =
                ClaseResponse.builder()
                        .id(1L)
                        .nombreClase("futbol")
                        .build();

        when(service.update(
                eq(1L),
                any(),
                anyString()))
                .thenReturn(respuesta);

        mockMvc.perform(
                        put("/api/v1/clases/1")
                                .header("Authorization",
                                        "1111")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writeValueAsString(request)
                                )
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Clase actualizada"))
                .andExpect(jsonPath("$.data.nombreClase")
                        .value("futbol"));
    }


    @Test
    void debeEliminarClase() throws Exception {

        doNothing()
                .when(service)
                .delete(1L);

        mockMvc.perform(
                        delete("/api/v1/clases/1")
                )

                .andExpect(status().isNoContent());
    }


    @Test
    void debeRestarCupo() throws Exception {

        doNothing()
                .when(service)
                .personaInscrita(1L);

        mockMvc.perform(
                        patch("/api/v1/clases/restar-cupo/1")
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Se resta cupo"));
    }


    @Test
    void debeSumarCupo() throws Exception {

        doNothing()
                .when(service)
                .removerInscripcion(1L);

        mockMvc.perform(
                        patch("/api/v1/clases/sumar-cupo/1")
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Se suma cupo"));
    }


    @Test
    void debeBuscarPorNombre() throws Exception {

        ClaseResponse clase =
                ClaseResponse.builder()
                        .id(1L)
                        .nombreClase("tenis")
                        .build();

        when(service.buscarPorNombre(
                "tenis",
                "1111"))
                .thenReturn(List.of(clase));

        mockMvc.perform(
                        get("/api/v1/clases/buscar-por-nombre/tenis")
                                .header(
                                        "Authorization",
                                        "1111")
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Búsqueda por nombre"))
                .andExpect(jsonPath("$.data[0].nombreClase")
                        .value("tenis"));
    }

}
