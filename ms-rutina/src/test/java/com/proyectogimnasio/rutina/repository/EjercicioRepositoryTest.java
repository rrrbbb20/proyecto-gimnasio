package com.proyectogimnasio.rutina.repository;

import com.proyectogimnasio.rutina.model.Ejercicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class EjercicioRepositoryTest {

    @Autowired
    private EjercicioRepository ejercicioRepository;

    @Test
    void debeGuardarEjercicioEnElCatalogoGlobal() {
        // Arrange
        Ejercicio ejercicio = new Ejercicio(null, "Dominadas", "Espalda", 10, new HashSet<>());

        // Act
        Ejercicio guardado = ejercicioRepository.save(ejercicio);

        // Assert
        assertNotNull(guardado.getId());
        assertEquals("Dominadas", guardado.getNombreEjercicio());
        assertEquals("Espalda", guardado.getZonaEjercitada());
        assertEquals(10, guardado.getRepeticiones());
    }

    @Test
    void debeBuscarEjercicioPorId() {
        // Arrange
        Ejercicio ejercicio = new Ejercicio(null, "Press Militar", "Hombros", 12, new HashSet<>());
        Ejercicio guardado = ejercicioRepository.save(ejercicio);

        // Act
        Optional<Ejercicio> resultado = ejercicioRepository.findById(guardado.getId());

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Press Militar", resultado.get().getNombreEjercicio());
    }

    @Test
    void debeListarTodosLosEjerciciosGlobales() {
        // Arrange
        ejercicioRepository.save(new Ejercicio(null, "Ejercicio A", "Zona A", 8, new HashSet<>()));
        ejercicioRepository.save(new Ejercicio(null, "Ejercicio B", "Zona B", 15, new HashSet<>()));

        // Act
        List<Ejercicio> resultado = ejercicioRepository.findAll();

        // Assert
        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarEjercicioCorrectamente() {
        // Arrange
        Ejercicio ejercicio = new Ejercicio(null, "Fondos", "Tríceps", 12, new HashSet<>());
        Ejercicio guardado = ejercicioRepository.save(ejercicio);

        // Act
        ejercicioRepository.deleteById(guardado.getId());
        Optional<Ejercicio> resultado = ejercicioRepository.findById(guardado.getId());

        // Assert
        assertFalse(resultado.isPresent());
    }
}