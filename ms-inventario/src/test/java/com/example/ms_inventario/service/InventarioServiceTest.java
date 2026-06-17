package com.example.ms_inventario.service;

import com.example.ms_inventario.client.MantenimientoClient;
import com.example.ms_inventario.dto.InventarioRequest;
import com.example.ms_inventario.dto.InventarioResponse;
import com.example.ms_inventario.dto.MantenimientoResponse;
import com.example.ms_inventario.model.Inventario;
import com.example.ms_inventario.repository.InventarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest {

    @Mock
    private InventarioRepository repository;

    @Mock
    private MantenimientoClient mantenimientoClient;

    @InjectMocks
    private InventarioService service;

    private static final String TOKEN = "Bearer test-token";

    // ─── Helper para crear un MantenimientoResponse de prueba ───────────────
    private MantenimientoResponse mantenimientoMock() {
        // Ajusta los campos según tu MantenimientoResponse real
        MantenimientoResponse m = new MantenimientoResponse();
        m.setId(10L);
        return m;
    }

    // ─── Helper para crear un Inventario de prueba ───────────────────────────
    private Inventario inventarioMock() {
        return new Inventario(1L, "Pelota", "Pelota de fútbol",
                29.99, LocalDate.parse("2024-01-15"), 10L);
    }

    // ─── Helper para crear un InventarioRequest de prueba ────────────────────
    private InventarioRequest requestMock() {
        InventarioRequest req = new InventarioRequest();
        req.setNombre("Pelota");
        req.setDescripcion("Pelota de fútbol");
        req.setPrecio(29.99);
        req.setFechaRegistro(LocalDate.parse("2024-01-15"));
        req.setIdMantenimiento(10L);
        return req;
    }

    // ════════════════════════════════════════════════════════════════
    // ADD
    // ════════════════════════════════════════════════════════════════

    @Test
    public void cuandoAgregaInventario() {
        InventarioRequest req = requestMock();
        Inventario inventario = inventarioMock();
        MantenimientoResponse mantenimiento = mantenimientoMock();

        when(mantenimientoClient.obtenerMantenimiento(10L, TOKEN)).thenReturn(mantenimiento);
        when(repository.save(any(Inventario.class))).thenReturn(inventario);
        // mapToResponse también llama al client con i.getId() (id=1L)
        when(mantenimientoClient.obtenerMantenimiento(1L, TOKEN)).thenReturn(mantenimiento);

        InventarioResponse respuesta = service.add(req, TOKEN);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals("Pelota", respuesta.getNombre());
        assertEquals("Pelota de fútbol", respuesta.getDescripcion());
        assertEquals(29.99, respuesta.getPrecio());
        assertEquals(LocalDate.parse("2024-01-15"), respuesta.getFechaRegistro());
        assertNotNull(respuesta.getInfoMantenimiento());

        verify(mantenimientoClient).obtenerMantenimiento(10L, TOKEN);
        verify(repository).save(any(Inventario.class));
    }

    @Test
    public void cuandoMantenimientoNoExisteAlAgregar() {
        InventarioRequest req = requestMock();

        when(mantenimientoClient.obtenerMantenimiento(10L, TOKEN)).thenReturn(null);

        RuntimeException excepcion = assertThrows(RuntimeException.class,
                () -> service.add(req, TOKEN));

        assertEquals("mantenimiento no existe", excepcion.getMessage());
        verify(mantenimientoClient).obtenerMantenimiento(10L, TOKEN);
        verify(repository, never()).save(any());
    }

    // ════════════════════════════════════════════════════════════════
    // FIND BY ID
    // ════════════════════════════════════════════════════════════════

    @Test
    public void retornarInventarioCuandoExiste() {
        Inventario inventario = inventarioMock();
        MantenimientoResponse mantenimiento = mantenimientoMock();

        when(repository.findById(1L)).thenReturn(Optional.of(inventario));
        // mapToResponse usa i.getId() → 1L
        when(mantenimientoClient.obtenerMantenimiento(1L, TOKEN)).thenReturn(mantenimiento);

        InventarioResponse respuesta = service.findById(1L, TOKEN);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals("Pelota", respuesta.getNombre());
        assertEquals("Pelota de fútbol", respuesta.getDescripcion());
        assertEquals(29.99, respuesta.getPrecio());
        assertEquals(LocalDate.parse("2024-01-15"), respuesta.getFechaRegistro());

        verify(repository).findById(1L);
    }

    @Test
    public void cuandoInventarioNoExistePorId() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException excepcion = assertThrows(EntityNotFoundException.class,
                () -> service.findById(1L, TOKEN));

        assertEquals("Inventario no encontrado", excepcion.getMessage());
        verify(repository).findById(1L);
    }

    // ════════════════════════════════════════════════════════════════
    // GET ALL
    // ════════════════════════════════════════════════════════════════

    @Test
    public void debeListarInventarios() {
        Inventario inventario = inventarioMock();
        MantenimientoResponse mantenimiento = mantenimientoMock();

        when(repository.findAll()).thenReturn(List.of(inventario));
        when(mantenimientoClient.obtenerMantenimiento(1L, TOKEN)).thenReturn(mantenimiento);

        List<InventarioResponse> resultado = service.getAll(TOKEN);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Pelota", resultado.get(0).getNombre());
        verify(repository).findAll();
    }

    // ════════════════════════════════════════════════════════════════
    // UPDATE
    // ════════════════════════════════════════════════════════════════

    @Test
    public void puedoActualizarInventario() {
        Inventario inventario = inventarioMock();
        MantenimientoResponse mantenimiento = mantenimientoMock();

        InventarioRequest req = new InventarioRequest();
        req.setNombre("Conos");
        req.setDescripcion("Conos de entrenamiento");
        req.setPrecio(15.00);
        req.setFechaRegistro(LocalDate.parse("2024-03-20"));
        req.setIdMantenimiento(10L);

        when(mantenimientoClient.obtenerMantenimiento(10L, TOKEN)).thenReturn(mantenimiento);
        when(repository.findById(1L)).thenReturn(Optional.of(inventario));
        when(repository.save(any(Inventario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        // mapToResponse usa el id del objeto guardado → 1L
        when(mantenimientoClient.obtenerMantenimiento(1L, TOKEN)).thenReturn(mantenimiento);

        InventarioResponse resultado = service.update(1L, req, TOKEN);

        assertNotNull(resultado);
        assertEquals("Conos", resultado.getNombre());
        assertEquals("Conos de entrenamiento", resultado.getDescripcion());
        assertEquals(15.00, resultado.getPrecio());
        assertEquals(LocalDate.parse("2024-03-20"), resultado.getFechaRegistro());

        verify(repository).findById(1L);
        verify(repository).save(any(Inventario.class));
    }

    @Test
    public void cuandoInventarioNoExisteAlActualizar() {
        InventarioRequest req = requestMock();
        MantenimientoResponse mantenimiento = mantenimientoMock();

        when(mantenimientoClient.obtenerMantenimiento(10L, TOKEN)).thenReturn(mantenimiento);
        when(repository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException excepcion = assertThrows(EntityNotFoundException.class,
                () -> service.update(1L, req, TOKEN));

        assertEquals("Inventario no Encontrado", excepcion.getMessage());
        verify(repository).findById(1L);
        verify(repository, never()).save(any());
    }

    @Test
    public void cuandoMantenimientoNoExisteAlActualizar() {
        InventarioRequest req = requestMock();

        when(mantenimientoClient.obtenerMantenimiento(10L, TOKEN)).thenReturn(null);

        RuntimeException excepcion = assertThrows(RuntimeException.class,
                () -> service.update(1L, req, TOKEN));

        assertEquals("mantenimiento no existe", excepcion.getMessage());
        verify(repository, never()).findById(anyLong());
    }

    // ════════════════════════════════════════════════════════════════
    // DELETE
    // ════════════════════════════════════════════════════════════════

    @Test
    public void debeBorrarInventario() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }
}