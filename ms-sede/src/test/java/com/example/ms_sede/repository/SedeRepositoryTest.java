package com.example.ms_sede.repository;

import com.example.ms_sede.model.Sede;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class SedeRepositoryTest {
    @Autowired
    private SedeRepository repository;

    @Test
    void debeGuardarSede() {

        Sede sede = new Sede(
                1L, "Antonio Varas", "E.Yanes",800,2100,1L
        );

        Sede guardada = repository.save(sede);

        assertNotNull(guardada.getId());
        assertEquals(1L, guardada.getId());
        assertEquals("Antonio Varas",guardada.getNombre());
        assertEquals("E.Yanes",guardada.getDireccion());
        assertEquals(800,guardada.getHoraApertura());
        assertEquals(2100,guardada.getHoraCierre());
        assertEquals(1L,guardada.getIdEncargado());
    }

    @Test
    void debeBuscarSedePorId() {

        Sede guardada = repository.save(
                new Sede(
                        1L,"Antonio Varas", "E.Yanes",800,2100,1L
                )
        );

        Optional<Sede> resultado =
                repository.findById(guardada.getId());

        assertTrue(resultado.isPresent());

        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void debeListarSedes() {

        repository.save(
                new Sede(1L, "Antonio Varas", "E.Yanes",800,2100,1L)
        );


        List<Sede> resultado =
                repository.findAll();

        assertFalse(resultado.isEmpty());

        assertTrue(resultado.size() >= 1);
    }

    @Test
    void debeEliminarSede() {

        Sede guardada = repository.save(
                new Sede(1L,"Antonio Varas", "E.Yanes",800,2100,1L)
        );

        repository.deleteById(guardada.getId());

        Optional<Sede> resultado =
                repository.findById(guardada.getId());

        assertFalse(resultado.isPresent());
    }

    @Test
    void debeBuscarPorIdSede() {

        repository.save(
                new Sede(1L,"Antonio Varas", "E.Yanes",800,2100,1L)
        );

        Optional<Sede> resultado =
                repository.findById(1L);

        assertFalse(resultado.isEmpty());

        assertEquals(
                1L,
                resultado.get().getId()
        );
    }

    @Test
    void debeRetornarTrueCuandoIdExiste() {

        repository.save(new Sede(1L,"Antonio Varas", "E.Yanes",800,2100,1L));

        boolean existe = repository.existsById(1L);

        assertTrue(existe);
    }

    @Test
    void debeRetornarFalseCuandoIdNoExiste() {
        boolean existe = repository.existsById(3L);
        assertFalse(existe);
    }
}

