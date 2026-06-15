package com.example.ms_encargado.repository;

import com.example.ms_encargado.model.Encargado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("test")
class EncargadoRepositoryTest {

    @Autowired
    private EncargadoRepository repository;

    @Test
    void debeGuardarEncargado() {

        Encargado encargado = new Encargado(
                null,
                "Juan Diaz",
                "111-1",
                "Santiago",
                LocalDate.parse("1992-02-13")
        );

        Encargado guardado =
                repository.save(encargado);

        assertNotNull(guardado.getId());
        assertEquals(
                "Juan Diaz",
                guardado.getNombreCompleto());
        assertEquals(
                "111-1",
                guardado.getRun());
        assertEquals(
                "Santiago",
                guardado.getDireccion());
        assertEquals(
                LocalDate.parse("1992-02-13"),
                guardado.getFechaNacimiento());
    }

    @Test
    void debeBuscarEncargadoPorId() {

        Encargado encargado = new Encargado(
                null,
                "Pedro Diaz",
                "222-2",
                "Valparaiso",
                LocalDate.parse("1995-04-10")
        );

        Encargado guardado =
                repository.save(encargado);

        Optional<Encargado> resultado =
                repository.findById(
                        guardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals(
                "Pedro Diaz",
                resultado.get().getNombreCompleto());
        assertEquals(
                "222-2",
                resultado.get().getRun());
        assertEquals(
                "Valparaiso",
                resultado.get().getDireccion());
    }

    @Test
    void debeListarEncargados() {

        repository.save(
                new Encargado(
                        null,
                        "Juan Diaz",
                        "111-1",
                        "Santiago",
                        LocalDate.parse("1992-02-13")
                )
        );

        repository.save(
                new Encargado(
                        null,
                        "Pedro Diaz",
                        "222-2",
                        "Valparaiso",
                        LocalDate.parse("1995-04-10")
                )
        );

        List<Encargado> resultado =
                repository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarEncargado() {

        Encargado encargado = new Encargado(
                null,
                "Juan Diaz",
                "111-1",
                "Santiago",
                LocalDate.parse("1992-02-13")
        );

        Encargado guardado =
                repository.save(encargado);

        repository.deleteById(
                guardado.getId());

        Optional<Encargado> resultado =
                repository.findById(
                        guardado.getId());

        assertFalse(resultado.isPresent());
    }
}