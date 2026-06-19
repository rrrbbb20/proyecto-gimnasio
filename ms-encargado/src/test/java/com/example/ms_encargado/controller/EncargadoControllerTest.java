package com.example.ms_encargado.controller;


import com.example.ms_encargado.dto.EncargadoRequest;
import com.example.ms_encargado.dto.EncargadoResponse;
import com.example.ms_encargado.security.JwtUtil;
import com.example.ms_encargado.service.EncargadoService;


//
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

@WebMvcTest(EncargadoController.class)
@AutoConfigureMockMvc(addFilters = false)
class EncargadoControllerTest {


    @Autowired
    private MockMvc mockMvc;  //para simular peticiones HTTP sin levantar un servidor real.


    //json a objetos
    private final ObjectMapper objectMapper =
            new ObjectMapper()
                    .registerModule(
                            new JavaTimeModule());

    @MockitoBean
    private EncargadoService service;

    //autenticacion
    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void debeListarEncargados() throws Exception {

        //Simulamos la respuesta
        EncargadoResponse encargado =
                EncargadoResponse.builder()
                        .id(1L)
                        .nombreCompleto("Juan Diaz")
                        .run("111-1")
                        .direccion("Santiago")
                        .fechaNacimiento(
                                LocalDate.parse("1992-02-13"))
                        .build();

        when(service.getAll())
                .thenReturn(List.of(encargado));


        //Simula la peticion
        mockMvc.perform(
                        get("/api/v1/encargado")
                                .header("Authorization", "Bearer token-prueba")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.data[0].id")
                        .value(1))
                .andExpect(jsonPath("$.data[0].nombreCompleto")
                        .value("Juan Diaz"))
                .andExpect(jsonPath("$.data[0].run")
                        .value("111-1"))
                .andExpect(jsonPath("$.data[0].direccion")
                        .value("Santiago"))
                .andExpect(jsonPath("$.message")
                        .value("Listado de encargados"));

    }

    @Test
    void debeBuscarEncargadoPorId()
            throws Exception {

        EncargadoResponse encargado =
                EncargadoResponse.builder()
                        .id(1L)
                        .nombreCompleto("Juan Diaz")
                        .run("111-1")
                        .direccion("Santiago")
                        .fechaNacimiento(
                                LocalDate.parse("1992-02-13"))
                        .build();

        when(service.findById(
                eq(1L),
                anyString()
        )).thenReturn(encargado);

        mockMvc.perform(
                        get("/api/v1/encargado/1")
                                .header("Authorization", "Bearer token-prueba")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.data.id")
                        .value(1))
                .andExpect(jsonPath("$.data.nombreCompleto")
                        .value("Juan Diaz"))
                .andExpect(jsonPath("$.data.run")
                        .value("111-1"))
                .andExpect(jsonPath("$.data.direccion")
                        .value("Santiago"))
                .andExpect(jsonPath("$.message")
                        .value("Encargado encontrado"));
    }

    @Test
    void debeCrearEncargado()
            throws Exception {

        EncargadoRequest request =
                new EncargadoRequest();

        request.setNombreCompleto("Juan Diaz");
        request.setRun("111-1");
        request.setDireccion("Santiago");
        request.setFechaNacimiento(
                LocalDate.parse("1992-02-13"));

        EncargadoResponse response =
                EncargadoResponse.builder()
                        .id(1L)
                        .nombreCompleto("Juan Diaz")
                        .run("111-1")
                        .direccion("Santiago")
                        .fechaNacimiento(
                                LocalDate.parse("1992-02-13"))
                        .build();

        when(service.add(
                any(EncargadoRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/encargado")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper
                                                .writeValueAsString(
                                                        request
                                                )
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.data.nombreCompleto")
                        .value("Juan Diaz"))
                .andExpect(jsonPath("$.data.run")
                        .value("111-1"))
                .andExpect(jsonPath("$.data.direccion")
                        .value("Santiago"))
                .andExpect(jsonPath("$.message")
                        .value("Encargado creado"));
    }

    @Test
    void debeActualizarEncargado()
            throws Exception {

        EncargadoRequest request =
                new EncargadoRequest();

        request.setNombreCompleto("Pedro Diaz");
        request.setRun("222-2");
        request.setDireccion("Valparaiso");
        request.setFechaNacimiento(
                LocalDate.parse("1996-04-14"));

        EncargadoResponse actualizado =
                EncargadoResponse.builder()
                        .id(1L)
                        .nombreCompleto("Pedro Diaz")
                        .run("222-2")
                        .direccion("Valparaiso")
                        .fechaNacimiento(
                                LocalDate.parse("1996-04-14"))
                        .build();

        when(
                service.update(
                        eq(1L),
                        any(EncargadoRequest.class),
                        any()
                )
        ).thenReturn(actualizado);

        mockMvc.perform(
                        put("/api/v1/encargado/1")
                                .header("Authorization", "Bearer token-prueba")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombreCompleto")
                        .value("Pedro Diaz"))
                .andExpect(jsonPath("$.data.run")
                        .value("222-2"))
                .andExpect(jsonPath("$.data.direccion")
                        .value("Valparaiso"))
                .andExpect(jsonPath("$.message")
                        .value("Encargado actualizado"));
    }

    @Test
    void debeEliminarEncargado() throws Exception {

        doNothing()
                .when(service)
                .delete(1L);

        mockMvc.perform(
                        delete("/api/v1/encargado/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.message")
                        .value("Encargado Eliminado"));
    }
}