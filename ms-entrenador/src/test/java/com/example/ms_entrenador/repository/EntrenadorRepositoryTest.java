package com.example.ms_entrenador.repository;

import com.example.ms_entrenador.model.Entrenador;
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
public class EntrenadorRepositoryTest {
    @Autowired
    private EntrenadorRepository repository;

    @Test
    void debeGuardarEntrenador() {

        Entrenador entrenador = new Entrenador(
                null,
                "Tito",
                "111-1",
                LocalDate.parse("1992-02-13")
        );

        Entrenador guardado = repository.save(entrenador);

        assertNotNull(guardado.getId());
        assertEquals("Tito", guardado.getNombreCompleto());
        assertEquals("111-1", guardado.getRun());
        assertEquals(
                LocalDate.parse("1992-02-13"),
                guardado.getFechaNacimiento()
        );
    }

    @Test
    void debeBuscarEntrenadorPorId() {

        Entrenador entrenador = new Entrenador(
                null,
                "Paco",
                "222-2",
                LocalDate.parse("1995-04-10")
        );

        Entrenador guardado = repository.save(entrenador);

        Optional<Entrenador> resultado =
                repository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Paco", resultado.get().getNombreCompleto());
        assertEquals("222-2", resultado.get().getRun());
    }

    @Test
    void debeListarEntrenadores() {

        repository.save(new Entrenador(
                null,
                "Tito",
                "111-1",
                LocalDate.parse("1992-02-13")
        ));

        repository.save(new Entrenador(
                null,
                "Paco",
                "222-2",
                LocalDate.parse("1995-04-10")
        ));

        List<Entrenador> resultado = repository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarEntrenador() {

        Entrenador entrenador = new Entrenador(
                null,
                "Tito",
                "111-1",
                LocalDate.parse("1992-02-13")
        );

        Entrenador guardado = repository.save(entrenador);

        repository.deleteById(guardado.getId());

        Optional<Entrenador> resultado =
                repository.findById(guardado.getId());

        assertFalse(resultado.isPresent());
    }

    @Test
    void debeBuscarPorRun() {

        Entrenador entrenador = new Entrenador(
                null,
                "Tito",
                "111-1",
                LocalDate.parse("1992-02-13")
        );

        repository.save(entrenador);

        Optional<Entrenador> resultado =
                repository.findByRun("111-1");

        assertTrue(resultado.isPresent());
        assertEquals("Tito", resultado.get().getNombreCompleto());
        assertEquals("111-1", resultado.get().getRun());
    }

    @Test
    void debeRetornarTrueCuandoRunExiste() {

        repository.save(new Entrenador(
                null,
                "Tito",
                "111-1",
                LocalDate.parse("1992-02-13")
        ));

        boolean existe = repository.existsByRun("111-1");

        assertTrue(existe);
    }

    @Test
    void debeRetornarFalseCuandoRunNoExiste() {

        boolean existe = repository.existsByRun("999-9");

        assertFalse(existe);
    }

}
