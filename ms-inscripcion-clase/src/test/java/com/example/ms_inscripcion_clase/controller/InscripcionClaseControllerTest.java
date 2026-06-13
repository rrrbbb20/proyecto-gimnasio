package com.example.ms_inscripcion_clase.controller;

import com.example.ms_inscripcion_clase.dto.ClaseResponse;
import com.example.ms_inscripcion_clase.dto.ClienteResponse;
import com.example.ms_inscripcion_clase.dto.InscripcionClaseRequest;
import com.example.ms_inscripcion_clase.dto.InscripcionClaseResponse;
import com.example.ms_inscripcion_clase.service.InscripcionClaseService;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
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
@WebMvcTest(InscripcionClaseController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InscripcionClaseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private InscripcionClaseService service;

    @MockitoBean
    private com.example.ms_inscripcion_clase.security.JwtUtil jwtUtil;

    @Test
    public void debeListarInscripciones() throws Exception {

        ClaseResponse clase = new ClaseResponse();
        clase.setId(1L);

        ClienteResponse cliente = new ClienteResponse();
        cliente.setId(2L);

        InscripcionClaseResponse response =
                InscripcionClaseResponse.builder()
                        .idInscripcion(1L)
                        .clase(clase)
                        .cliente(cliente)
                        .fechaInscripcion(LocalDate.now())
                        .horaInscripcion(LocalTime.now())
                        .build();

        when(service.getAll("1111"))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/v1/inscripcion-clases")
                                .header("Authorization","1111")
                )

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success")
                        .value(true))

                .andExpect(jsonPath("$.message")
                        .value("Listado de inscripciones"))

                .andExpect(jsonPath("$.data[0].idInscripcion")
                        .value(1));
    }

    @Test
    public void debeBuscarInscripcionPorId() throws Exception {

        ClaseResponse clase = new ClaseResponse();
        clase.setId(1L);

        ClienteResponse cliente = new ClienteResponse();
        cliente.setId(2L);

        InscripcionClaseResponse response =
                InscripcionClaseResponse.builder()
                        .idInscripcion(1L)
                        .clase(clase)
                        .cliente(cliente)
                        .fechaInscripcion(LocalDate.now())
                        .horaInscripcion(LocalTime.now())
                        .build();

        when(service.findById(1L,"1111"))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v1/inscripcion-clases/1")
                                .header("Authorization","1111")
                )

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success")
                        .value(true))

                .andExpect(jsonPath("$.message")
                        .value("Inscripción encontrada"))

                .andExpect(jsonPath("$.data.idInscripcion")
                        .value(1))

                .andExpect(jsonPath("$.data.clase.id")
                        .value(1))

                .andExpect(jsonPath("$.data.cliente.id")
                        .value(2));
    }

    @Test
    public void debeCrearInscripcion() throws Exception {

        InscripcionClaseRequest request =
                new InscripcionClaseRequest();

        request.setIdClase(1L);
        request.setIdCliente(2L);

        ClaseResponse clase = new ClaseResponse();
        clase.setId(1L);

        ClienteResponse cliente = new ClienteResponse();
        cliente.setId(2L);

        InscripcionClaseResponse response =
                InscripcionClaseResponse.builder()
                        .idInscripcion(1L)
                        .clase(clase)
                        .cliente(cliente)
                        .fechaInscripcion(LocalDate.now())
                        .horaInscripcion(LocalTime.now())
                        .build();

        when(service.add(
                any(InscripcionClaseRequest.class),
                eq("1111")
        )).thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/inscripcion-clases")
                                .header("Authorization","1111")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )

                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.success")
                        .value(true))

                .andExpect(jsonPath("$.message")
                        .value("Inscripción creada"))

                .andExpect(jsonPath("$.data.idInscripcion")
                        .value(1))

                .andExpect(jsonPath("$.data.clase.id")
                        .value(1))

                .andExpect(jsonPath("$.data.cliente.id")
                        .value(2));
    }

    @Test
    public void debeEliminarInscripcion() throws Exception {

        doNothing()
                .when(service)
                .delete(1L,"1111");

        mockMvc.perform(
                        delete("/api/v1/inscripcion-clases/1")
                                .header("Authorization","1111")
                )

                .andExpect(status().isNoContent());
    }
}
