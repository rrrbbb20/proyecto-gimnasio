package com.example.ms_mantenimiento.repository;

import com.example.ms_mantenimiento.model.Mantenimiento;
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
class MantenimientoRepositoryTest {

    @Autowired
    private MantenimientoRepository repository;

    @Test
    void debeGuardarMantenimiento() {

        Mantenimiento mantenimiento = new Mantenimiento(
                null,
                "Empresa SPA",
                "Mantenimiento de maquina",
                LocalDate.parse("2024-02-13"),
                15000.0
        );

        Mantenimiento guardado = repository.save(mantenimiento);

        assertNotNull(guardado.getId());
        assertEquals("Empresa SPA", guardado.getEmpresa());
        assertEquals("Mantenimiento de maquina", guardado.getDescripcionMantenimiento());
        assertEquals(LocalDate.parse("2024-02-13"), guardado.getFechaMantenimiento());
        assertEquals(15000.0, guardado.getPrecio());
    }

    @Test
    void debeBuscarMantenimientoPorId() {

        Mantenimiento mantenimiento = new Mantenimiento(
                null,
                "Empresa Nueva",
                "Cambio de pieza",
                LocalDate.parse("2025-04-10"),
                20000.0
        );

        Mantenimiento guardado = repository.save(mantenimiento);

        Optional<Mantenimiento> resultado = repository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Empresa Nueva", resultado.get().getEmpresa());
        assertEquals("Cambio de pieza", resultado.get().getDescripcionMantenimiento());
    }

    @Test
    void debeListarMantenimientos() {

        repository.save(
                new Mantenimiento(
                        null,
                        "Empresa SPA",
                        "Mantenimiento de maquina",
                        LocalDate.parse("2024-02-13"),
                        15000.0
                )
        );

        repository.save(
                new Mantenimiento(
                        null,
                        "Empresa Nueva",
                        "Cambio de pieza",
                        LocalDate.parse("2025-04-10"),
                        20000.0
                )
        );

        List<Mantenimiento> resultado = repository.findAll();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarMantenimiento() {

        Mantenimiento mantenimiento = new Mantenimiento(
                null,
                "Empresa SPA",
                "Mantenimiento de maquina",
                LocalDate.parse("2024-02-13"),
                15000.0
        );

        Mantenimiento guardado = repository.save(mantenimiento);

        repository.deleteById(guardado.getId());

        Optional<Mantenimiento> resultado = repository.findById(guardado.getId());

        assertFalse(resultado.isPresent());
    }
}
