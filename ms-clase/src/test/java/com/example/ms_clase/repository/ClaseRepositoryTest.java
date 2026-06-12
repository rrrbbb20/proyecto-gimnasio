package com.example.ms_clase.repository;

import com.example.ms_clase.model.Clase;
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
public class ClaseRepositoryTest {
    @Autowired
    private ClaseRepository repository;

    @Test
    void debeGuardarClase() {

        Clase clase = new Clase(
                null,
                "Tenis",
                "Para todos",
                "Inicial",
                LocalDate.parse("2026-08-16"),
                LocalTime.parse("16:00:00"),
                10,
                true,
                2L
        );

        Clase guardada = repository.save(clase);

        assertNotNull(guardada.getId());
        assertEquals("Tenis", guardada.getNombreClase());
        assertEquals("Para todos", guardada.getDescripcion());
        assertEquals("Inicial", guardada.getNivelDeClase());
        assertEquals(10, guardada.getCupos());
        assertEquals(true, guardada.getEstado());
        assertEquals(2L, guardada.getIdEntrenador());
    }

    @Test
    void debeBuscarClasePorId() {

        Clase guardada = repository.save(
                new Clase(
                        null,
                        "Yoga",
                        "Clase tranquila",
                        "Basico",
                        LocalDate.parse("2026-08-16"),
                        LocalTime.parse("18:00:00"),
                        20,
                        true,
                        1L
                )
        );

        Optional<Clase> resultado =
                repository.findById(guardada.getId());

        assertTrue(resultado.isPresent());

        assertEquals("Yoga",
                resultado.get().getNombreClase());
    }

    @Test
    void debeListarClases() {

        repository.save(
                new Clase(
                        null,
                        "Tenis",
                        "Para todos",
                        "Inicial",
                        LocalDate.parse("2026-08-16"),
                        LocalTime.parse("16:00:00"),
                        10,
                        true,
                        2L
                )
        );

        repository.save(
                new Clase(
                        null,
                        "Natacion",
                        "Avanzado",
                        "Avanzado",
                        LocalDate.parse("2026-08-17"),
                        LocalTime.parse("10:00:00"),
                        15,
                        true,
                        3L
                )
        );

        List<Clase> resultado =
                repository.findAll();

        assertFalse(resultado.isEmpty());

        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarClase() {

        Clase guardada = repository.save(
                new Clase(
                        null,
                        "Crossfit",
                        "Intenso",
                        "Intermedio",
                        LocalDate.parse("2026-08-20"),
                        LocalTime.parse("09:00:00"),
                        8,
                        true,
                        1L
                )
        );

        repository.deleteById(guardada.getId());

        Optional<Clase> resultado =
                repository.findById(guardada.getId());

        assertFalse(resultado.isPresent());
    }

    @Test
    void debeBuscarPorNombreClase() {

        repository.save(
                new Clase(
                        null,
                        "Tenis",
                        "Para todos",
                        "Inicial",
                        LocalDate.parse("2026-08-16"),
                        LocalTime.parse("16:00:00"),
                        10,
                        true,
                        2L
                )
        );

        List<Clase> resultado =
                repository.findByNombreClase("Tenis");

        assertFalse(resultado.isEmpty());

        assertEquals(
                "Tenis",
                resultado.get(0).getNombreClase()
        );
    }

    @Test
    void debeRestarCupo() {

        Clase guardada = repository.save(
                new Clase(
                        null,
                        "Tenis",
                        "Para todos",
                        "Inicial",
                        LocalDate.parse("2026-08-16"),
                        LocalTime.parse("16:00:00"),
                        10,
                        true,
                        2L
                )
        );

        int filas =
                repository.restarCupo(
                        guardada.getId()
                );

        Clase actualizada =
                repository.findById(
                        guardada.getId()
                ).get();

        assertEquals(1, filas);

        assertEquals(
                9,
                actualizada.getCupos()
        );
    }

    @Test
    void noDebeRestarCupoCuandoEsCero() {

        Clase guardada = repository.save(
                new Clase(
                        null,
                        "Tenis",
                        "Para todos",
                        "Inicial",
                        LocalDate.parse("2026-08-16"),
                        LocalTime.parse("16:00:00"),
                        0,
                        true,
                        2L
                )
        );

        int filas =
                repository.restarCupo(
                        guardada.getId()
                );

        assertEquals(0, filas);
    }

    @Test
    void debeSumarCupo() {

        Clase guardada = repository.save(
                new Clase(
                        null,
                        "Tenis",
                        "Para todos",
                        "Inicial",
                        LocalDate.parse("2026-08-16"),
                        LocalTime.parse("16:00:00"),
                        10,
                        true,
                        2L
                )
        );

        int filas =
                repository.sumarCupo(
                        guardada.getId()
                );

        Clase actualizada =
                repository.findById(
                        guardada.getId()
                ).get();

        assertEquals(1, filas);

        assertEquals(
                11,
                actualizada.getCupos()
        );
    }

}

