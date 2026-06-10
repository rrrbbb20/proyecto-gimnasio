package com.proyectogimnasio.cliente.service;

import com.proyectogimnasio.cliente.client.PlanesClient;
import com.proyectogimnasio.cliente.dto.ClienteRequest;
import com.proyectogimnasio.cliente.dto.ClienteResponse;
import com.example.ms_planes.dto.PlanesResponse;
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
    private com.proyectogimnasio.cliente.client.PlanesClient client;

    @InjectMocks
    private ClienteService service;

    @Test
    void deberiaRetornarClienteCuandoExiste() {
        // Arrange
        Cliente cliente = new Cliente(1L, "vicentito", "garcia", "7.435.565-9", "vicentito.garcia1@gmail.com", LocalDate.of(2007, 12, 1), 1L);
        when(repo.findById(1L)).thenReturn(Optional.of(cliente));

        PlanesResponse planMock = new PlanesResponse();
        when(client.getPlan(anyLong(), anyString())).thenReturn(planMock);

        // Act
        ClienteResponse resultado = service.findById(1L, "aaa");

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("vicentito", resultado.getNombres());
        assertEquals("garcia", resultado.getApellidos());
        assertEquals("7.435.565-9", resultado.getRun());
        assertEquals("vicentito.garcia1@gmail.com", resultado.getCorreo());
        assertEquals(LocalDate.of(2007, 12, 1), resultado.getFechaNac());
        assertEquals(1L, resultado.getIdPlan());
        verify(repo).findById(1L);
    }

    @Test
    void deberiaLanzarExcepcionCuandoClienteNoExiste() {
        // Arrange
        when(repo.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.findById(99L, null)
        );

        assertEquals("Cliente no encontrado", ex.getMessage());
        verify(repo).findById(99L);
    }

    @Test
    void deberiaRetornarListaCliente() {
        // Arrange
        Cliente cliente = new Cliente(1L, "vicentito", "garcia", "7.435.565-9", "vicentito.garcia1@gmail.com", LocalDate.of(2007, 12, 1), 1L);
        when(repo.findAll()).thenReturn(List.of(cliente));
        var planMock = new PlanesResponse(1L, "Plan Mensual", 29990, null);
        when(client.getPlan(anyLong(), anyString())).thenReturn(planMock);

        // Act
        List<ClienteResponse> resultado = service.getAll("s");

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("vicentito", resultado.get(0).getNombres());
        verify(repo).findAll();
    }

    @Test
    void deberiaCrearClienteCorrectamente() {
        // Arrange
        ClienteRequest dto = new ClienteRequest();
        dto.setNombres("Isabel Allende");
        dto.setApellidos("Garcia");
        dto.setRun("7.435.565-9");
        dto.setCorreo("vicentito.garcia1@gmail.com");
        dto.setFechaNac(LocalDate.of(2007, 12, 1));
        dto.setIdPlan(1L);

        Cliente clienteGuardado = new Cliente(1L, "vicentito", "garcia", "7.435.565-9", "vicentito.garcia1@gmail.com", LocalDate.of(2007, 12, 1), 1L);
        when(repo.save(any(Cliente.class))).thenReturn(clienteGuardado);
        PlanesResponse planMock = new PlanesResponse();
        when(client.getPlan(anyLong(), anyString())).thenReturn(planMock);

        // Act
        ClienteResponse resultado = service.add(dto, "22");

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("vicentito", resultado.getNombres());
        assertEquals("garcia", resultado.getApellidos());
        assertEquals("7.435.565-9", resultado.getRun());
        assertEquals("vicentito.garcia1@gmail.com", resultado.getCorreo());
        assertEquals(LocalDate.of(2007, 12, 1), resultado.getFechaNac());
        assertEquals(1L, resultado.getIdPlan());
        verify(repo).save(any(Cliente.class));
    }

    @Test
    void deberiaActualizarClienteCorrectamente() {
        // Arrange
        Cliente existente = new Cliente(1L, "vicentito", "garcia", "7.435.565-9", "vicentito.garcia1@gmail.com", LocalDate.of(2007, 12, 1), 1L);

        ClienteRequest dto = new ClienteRequest();
        dto.setNombres("Cliente nuevo");
        dto.setApellidos("Garcia");
        dto.setRun("7.435.565-9");
        dto.setCorreo("vicentito.garcia1@gmail.com");
        dto.setFechaNac(LocalDate.of(2007, 12, 1));
        dto.setIdPlan(1L);

        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PlanesResponse planMock = new PlanesResponse();
        when(client.getPlan(anyLong(), any())).thenReturn(planMock);

        // Act
        ClienteResponse resultado = service.update(1L, dto, null);

        // Assert
        assertEquals(1L, resultado.getId());
        assertEquals("Cliente nuevo", resultado.getNombres());
        assertEquals("Garcia", resultado.getApellidos());
        assertEquals("7.435.565-9", resultado.getRun());
        assertEquals("vicentito.garcia1@gmail.com", resultado.getCorreo());
        assertEquals(LocalDate.of(2007, 12, 1), resultado.getFechaNac());
        assertEquals(1L, resultado.getIdPlan());
        verify(repo).findById(1L);
        verify(repo).save(any(Cliente.class));
    }

    @Test
    void deberiaEliminarClientePorId() {
        // Arrange
        when(repo.existsById(1L)).thenReturn(true);
        doNothing().when(repo).deleteById(1L);

        // Act
        service.delete(1L);

        // Assert
        verify(repo).existsById(1L);
        verify(repo).deleteById(1L);
    }
}
