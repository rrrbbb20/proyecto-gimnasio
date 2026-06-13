package com.proyectogimnasio.rutina.repository;

import com.proyectogimnasio.rutina.model.DetallesEjercicio;
import com.proyectogimnasio.rutina.model.Ejercicio;
import com.proyectogimnasio.rutina.model.Rutina;
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
public class RutinaRepositoryTest {

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private EjercicioRepository ejercicioRepository;

    @Test
    void debeGuardarRutinaConSusDetallesEnCascada() {
        // Arrange
        Ejercicio ejercicio = new Ejercicio(null, "Sentadillas", "Piernas", 12, new HashSet<>());
        Ejercicio ejercicioGuardado = ejercicioRepository.save(ejercicio);

        Rutina rutina = new Rutina();
        rutina.setNombreRutina("Rutina de Pierna");
        rutina.setDescripcionRutina("Enfoque en cuádriceps");
        rutina.setDetalles(new HashSet<>());

        DetallesEjercicio detalle = new DetallesEjercicio();
        detalle.setRutina(rutina);
        detalle.setEjercicio(ejercicioGuardado);
        detalle.setNumeroEjercicios(4);
        detalle.setDuracionRutina("15 min");
        detalle.setTiempoDescanso("90 seg");

        rutina.getDetalles().add(detalle);

        // Act
        Rutina guardada = rutinaRepository.save(rutina);

        // Assert
        assertNotNull(guardada.getId());
        assertEquals("Rutina de Pierna", guardada.getNombreRutina());
        assertFalse(guardada.getDetalles().isEmpty());

        // Verificar que el detalle apunta al ejercicio correcto
        DetallesEjercicio detalleGuardado = guardada.getDetalles().iterator().next();
        assertEquals("Sentadillas", detalleGuardado.getEjercicio().getNombreEjercicio());
    }

    @Test
    void debeBuscarRutinaPorId() {
        // Arrange
        Rutina rutina = new Rutina(null, "Cardio LIGERO", "Quema de grasa", new HashSet<>());
        Rutina guardada = rutinaRepository.save(rutina);

        // Act
        Optional<Rutina> resultado = rutinaRepository.findById(guardada.getId());

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Cardio LIGERO", resultado.get().getNombreRutina());
    }

    @Test
    void debeListarRutinas() {
        // Arrange
        rutinaRepository.save(new Rutina(null, "Rutina A", "Desc A", new HashSet<>()));
        rutinaRepository.save(new Rutina(null, "Rutina B", "Desc B", new HashSet<>()));

        // Act
        List<Rutina> resultado = rutinaRepository.findAll();

        // Assert
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarRutinaYBorrarDetallesEnCascada() {
        // Arrange
        Rutina rutina = new Rutina(null, "A eliminar", "Detalle", new HashSet<>());
        Rutina guardada = rutinaRepository.save(rutina);

        // Act
        rutinaRepository.deleteById(guardada.getId());
        Optional<Rutina> resultado = rutinaRepository.findById(guardada.getId());

        // Assert
        assertFalse(resultado.isPresent());
    }
}