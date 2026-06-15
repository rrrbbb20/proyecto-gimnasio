package com.proyectogimnasio.planes.repository;

import com.proyectogimnasio.planes.model.Planes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class PlanesRepositoryTest {

    @Autowired
    private PlanesRepository planesRepository;

    @Test
    public void debeGuardarPlanExitosamente() {
        // Arrange
        Planes plan = new Planes(null, "Plan Premium", new BigDecimal("45000"), "Acceso total", "Gimnasio + Piscina");

        // Act
        Planes guardado = planesRepository.save(plan);

        // Assert
        assertNotNull(guardado.getId());
        assertEquals("Plan Premium", guardado.getNombrePlan());
        assertEquals(new BigDecimal("45000"), guardado.getPrecioPlan());
    }

    @Test
    public void debeBuscarPlanPorId() {
        // Arrange
        Planes plan = new Planes(null, "Plan Estudiante", new BigDecimal("25000"), "Lunes a Viernes", "Ninguno");
        Planes guardado = planesRepository.save(plan);

        // Act
        Optional<Planes> resultado = planesRepository.findById(guardado.getId());

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(guardado.getId(), resultado.get().getId());
        assertEquals("Plan Estudiante", resultado.get().getNombrePlan());
    }

    @Test
    public void debeListarPlanes() {
        // Arrange
        planesRepository.save(new Planes(null, "Plan Diario", new BigDecimal("5000"), "Solo 1 dia", "Lockers"));
        planesRepository.save(new Planes(null, "Plan Mensual", new BigDecimal("30000"), "Mes completo", "Lockers + Toalla"));

        // Act
        List<Planes> resultado = planesRepository.findAll();

        // Assert
        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    public void debeEliminarPlan() {
        // Arrange
        Planes plan = new Planes(null, "Plan Temporal", new BigDecimal("10000"), "Por vencer", "Ninguno");
        Planes guardado = planesRepository.save(plan);

        // Act
        planesRepository.deleteById(guardado.getId());
        Optional<Planes> resultado = planesRepository.findById(guardado.getId());

        // Assert
        assertTrue(resultado.isEmpty());
    }
}