package com.proyectogimnasio.cliente.service;

import com.proyectogimnasio.cliente.client.PlanesClient;
import com.proyectogimnasio.cliente.dto.ClienteRequest;
import com.proyectogimnasio.cliente.dto.ClienteResponse;
import com.proyectogimnasio.cliente.dto.PlanesResponse;
import com.proyectogimnasio.cliente.model.Cliente;
import com.proyectogimnasio.cliente.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository repo;

    @Mock
    private PlanesClient client;

    @InjectMocks
    private ClienteService service;

    @Test
    void deberiaRetornarClienteCuandoExiste() {
        // Arrange
        Cliente cliente = new Cliente(1L, "vicentito", "garcia", "7.435.565-9", "vicentito.garcia1@gmail.com", LocalDate.of(2007, 12, 1), 1L);
        PlanesResponse planMock = new PlanesResponse();

        when(repo.findById(1L)).thenReturn(Optional.of(cliente));
        when(client.getPlan(eq(1L), anyString())).thenReturn(planMock);

        // Act
        ClienteResponse resultado = service.findById(1L, "Bearer token-valido");

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(planMock, resultado.getDetallesPlan());
        verify(repo).findById(1L);
        verify(client).getPlan(eq(1L), anyString());
    }

    @Test
    void deberiaLanzarExcepcionCuandoClienteNoExiste() {
        // Arrange
        when(repo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.findById(1L, "token"));
    }

    @Test
    void deberiaAgregarClienteExitosamente() {
        // Arrange
        ClienteRequest request = new ClienteRequest();
        request.setNombres("vicentito");
        request.setApellidos("garcia");
        request.setRun("7.435.565-9");
        request.setCorreo("vicentito.garcia1@gmail.com");
        request.setFechaNac(LocalDate.of(2007, 12, 1));
        request.setIdPlan(1L);

        PlanesResponse planMock = new PlanesResponse();
        Cliente clienteGuardado = new Cliente(1L, "vicentito", "garcia", "7.435.565-9", "vicentito.garcia1@gmail.com", LocalDate.of(2007, 12, 1), 1L);

        when(client.getPlan(eq(1L), anyString())).thenReturn(planMock);
        when(repo.save(any(Cliente.class))).thenReturn(clienteGuardado);

        // Act
        ClienteResponse resultado = service.add(request, "Bearer token");

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(client).getPlan(eq(1L), anyString());
        verify(repo).save(any(Cliente.class));
    }

    @Test
    void deberiaListarTodosLosClientes() {
        // Arrange
        List<Cliente> lista = List.of(
                new Cliente(1L, "vicentito", "garcia", "7.435.565-9", "vicentito.garcia1@gmail.com", LocalDate.of(2007, 12, 1), 1L)
        );
        when(repo.findAll()).thenReturn(lista);

        // Act
        List<ClienteResponse> resultado = service.getAll("Bearer token"); // Añadido el token requerido

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(repo).findAll();
    }

    @Test
    void deberiaActualizarClientePorId() {
        // Arrange
        Cliente existente = new Cliente(1L, "vicentito", "garcia", "7.435.565-9", "vicentito.garcia1@gmail.com", LocalDate.of(2007, 12, 1), 1L);
        ClienteRequest dto = new ClienteRequest();
        dto.setNombres("Cliente nuevo");
        dto.setApellidos("Garcia");
        dto.setRun("7.435.565-9");
        dto.setCorreo("vicentito.garcia1@gmail.com");
        dto.setFechaNac(LocalDate.of(2007, 12, 1));
        dto.setIdPlan(1L);

        PlanesResponse planMock = new PlanesResponse();
        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(client.getPlan(eq(1L), any())).thenReturn(planMock);
        when(repo.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ClienteResponse resultado = service.update(1L, dto, "Bearer token");

        // Assert
        assertEquals(1L, resultado.getId());
        assertEquals("Cliente nuevo", resultado.getNombres());
        verify(repo).findById(1L);
        verify(repo).save(any(Cliente.class));
    }

    @Test
    void deberiaEliminarClientePorId() {
        // Arrange
        when(repo.existsById(1L)).thenReturn(true);
        doNothing().when(repo).deleteById(1L);

        // Act
        assertDoesNotThrow(() -> service.delete(1L));

        // Assert
        verify(repo).existsById(1L);
        verify(repo).deleteById(1L);
    }
}