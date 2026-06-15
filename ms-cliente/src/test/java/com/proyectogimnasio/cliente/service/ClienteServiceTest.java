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
import java.util.Map;
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

    private Map<String, Object> getPagoMock() {
        return Map.of(
                "tipoPago", "DEBITO",
                "numTarjeta", "1234567890123456",
                "fechaVencimiento", "12/28",
                "cvc", 123,
                "direccionFacturacion", "Santiago",
                "codigoPostal", "8320000"
        );
    }

    @Test
    void deberiaRetornarClienteCuandoExiste() {
        // Arrange
        Cliente cliente = new Cliente(1L, "vicentito", "garcia", "7.435.565-9", "vicentito.garcia1@gmail.com", LocalDate.of(2007, 12, 1), 1L);
        PlanesResponse planMock = new PlanesResponse();

        when(repo.findById(1L)).thenReturn(Optional.of(cliente));
        when(client.getPlan(eq(1L), anyString())).thenReturn(planMock);

        // Act
        ClienteResponse resultado = service.findById(1L, "Bearer token");

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(repo).findById(1L);
        verify(client).getPlan(eq(1L), anyString());
    }

    @Test
    void deberiaLanzarExcepcionCuandoClienteNoExiste() {
        // Arrange
        when(repo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.findById(1L, "Bearer token"));
        verify(repo).findById(1L);
    }

    @Test
    void deberiaGuardarClienteYActivarSuscripcionCorrectamente() {
        // Arrange
        ClienteRequest dto = new ClienteRequest();
        dto.setNombres("Juan");
        dto.setApellidos("Perez");
        dto.setRun("12345678-9");
        dto.setCorreo("juan@gmail.com");
        dto.setFechaNac(LocalDate.of(1995, 5, 12));
        dto.setIdPlan(1L);
        dto.setPago(getPagoMock());

        PlanesResponse planMock = new PlanesResponse();
        Cliente clienteGuardado = new Cliente(10L, "Juan", "Perez", "12345678-9", "juan@gmail.com", LocalDate.of(1995, 5, 12), 1L);

        when(client.getPlan(eq(1L), anyString())).thenReturn(planMock);
        when(repo.save(any(Cliente.class))).thenReturn(clienteGuardado);
        when(client.activarSuscripcion(any(), anyString())).thenReturn(new Object());

        // Act
        ClienteResponse resultado = service.add(dto, "Bearer token");

        // Assert
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        verify(client).getPlan(eq(1L), anyString());
        verify(repo).save(any(Cliente.class));
        verify(client).activarSuscripcion(any(), anyString());
    }

    @Test
    void deberiaLanzarExcepcionSiElPagoOSuscripcionFallan() {
        // Arrange
        ClienteRequest dto = new ClienteRequest();
        dto.setNombres("Juan");
        dto.setIdPlan(1L);
        dto.setPago(getPagoMock());

        PlanesResponse planMock = new PlanesResponse();
        Cliente clienteGuardado = new Cliente(10L, "Juan", "Perez", "12345678-9", "juan@gmail.com", LocalDate.of(1995, 5, 12), 1L);

        when(client.getPlan(eq(1L), anyString())).thenReturn(planMock);
        when(repo.save(any(Cliente.class))).thenReturn(clienteGuardado);
        when(client.activarSuscripcion(any(), anyString())).thenThrow(new RuntimeException("Error de red o pago rechazado"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.add(dto, "Bearer token"));
    }

    @Test
    void deberiaListarTodosLosClientes() {
        // Arrange
        List<Cliente> listaMock = List.of(
                new Cliente(1L, "A", "B", "1", "a@a.com", LocalDate.now().minusYears(20), 1L)
        );
        when(repo.findAll()).thenReturn(listaMock);
        when(client.getPlan(anyLong(), anyString())).thenReturn(new PlanesResponse());

        // Act
        List<ClienteResponse> resultado = service.getAll("Bearer token");

        // Assert
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
        when(client.getPlan(eq(1L), anyString())).thenReturn(planMock);
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

        // Act
        service.delete(1L);

        // Assert
        verify(repo).deleteById(1L);
    }
}