package com.example.ms_inscripcion_clase.repository;

import com.example.ms_inscripcion_clase.model.InscripcionClase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class InscripcionClaseRepositoryTest {
    @Autowired
    private InscripcionClaseRepository repository;

    @Test
    public void debeGuardarInscripcion() {

        InscripcionClase inscripcion =
                new InscripcionClase(
                        null,
                        1L,
                        2L,
                        LocalDate.parse("2026-06-11"),
                        LocalTime.parse("16:00:00")
                );

        InscripcionClase guardada =
                repository.save(inscripcion);

        assertNotNull(guardada.getId());

        assertEquals(
                1L,
                guardada.getIdClase()
        );

        assertEquals(
                2L,
                guardada.getIdCliente()
        );

        assertEquals(
                LocalDate.parse("2026-06-11"),
                guardada.getFechaInscripcion()
        );

        assertEquals(
                LocalTime.parse("16:00:00"),
                guardada.getHoraInscripcion()
        );
    }

    @Test
    public void debeBuscarInscripcionPorId() {

        InscripcionClase guardada =
                repository.save(
                        new InscripcionClase(
                                null,
                                3L,
                                4L,
                                LocalDate.now(),
                                LocalTime.now()
                        )
                );

        Optional<InscripcionClase> resultado =
                repository.findById(
                        guardada.getId()
                );

        assertTrue(resultado.isPresent());

        assertEquals(
                3L,
                resultado.get().getIdClase()
        );

        assertEquals(
                4L,
                resultado.get().getIdCliente()
        );
    }

    @Test
    public void debeListarInscripciones() {

        repository.save(
                new InscripcionClase(
                        null,
                        1L,
                        1L,
                        LocalDate.now(),
                        LocalTime.now()
                )
        );

        repository.save(
                new InscripcionClase(
                        null,
                        2L,
                        2L,
                        LocalDate.now(),
                        LocalTime.now()
                )
        );

        List<InscripcionClase> resultado =
                repository.findAll();

        assertFalse(resultado.isEmpty());

        assertTrue(
                resultado.size() >= 2
        );
    }

    @Test
    public void debeEliminarInscripcion() {

        InscripcionClase guardada =
                repository.save(
                        new InscripcionClase(
                                null,
                                1L,
                                2L,
                                LocalDate.now(),
                                LocalTime.now()
                        )
                );

        repository.deleteById(
                guardada.getId()
        );

        Optional<InscripcionClase> resultado =
                repository.findById(
                        guardada.getId()
                );

        assertFalse(
                resultado.isPresent()
        );
    }

    @Test
    public void debeRetornarTrueSiExisteClaseYCliente() {

        repository.save(
                new InscripcionClase(
                        null,
                        10L,
                        20L,
                        LocalDate.now(),
                        LocalTime.now()
                )
        );

        boolean existe =
                repository.existsByIdClaseAndIdCliente(
                        10L,
                        20L
                );

        assertTrue(existe);
    }

    @Test
    public void debeRetornarFalseSiNoExisteClaseYCliente() {

        boolean existe =
                repository.existsByIdClaseAndIdCliente(
                        99L,
                        88L
                );

        assertFalse(existe);
    }

}
