package com.proyectogimnasio.cliente.repository;


import com.proyectogimnasio.cliente.model.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ClienteRepositoryTest { // Nombre de clase corregido

    @Autowired
    private ClienteRepository repository;

    @Test
    void debeGuardarCliente() {
        // Arrange
        Cliente cliente = new Cliente(null, "vicentito", "garcia", "7.435.565-9", "vicentito.garcia1@gmail.com", LocalDate.of(2007, 12, 1), 1L);

        // Act
        Cliente guardado = repository.save(cliente);

        // Assert
        assertNotNull(guardado.getId());
        assertEquals("vicentito", guardado.getNombres()); // Coincide con lo guardado
        assertEquals("garcia", guardado.getApellidos());  // Coincide con lo guardado
        assertEquals("7.435.565-9", guardado.getRun());
        assertEquals("vicentito.garcia1@gmail.com", guardado.getCorreo());
        assertEquals(LocalDate.of(2007, 12, 1), guardado.getFechaNac());
        assertEquals(1L, guardado.getIdPlan());
    }

    @Test
    void debeBuscarClientePorId() {
        // Arrange
        Cliente cliente = new Cliente(null, "vicentito", "garcia", "7.435.565-9", "vicentito.garcia1@gmail.com", LocalDate.of(2007, 12, 1), 1L);
        Cliente guardado = repository.save(cliente);

        // Act
        Optional<Cliente> resultado = repository.findById(guardado.getId());

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(guardado.getId(), resultado.get().getId()); // Compara de forma dinámica contra el ID real generado
        assertEquals("vicentito", resultado.get().getNombres());
        assertEquals("garcia", resultado.get().getApellidos());
        assertEquals("7.435.565-9", resultado.get().getRun());
        assertEquals("vicentito.garcia1@gmail.com", resultado.get().getCorreo());
        assertEquals(LocalDate.of(2007, 12, 1), resultado.get().getFechaNac());
        assertEquals(1L, resultado.get().getIdPlan());
    }

    @Test
    void debeListarClientes() {
        // Arrange
        repository.save(new Cliente(null, "vicentito", "garcia", "7.435.565-9", "vicentito.garcia1@gmail.com", LocalDate.of(2007, 12, 1), 1L));
        repository.save(new Cliente(null, "isabel", "allende", "12.345.678-9", "isabel@gmail.com", LocalDate.of(1942, 8, 2), 2L));

        // Act
        List<Cliente> resultado = repository.findAll();

        // Assert
        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }

    @Test
    void debeEliminarCliente() {
        // Arrange
        Cliente cliente = new Cliente(null, "vicentito", "garcia", "7.435.565-9", "vicentito.garcia1@gmail.com", LocalDate.of(2007, 12, 1), 1L);
        Cliente guardado = repository.save(cliente);

        // Act
        repository.deleteById(guardado.getId());

        // Assert
        Optional<Cliente> resultado = repository.findById(guardado.getId());
        assertFalse(resultado.isPresent());
    }
}
