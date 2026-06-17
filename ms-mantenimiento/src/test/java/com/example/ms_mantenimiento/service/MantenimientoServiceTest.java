package com.example.ms_mantenimiento.service;

import com.example.ms_mantenimiento.dto.MantenimientoRequest;
import com.example.ms_mantenimiento.dto.MantenimientoResponse;
import com.example.ms_mantenimiento.model.Mantenimiento;
import com.example.ms_mantenimiento.repository.MantenimientoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MantenimientoServiceTest {

    @Mock
    private MantenimientoRepository repository;

    @InjectMocks
    private MantenimientoService service;

    @Test
    void cuandoAgregaMantenimiento() {

        MantenimientoRequest request = new MantenimientoRequest();
        request.setEmpresa("Empresa SPA");
        request.setDescripcionMantenimiento("Mantenimiento de maquina");
        request.setFechaMantenimiento(LocalDate.parse("2024-02-13"));
        request.setPrecio(15000.0);

        Mantenimiento mantenimiento = new Mantenimiento(
                1L,
                "Empresa SPA",
                "Mantenimiento de maquina",
                LocalDate.parse("2024-02-13"),
                15000.0
        );

        when(repository.save(any(Mantenimiento.class)))
                .thenReturn(mantenimiento);

        MantenimientoResponse respuesta = service.add(request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals("Empresa SPA", respuesta.getEmpresa());
        assertEquals("Mantenimiento de maquina", respuesta.getDescripcionMantenimiento());
        assertEquals(LocalDate.parse("2024-02-13"), respuesta.getFechaMantenimiento());
        assertEquals(15000.0, respuesta.getPrecio());

        verify(repository).save(any(Mantenimiento.class));
    }

    @Test
    void retornarMantenimientoCuandoExiste() {

        Mantenimiento mantenimiento = new Mantenimiento(
                1L,
                "Empresa SPA",
                "Mantenimiento de maquina",
                LocalDate.parse("2024-02-13"),
                15000.0
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(mantenimiento));

        MantenimientoResponse respuesta = service.findById(1L);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals("Empresa SPA", respuesta.getEmpresa());

        verify(repository).findById(1L);
    }

    @Test
    void cuandoMantenimientoNoExiste() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        EntityNotFoundException excepcion = assertThrows(
                EntityNotFoundException.class,
                () -> service.findById(1L));

        assertEquals("Mantenimiento no encontrado", excepcion.getMessage());

        verify(repository).findById(1L);
    }

    @Test
    void puedoActualizarMantenimiento() {

        Mantenimiento mantenimiento = new Mantenimiento(
                1L,
                "Empresa SPA",
                "Mantenimiento de maquina",
                LocalDate.parse("2024-02-13"),
                15000.0
        );

        MantenimientoRequest request = new MantenimientoRequest();
        request.setEmpresa("Empresa Nueva");
        request.setDescripcionMantenimiento("Cambio de pieza");
        request.setFechaMantenimiento(LocalDate.parse("2025-04-14"));
        request.setPrecio(20000.0);

        when(repository.findById(1L))
                .thenReturn(Optional.of(mantenimiento));

        when(repository.save(any(Mantenimiento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MantenimientoResponse resultado = service.update(1L, request);

        assertNotNull(resultado);
        assertEquals("Empresa Nueva", resultado.getEmpresa());
        assertEquals("Cambio de pieza", resultado.getDescripcionMantenimiento());
        assertEquals(LocalDate.parse("2025-04-14"), resultado.getFechaMantenimiento());
        assertEquals(20000.0, resultado.getPrecio());

        verify(repository).findById(1L);
        verify(repository).save(any(Mantenimiento.class));
    }

    @Test
    void noPuedoActualizarSiNoExiste() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        EntityNotFoundException excepcion = assertThrows(
                EntityNotFoundException.class,
                () -> service.update(1L, new MantenimientoRequest()));

        assertEquals("Mantenimiento no encontrado", excepcion.getMessage());

        verify(repository).findById(1L);
    }

    @Test
    void debeListarMantenimientos() {

        Mantenimiento mantenimiento = new Mantenimiento(
                1L,
                "Empresa SPA",
                "Mantenimiento de maquina",
                LocalDate.parse("2024-02-13"),
                15000.0
        );

        when(repository.findAll())
                .thenReturn(List.of(mantenimiento));

        List<MantenimientoResponse> resultado = service.getAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Empresa SPA", resultado.get(0).getEmpresa());

        verify(repository).findAll();
    }

    @Test
    void debeBorrar() {

        when(repository.existsById(1L))
                .thenReturn(true);

        doNothing()
                .when(repository)
                .deleteById(1L);

        service.delete(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void noPuedoBorrar() {

        when(repository.existsById(1L))
                .thenReturn(false);

        EntityNotFoundException excepcion = assertThrows(
                EntityNotFoundException.class,
                () -> service.delete(1L));

        assertEquals(
                "No se puede eliminar mantenimiento no encontrado",
                excepcion.getMessage());

        verify(repository).existsById(1L);
    }
}
