package com.proyectogimnasio.rutina.service;

import com.proyectogimnasio.rutina.dto.*;
import com.proyectogimnasio.rutina.model.Ejercicio;
import com.proyectogimnasio.rutina.model.Rutina;
import com.proyectogimnasio.rutina.repository.EjercicioRepository;
import com.proyectogimnasio.rutina.repository.RutinaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RutinaServiceTest {

    @Mock
    private EjercicioRepository ejercicioRepository;

    @Mock
    private RutinaRepository rutinaRepository;



    @InjectMocks
    private RutinaService service;



    @Test
    void debeAgregarRutinaSinDetallesExitosamente() {
        // Arrange
        RutinaRequest request = new RutinaRequest();
        request.setNombreRutina("Cardio Lunes");
        request.setDescripcionRutina("Quemas grasa rápido");
        request.setDetalles(null);

        Rutina rutinaGuardada = new Rutina(1L, "Cardio Lunes", "Quemas grasa rápido", null);
        when(rutinaRepository.save(any(Rutina.class))).thenReturn(rutinaGuardada);

        // Act
        RutinaResponse resultado = service.addRutina(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertTrue(resultado.getDetalles().isEmpty());
        verify(rutinaRepository, times(1)).save(any(Rutina.class));
    }

    @Test
    void debeAgregarRutinaConDetallesDeEjerciciosExitosamente() {
        // Arrange
        DetallesEjercicioRequest detRequest = new DetallesEjercicioRequest();
        detRequest.setEjercicioId(5L);
        detRequest.setNumeroEjercicios(4);
        detRequest.setDuracionRutina("20 min");
        detRequest.setTiempoDescanso("1 min");

        RutinaRequest request = new RutinaRequest();
        request.setNombreRutina("Hipertrofia Pierna");
        request.setDescripcionRutina("Foco en Cuádriceps");
        request.setDetalles(List.of(detRequest));

        Rutina rutinaInicial = new Rutina(1L, "Hipertrofia Pierna", "Foco en Cuádriceps", new HashSet<>());
        Ejercicio ejercicioGlobal = new Ejercicio(5L, "Prensa", "Piernas", 10, new HashSet<>());
        // Simular primera guardada (rutina base) y posterior procesado de flujos
        when(rutinaRepository.save(any(Rutina.class))).thenReturn(rutinaInicial);
        when(ejercicioRepository.findById(5L)).thenReturn(Optional.of(ejercicioGlobal));

        // Act
        RutinaResponse resultado = service.addRutina(request);

        // Assert
        assertNotNull(resultado);
        verify(rutinaRepository, times(2)).save(any(Rutina.class));
        verify(ejercicioRepository).findById(5L);
    }

    @Test
    void debeLanzarExcepcionAlAgregarRutinaSiEjercicioGlobalNoExiste() {
        // Arrange
        DetallesEjercicioRequest detRequest = new DetallesEjercicioRequest();
        detRequest.setEjercicioId(99L); // ID Inexistente

        RutinaRequest request = new RutinaRequest();
        request.setDetalles(List.of(detRequest));

        Rutina rutinaInicial = new Rutina(1L, "Test Error", "Desc", new HashSet<>());
        when(rutinaRepository.save(any(Rutina.class))).thenReturn(rutinaInicial);
        when(ejercicioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.addRutina(request));
    }

    @Test
    void debeBuscarRutinaPorIdCuandoExiste() {
        // Arrange
        Rutina rutina = new Rutina(1L, "Fuerza 5x5", "Pesado", new HashSet<>());
        when(rutinaRepository.findById(1L)).thenReturn(Optional.of(rutina));

        // Act
        RutinaResponse resultado = service.findRutina(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Fuerza 5x5", resultado.getNombreRutina());
    }

    @Test
    void debeLanzarExcepcionCuandoRutinaNoExiste() {
        // Arrange
        when(rutinaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.findRutina(1L));
    }

    @Test
    void debeListarTodasLasRutinas() {
        // Arrange
        List<Rutina> listaMock = List.of(
                new Rutina(1L, "R1", "D1", null),
                new Rutina(2L, "R2", "D2", null)
        );
        when(rutinaRepository.findAll()).thenReturn(listaMock);

        // Act
        List<RutinaResponse> resultado = service.getRutinas();

        // Assert
        assertEquals(2, resultado.size());
        verify(rutinaRepository).findAll();
    }

    @Test
    void debeActualizarRutinaLimpiaYConNuevosDetallesExitosamente() {
        // Arrange
        Rutina rutinaExistente = new Rutina();
        rutinaExistente.setId(1L);
        rutinaExistente.setNombreRutina("Rutina Vieja");
        rutinaExistente.setDescripcionRutina("Desc Vieja");
        rutinaExistente.setDetalles(new HashSet<>());

        RutinaRequest request = new RutinaRequest();
        request.setNombreRutina("Rutina Nueva");
        request.setDescripcionRutina("Desc Nueva");

        DetallesEjercicioRequest detReq = new DetallesEjercicioRequest();
        detReq.setEjercicioId(2L);
        request.setDetalles(List.of(detReq));

        Ejercicio ej = new Ejercicio();
        ej.setId(2L);
        ej.setNombreEjercicio("Flexiones");
        ej.setZonaEjercitada("Pecho");
        ej.setRepeticiones(15);

        when(rutinaRepository.findById(1L)).thenReturn(Optional.of(rutinaExistente));
        when(ejercicioRepository.findById(2L)).thenReturn(Optional.of(ej));
        when(rutinaRepository.save(any(Rutina.class))).thenReturn(rutinaExistente);

        // Act
        RutinaResponse resultado = service.updateRutina(1L, request);

        // Assert
        assertNotNull(resultado);
        assertEquals("Rutina Nueva", resultado.getNombreRutina());
        assertEquals("Desc Nueva", resultado.getDescripcionRutina());

        verify(rutinaRepository, times(1)).save(any(Rutina.class));
        verify(rutinaRepository).save(any(Rutina.class));
    }

    @Test
    void debeLanzarExcepcionAlActualizarRutinaInexistente() {
        // Arrange
        when(rutinaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.updateRutina(1L, new RutinaRequest()));
    }

    @Test
    void debeEliminarRutinaExitosamente() {
        // Arrange
        when(rutinaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(rutinaRepository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> service.deleteRutina(1L));
        verify(rutinaRepository).deleteById(1L);
    }

    @Test
    void debeLanzarExcepcionAlEliminarRutinaInexistente() {
        // Arrange
        when(rutinaRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.deleteRutina(1L));
    }

    // =========================================================================
    // TRATAMIENTO Y PRUEBAS: CATALOGO GLOBAL DE EJERCICIOS
    // =========================================================================

    @Test
    void debeAgregarEjercicioGlobalExitosamente() {
        // Arrange
        EjercicioRequest request = new EjercicioRequest();
        request.setNombreEjercicio("Dominadas");
        request.setZonaEjercitada("Espalda");
        request.setRepeticiones(8);

        Ejercicio ejercicioGuardado = new Ejercicio(10L, "Dominadas", "Espalda", 8, null);
        when(ejercicioRepository.save(any(Ejercicio.class))).thenReturn(ejercicioGuardado);

        // Act
        EjercicioResponse resultado = service.addEjercicio(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals("Dominadas", resultado.getNombreEjercicio());
    }

    @Test
    void debeBuscarEjercicioPorIdCuandoExiste() {
        // Arrange
        Ejercicio ejercicio = new Ejercicio(1L, "Fondos", "Tríceps", 12, null);
        when(ejercicioRepository.findById(1L)).thenReturn(Optional.of(ejercicio));

        // Act
        EjercicioResponse resultado = service.findEjercicio(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Fondos", resultado.getNombreEjercicio());
    }

    @Test
    void debeLanzarExcepcionCuandoEjercicioNoExiste() {
        // Arrange
        when(ejercicioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.findEjercicio(1L));
    }

    @Test
    void debeListarTodosLosEjercicios() {
        // Arrange
        List<Ejercicio> listaMock = List.of(new Ejercicio(1L, "E1", "Z1", 10, null));
        when(ejercicioRepository.findAll()).thenReturn(listaMock);

        // Act
        List<EjercicioResponse> resultado = service.getEjercicios();

        // Assert
        assertEquals(1, resultado.size());
        verify(ejercicioRepository).findAll();
    }

    @Test
    void debeActualizarEjercicioExistenteExitosamente() {
        // Arrange
        Ejercicio existente = new Ejercicio(1L, "Remo Invertido", "Espalda", 10, null);
        EjercicioRequest request = new EjercicioRequest();
        request.setNombreEjercicio("Remo con Barra");
        request.setZonaEjercitada("Espalda Media");
        request.setRepeticiones(12);

        when(ejercicioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(ejercicioRepository.save(any(Ejercicio.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        EjercicioResponse resultado = service.updateEjercicio(1L, request);

        // Assert
        assertNotNull(resultado);
        assertEquals("Remo con Barra", resultado.getNombreEjercicio());
        assertEquals(12, resultado.getRepeticiones());
    }

    @Test
    void debeLanzarExcepcionAlActualizarEjercicioInexistente() {
        // Arrange
        when(ejercicioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.updateEjercicio(1L, new EjercicioRequest()));
    }

    @Test
    void debeEliminarEjercicioExitosamente() {
        // Arrange
        when(ejercicioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ejercicioRepository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> service.deleteEjercicio(1L));
        verify(ejercicioRepository).deleteById(1L);
    }

    @Test
    void debeLanzarExcepcionAlEliminarEjercicioInexistente() {
        // Arrange
        when(ejercicioRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.deleteEjercicio(1L));
    }
}