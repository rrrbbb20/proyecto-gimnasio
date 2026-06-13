package com.example.ms_inscripcion_clase.service;

import com.example.ms_inscripcion_clase.client.ClaseClient;
import com.example.ms_inscripcion_clase.client.ClienteClient;
import com.example.ms_inscripcion_clase.dto.ClaseResponse;
import com.example.ms_inscripcion_clase.dto.ClienteResponse;
import com.example.ms_inscripcion_clase.dto.InscripcionClaseRequest;
import com.example.ms_inscripcion_clase.dto.InscripcionClaseResponse;
import com.example.ms_inscripcion_clase.model.InscripcionClase;
import com.example.ms_inscripcion_clase.repository.InscripcionClaseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InscripcionClaseServiceTest {
    @InjectMocks
    private InscripcionClaseService service;
    @Mock
    private ClaseClient claseClient;
    @Mock
    private ClienteClient clienteClient;
    @Mock
    private InscripcionClaseRepository repository;

    @Test
    public void debeAgregarInscripcion(){
        InscripcionClaseRequest request = new InscripcionClaseRequest();
        request.setIdClase(1L);
        request.setIdCliente(2L);
        ClaseResponse clase = new ClaseResponse();
        clase.setId(1L);
        ClienteResponse cliente = new ClienteResponse();
        cliente.setId(2L);
        InscripcionClase inscripcion =
                new InscripcionClase(1L, 1L, 2L, LocalDate.now(), LocalTime.now());
        when(claseClient.getClase(1L,"1111"))
                .thenReturn(clase);
        when(clienteClient.getCliente(2L,"1111"))
                .thenReturn(cliente);
        when(repository.existsByIdClaseAndIdCliente(1L,2L))
                .thenReturn(false);
        when(repository.save(any(InscripcionClase.class)))
                .thenReturn(inscripcion);
        InscripcionClaseResponse resultado =
                service.add(request,"1111");
        assertNotNull(resultado);
        assertEquals(1L,resultado.getIdInscripcion());
        assertEquals(1L,resultado.getClase().getId());
        assertEquals(2L,resultado.getCliente().getId());
        verify(claseClient, times(2)).getClase(1L,"1111");
        verify(clienteClient, times(2)).getCliente(2L,"1111");
        verify(repository).existsByIdClaseAndIdCliente(1L,2L);
        verify(repository).save(any(InscripcionClase.class));
        verify(claseClient).restarCupo(1L,"1111");
    }
    @Test
    public void noPuedeAgregarSiClaseNoExiste(){
        InscripcionClaseRequest request = new InscripcionClaseRequest();
        request.setIdClase(1L);
        request.setIdCliente(2L);
        when(claseClient.getClase(1L,"1111"))
                .thenReturn(null);
        EntityNotFoundException excepcion =
                assertThrows(EntityNotFoundException.class,
                        () -> service.add(request,"1111"));
        assertEquals("Clase no encontrada",excepcion.getMessage());
        verify(claseClient).getClase(1L,"1111");
    }
    @Test
    public void noPuedeAgregarSiClienteNoExiste(){
        InscripcionClaseRequest request = new InscripcionClaseRequest();
        request.setIdClase(1L);
        request.setIdCliente(2L);
        ClaseResponse clase = new ClaseResponse();
        clase.setId(1L);
        when(claseClient.getClase(1L,"1111"))
                .thenReturn(clase);
        when(clienteClient.getCliente(2L,"1111"))
                .thenReturn(null);
        EntityNotFoundException excepcion = assertThrows(
                EntityNotFoundException.class, () -> service.add(request,"1111"));
        assertEquals(
                "Cliente no encontrado",
                excepcion.getMessage()
        );
    }
    @Test
    public void noPuedeAgregarInscripcionDuplicada(){
        InscripcionClaseRequest request = new InscripcionClaseRequest();
        request.setIdClase(1L);
        request.setIdCliente(2L);
        ClaseResponse clase = new ClaseResponse();
        clase.setId(1L);
        ClienteResponse cliente = new ClienteResponse();
        cliente.setId(2L);
        when(claseClient.getClase(1L,"1111"))
                .thenReturn(clase);
        when(clienteClient.getCliente(2L,"1111"))
                .thenReturn(cliente);
        when(repository.existsByIdClaseAndIdCliente(1L,2L))
                .thenReturn(true);
        IllegalStateException excepcion =
                assertThrows(IllegalStateException.class,
                        () -> service.add(request,"1111"));
        assertEquals("Cliente ya se encuentra inscrito en la clase",
                excepcion.getMessage()
        );
    }
    @Test
    public void debeRetornarInscripcion(){
        InscripcionClase inscripcion =
                new InscripcionClase(1L, 1L, 2L, LocalDate.now(),
                        LocalTime.now()
                );
        ClaseResponse clase = new ClaseResponse();
        clase.setId(1L);
        ClienteResponse cliente = new ClienteResponse();
        cliente.setId(2L);
        when(repository.findById(1L))
                .thenReturn(Optional.of(inscripcion));
        when(claseClient.getClase(1L,"1111"))
                .thenReturn(clase);
        when(clienteClient.getCliente(2L,"1111"))
                .thenReturn(cliente);
        InscripcionClaseResponse resultado =
                service.findById(1L,"1111");
        assertNotNull(resultado);
        assertEquals(1L,
                resultado.getIdInscripcion());
        verify(repository).findById(1L);
    }
    @Test
    public void noEncuentraInscripcion(){
        when(repository.findById(1L))
                .thenReturn(Optional.empty());
        EntityNotFoundException excepcion =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> service.findById(1L,"1111"));
        assertEquals(
                "Inscripcion a clase no encontrada",
                excepcion.getMessage());
    }
    @Test
    public void debeListarInscripciones(){
        InscripcionClase inscripcion =
                new InscripcionClase(1L,1L,2L, LocalDate.now(), LocalTime.now());
        ClaseResponse clase = new ClaseResponse();
        clase.setId(1L);
        ClienteResponse cliente = new ClienteResponse();
        cliente.setId(2L);
        when(repository.findAll())
                .thenReturn(List.of(inscripcion));
        when(claseClient.getClase(1L,"1111"))
                .thenReturn(clase);
        when(clienteClient.getCliente(2L,"1111"))
                .thenReturn(cliente);
        List<InscripcionClaseResponse> resultado =
                service.getAll("1111");
        assertFalse(resultado.isEmpty());
        assertEquals(1,resultado.size());
        verify(repository).findAll();
    }
    @Test
    public void debeEliminarInscripcion(){
        InscripcionClase inscripcion =
                new InscripcionClase(1L, 1L, 2L, LocalDate.now(), LocalTime.now());
        when(repository.existsById(1L))
                .thenReturn(true);
        when(repository.findById(1L))
                .thenReturn(Optional.of(inscripcion));
        doNothing()
                .when(repository)
                .deleteById(1L);
        service.delete(1L,"1111");
        verify(claseClient)
                .sumarCupo(1L,"1111");
        verify(repository)
                .deleteById(1L);
    }
    @Test
    public void noPuedeEliminarInscripcion(){
        when(repository.existsById(1L))
                .thenReturn(false);
        EntityNotFoundException excepcion =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> service.delete(1L,"1111"));
        assertEquals(
                "No se puede eliminar inscripcion no encontrada",
                excepcion.getMessage());
    }
    @Test
    public void verificarExistenciaCorrectamente(){
        InscripcionClaseRequest request =
                new InscripcionClaseRequest();
        request.setIdClase(1L);
        request.setIdCliente(2L);
        ClaseResponse clase = new ClaseResponse();
        clase.setId(1L);
        ClienteResponse cliente = new ClienteResponse();
        cliente.setId(2L);
        when(claseClient.getClase(1L,"1111")).thenReturn(clase);
        when(clienteClient.getCliente(2L,"1111")).thenReturn(cliente);
        assertDoesNotThrow(
                () -> service.verificarExistencia(request, "1111"));
    }
}
