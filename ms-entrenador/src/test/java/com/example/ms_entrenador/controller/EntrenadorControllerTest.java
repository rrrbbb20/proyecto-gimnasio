package com.example.ms_entrenador.controller;

import com.example.ms_entrenador.dto.EntrenadorRequest;
import com.example.ms_entrenador.dto.EntrenadorResponse;
import com.example.ms_entrenador.service.EntrenadorService;


//
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(EntrenadorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EntrenadorControllerTest {
    @Autowired
    private MockMvc mockMvc;


    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    @MockitoBean
    private EntrenadorService service;

    @MockitoBean
    private com.example.ms_entrenador.security.JwtUtil jwtUtil;

    @Test
    public void debeListarEntrenadores() throws Exception {

        EntrenadorResponse entrenador =
                EntrenadorResponse.builder()
                        .id(1L)
                        .nombreCompleto("tito")
                        .run("111-1")
                        .fechaNacimiento(
                                LocalDate.parse("1992-02-13"))
                        .build();

        when(service.getAll())
                .thenReturn(List.of(entrenador));

        mockMvc.perform(
                        get("/api/v1/entrenadores")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Listado obtenido"))
                .andExpect(jsonPath("$.data[0].id")
                        .value(1))
                .andExpect(jsonPath("$.data[0].nombreCompleto")
                        .value("tito"))
                .andExpect(jsonPath("$.data[0].run")
                        .value("111-1"));
    }

    @Test
    public void debeBuscarEntrenadorPorId() throws Exception {

        EntrenadorResponse entrenador =
                EntrenadorResponse.builder()
                        .id(1L)
                        .nombreCompleto("tito")
                        .run("111-1")
                        .fechaNacimiento(
                                LocalDate.parse("1992-02-13"))
                        .build();

        when(service.findById(1L))
                .thenReturn(entrenador);

        mockMvc.perform(
                        get("/api/v1/entrenadores/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Encontrado"))
                .andExpect(jsonPath("$.data.id")
                        .value(1))
                .andExpect(jsonPath("$.data.nombreCompleto")
                        .value("tito"));
    }

    @Test
    public void debeCrearEntrenador() throws Exception {

        EntrenadorRequest request =
                new EntrenadorRequest();

        request.setNombreCompleto("tito");
        request.setRun("111-1");
        request.setFechaNacimiento(
                LocalDate.parse("1992-02-13"));

        EntrenadorResponse response =
                EntrenadorResponse.builder()
                        .id(1L)
                        .nombreCompleto("tito")
                        .run("111-1")
                        .fechaNacimiento(
                                LocalDate.parse("1992-02-13"))
                        .build();

        when(service.add(any(EntrenadorRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/entrenadores")
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
                        .value("Entrenador creado"))
                .andExpect(jsonPath("$.data.nombreCompleto")
                        .value("tito"))
                .andExpect(jsonPath("$.data.run")
                        .value("111-1"));
    }

    @Test
    public void debeActualizarEntrenador()
            throws Exception {

        EntrenadorRequest request =
                new EntrenadorRequest();

        request.setNombreCompleto("paco");
        request.setRun("222-2");
        request.setFechaNacimiento(
                LocalDate.parse("1996-04-14"));

        EntrenadorResponse actualizado =
                EntrenadorResponse.builder()
                        .id(1L)
                        .nombreCompleto("paco")
                        .run("222-2")
                        .fechaNacimiento(
                                LocalDate.parse("1996-04-14"))
                        .build();

        when(
                service.update(
                        eq(1L),
                        any(EntrenadorRequest.class)
                )
        ).thenReturn(actualizado);

        mockMvc.perform(
                        put("/api/v1/entrenadores/1")
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
                        .value("Entrenador actualizado"))
                .andExpect(jsonPath("$.data.nombreCompleto")
                        .value("paco"))
                .andExpect(jsonPath("$.data.run")
                        .value("222-2"));
    }

    @Test
    public void debeEliminarEntrenador()
            throws Exception {

        doNothing()
                .when(service)
                .delete(1L);

        mockMvc.perform(
                        delete("/api/v1/entrenadores/1")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void debeBuscarPorRun()
            throws Exception {

        EntrenadorResponse response =
                EntrenadorResponse.builder()
                        .id(1L)
                        .nombreCompleto("tito")
                        .run("111-1")
                        .fechaNacimiento(
                                LocalDate.parse("1992-02-13"))
                        .build();

        when(service.buscarPorRun("111-1"))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v1/entrenadores/buscar-por-run/111-1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Entrenador encontrado"))
                .andExpect(jsonPath("$.data.id")
                        .value(1))
                .andExpect(jsonPath("$.data.run")
                        .value("111-1"));
    }

}
