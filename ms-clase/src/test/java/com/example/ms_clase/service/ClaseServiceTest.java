package com.example.ms_clase.service;


import com.example.ms_clase.client.EntrenadorClient;
import com.example.ms_clase.dto.ClaseRequest;
import com.example.ms_clase.dto.ClaseResponse;
import com.example.ms_clase.dto.EntrenadorResponse;
import com.example.ms_clase.model.Clase;
import com.example.ms_clase.repository.ClaseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClaseServiceTest {


    @Mock
    private  ClaseRepository repository;
    @Mock
    private EntrenadorClient entrenadorClient;
    @InjectMocks
    private ClaseService service;
    @Test
    public void deberiaAgregarExisteEntrenador(){
        ClaseRequest cRequest = new ClaseRequest();
        cRequest.setNombreClase("tenis");
        cRequest.setDescripcion("para todos");
        cRequest.setNivelDeClase("inicial");
        cRequest.setFechaRealizacion(LocalDate.parse("2026-08-16"));
        cRequest.setHoraRealizacion(LocalTime.parse("16:00:00"));
        cRequest.setCupos(10);
        cRequest.setEstado(true);
        cRequest.setIdEntrenador(2L);
        Clase clase = new Clase(1L,"tenis","para todos",
                "inicial", LocalDate.parse("2026-08-16"),
                LocalTime.parse("16:00:00"),
                10,true,2L);
        when(repository.save(any(Clase.class))).thenReturn(clase);
        EntrenadorResponse e1 = new EntrenadorResponse();
        e1.setId(2L);
        e1.setRun("111-1");
        e1.setFechaNacimiento(LocalDate.parse("1992-05-12"));
        when(entrenadorClient.getEntrenador(2L,"1111")).thenReturn(e1);
        ClaseResponse respuesta = service.add(cRequest,"1111");
        assertNotNull(respuesta);
        assertEquals(1L,respuesta.getId());
        assertEquals("tenis",respuesta.getNombreClase());
        assertEquals("para todos",respuesta.getDescripcion());
        assertEquals("inicial",respuesta.getNivelDeClase());
        assertEquals(LocalDate.parse("2026-08-16"),respuesta.getFechaRealizacion());
        assertEquals(LocalTime.parse("16:00:00"),respuesta.getHoraRealizacion());
        assertEquals(10,respuesta.getCupos());
        assertEquals(true,respuesta.getEstado());
        assertEquals(2L,respuesta.getEntrenador().getId());
        verify(repository).save(any(Clase.class));
        verify(entrenadorClient, times(2))
                .getEntrenador(2L,"1111");
    }
    @Test
    public void cuandoNoExisteEntrenadorAlAgregar(){
        ClaseRequest cRequest = new ClaseRequest();
        cRequest.setIdEntrenador(2L);
        when(entrenadorClient.getEntrenador(2L,"1111"))
                .thenReturn(null);
        EntityNotFoundException excepcion = assertThrows(
                EntityNotFoundException.class,
                () -> service.add(cRequest,"1111"));
        assertEquals("Entrenador no encontrado",
                excepcion.getMessage());
        verify(entrenadorClient)
                .getEntrenador(2L,"1111");
    }

    @Test
    public void debeListarClases(){

        Clase clase = new Clase(
                1L,
                "tenis",
                "para todos",
                "inicial",
                LocalDate.parse("2026-08-16"),
                LocalTime.parse("16:00:00"),
                10,
                true,
                1L
        );

        EntrenadorResponse entrenador = new EntrenadorResponse();
        entrenador.setId(1L);

        when(repository.findAll())
                .thenReturn(List.of(clase));

        when(entrenadorClient.getEntrenador(1L,"1111"))
                .thenReturn(entrenador);

        List<ClaseResponse> resultado =
                service.getAll("1111");

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("tenis",
                resultado.get(0).getNombreClase());

        verify(repository).findAll();
        verify(entrenadorClient)
                .getEntrenador(1L,"1111");
    }
    @Test
    public void puedoActualizarClase(){

        Clase clase = new Clase(1L, "tenis", "para todos",
                "inicial", LocalDate.parse("2026-08-16"),
                LocalTime.parse("16:00:00"), 10, true,
                1L
        );
        ClaseRequest request = new ClaseRequest();
        request.setNombreClase("futbol");
        request.setDescripcion("juvenil");
        request.setNivelDeClase("avanzado");
        request.setFechaRealizacion(LocalDate.parse("2026-09-20"));
        request.setHoraRealizacion(LocalTime.parse("18:00:00"));
        request.setCupos(20);
        request.setEstado(true);
        request.setIdEntrenador(2L);
        EntrenadorResponse entrenador = new EntrenadorResponse();
        entrenador.setId(2L);
        when(repository.findById(1L))
                .thenReturn(Optional.of(clase));
        when(entrenadorClient.getEntrenador(2L,"1111"))
                .thenReturn(entrenador);
        when(repository.save(any(Clase.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        ClaseResponse resultado = service.update(1L,request,"1111");
        assertNotNull(resultado);
        assertEquals("futbol",
                resultado.getNombreClase());
        assertEquals("juvenil",
                resultado.getDescripcion());
        assertEquals(20,
                resultado.getCupos());
        verify(repository).findById(1L);
        verify(repository).save(any(Clase.class));
    }
    @Test
    public void noPuedoActualizarClaseInexistente(){
        ClaseRequest request = new ClaseRequest();
        when(repository.findById(1L))
                .thenReturn(Optional.empty());
        EntityNotFoundException excepcion = assertThrows(
                EntityNotFoundException.class,
                () -> service.update(1L,request,"1111"));
        assertEquals("Clase no encontrada",
                excepcion.getMessage());
        verify(repository).findById(1L);
    }
    @Test
    public void noPuedoActualizarPorEntrenadorInexistente(){
        Clase clase = new Clase();
        clase.setId(1L);
        ClaseRequest request = new ClaseRequest();
        request.setIdEntrenador(2L);
        when(repository.findById(1L))
                .thenReturn(Optional.of(clase));
        when(entrenadorClient.getEntrenador(2L,"1111"))
                .thenReturn(null);
        EntityNotFoundException excepcion = assertThrows(
                EntityNotFoundException.class,
                () -> service.update(1L,request,"1111"));
        assertEquals("Entrenador no encontrado",
                excepcion.getMessage());
        verify(repository).findById(1L);
        verify(entrenadorClient)
                .getEntrenador(2L,"1111");
    }
    @Test
    public void debeBorrarClase(){
        when(repository.existsById(1L))
                .thenReturn(true);
        doNothing().when(repository)
                .deleteById(1L);
        service.delete(1L);
        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }
    @Test
    public void noPuedoBorrarClase(){
        when(repository.existsById(1L))
                .thenReturn(false);
        EntityNotFoundException excepcion = assertThrows(
                EntityNotFoundException.class,
                () -> service.delete(1L));
        assertEquals(
                "No se puede eliminar clase no encontrada",
                excepcion.getMessage());
        verify(repository).existsById(1L);
    }
    @Test
    public void puedeRestarCupo(){
        when(repository.restarCupo(1L))
                .thenReturn(1);
        service.personaInscrita(1L);
        verify(repository).restarCupo(1L);
    }
    @Test
    public void noPuedeRestarCupo(){
        when(repository.restarCupo(1L))
                .thenReturn(0);
        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> service.personaInscrita(1L));
        assertEquals(
                "No se pudo restar el cupo",
                excepcion.getMessage());
        verify(repository).restarCupo(1L);
    }
    @Test
    public void puedeSumarCupo(){
        when(repository.sumarCupo(1L))
                .thenReturn(1);
        service.removerInscripcion(1L);
        verify(repository).sumarCupo(1L);
    }
    @Test
    public void noPuedeSumarCupo(){
        when(repository.sumarCupo(1L))
                .thenReturn(0);
        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> service.removerInscripcion(1L));
        assertEquals(
                "No se pudo sumar el cupo",
                excepcion.getMessage());
        verify(repository).sumarCupo(1L);
    }
    @Test
    public void debeBuscarPorNombre(){
        Clase clase = new Clase(1L,"tenis", "para todos",
                "inicial", LocalDate.parse("2026-08-16"),
                LocalTime.parse("16:00:00"), 10, true,
                1L
        );
        EntrenadorResponse entrenador = new EntrenadorResponse();
        entrenador.setId(1L);
        when(repository.findByNombreClase("tenis"))
                .thenReturn(List.of(clase));
        when(entrenadorClient.getEntrenador(1L,"1111"))
                .thenReturn(entrenador);
        List<ClaseResponse> resultado =
                service.buscarPorNombre("tenis","1111");
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("tenis",
                resultado.get(0).getNombreClase());
        verify(repository)
                .findByNombreClase("tenis");
    }



}
