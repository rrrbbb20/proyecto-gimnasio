package com.example.ms_inventario.repository;

import com.example.ms_inventario.model.Inventario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class InventarioRepositoryTest {

    @Autowired
    private InventarioRepository repository;

    @Test
    void debeGuardarInventario() {
        Inventario inventario = new Inventario(
                null,
                "Pelota",
                "Pelota de fútbol",
                29.99,
                LocalDate.parse("2024-01-15"),
                10L
        );

        Inventario guardado = repository.save(inventario);

        assertNotNull(guardado.getId());
        assertEquals("Pelota", guardado.getNombre());
        assertEquals("Pelota de fútbol", guardado.getDescripcion());
        assertEquals(29.99, guardado.getPrecio());
        assertEquals(LocalDate.parse("2024-01-15"), guardado.getFechaRegistro());
        assertEquals(10L, guardado.getIdMantenimiento());
    }

    @Test
    void debeBuscarInventarioPorId() {
        Inventario inventario = new Inventario(
                null,
                "Conos",
                "Conos de entrenamiento",
                15.00,
                LocalDate.parse("2024-03-20"),
                10L
        );

        Inventario guardado = repository.save(inventario);

        Optional<Inventario> resultado = repository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Conos", resultado.get().getNombre());
        assertEquals("Conos de entrenamiento", resultado.get().getDescripcion());
        assertEquals(15.00, resultado.get().getPrecio());
    }

    @Test
    void debeListarInventarios() {
        repository.save(new Inventario(
                null,
                "Pelota",
                "Pelota de fútbol",
                29.99,
                LocalDate.parse("2024-01-15"),
                10L
        ));

        repository.save(new Inventario(
                null,
                "Conos",
                "Conos de entrenamiento",
                15.00,
                LocalDate.parse("2024-03-20"),
                10L
        ));

        List<Inventario> resultado = repository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarInventario() {
        Inventario inventario = new Inventario(
                null,
                "Peto",
                "Peto deportivo",
                8.50,
                LocalDate.parse("2024-05-10"),
                10L
        );

        Inventario guardado = repository.save(inventario);

        repository.deleteById(guardado.getId());

        Optional<Inventario> resultado = repository.findById(guardado.getId());

        assertFalse(resultado.isPresent());
    }

    @Test
    void debeActualizarInventario() {
        Inventario inventario = new Inventario(
                null,
                "Pelota",
                "Pelota de fútbol",
                29.99,
                LocalDate.parse("2024-01-15"),
                10L
        );

        Inventario guardado = repository.save(inventario);

        guardado.setNombre("Pelota Pro");
        guardado.setPrecio(49.99);
        Inventario actualizado = repository.save(guardado);

        assertEquals("Pelota Pro", actualizado.getNombre());
        assertEquals(49.99, actualizado.getPrecio());
        assertEquals(guardado.getId(), actualizado.getId());
    }
}