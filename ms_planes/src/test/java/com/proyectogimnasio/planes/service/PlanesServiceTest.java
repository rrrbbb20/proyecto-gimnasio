package com.proyectogimnasio.planes.service;

import com.proyectogimnasio.planes.client.ClienteClient;
import com.proyectogimnasio.planes.dto.*;
import com.proyectogimnasio.planes.model.Pagos;
import com.proyectogimnasio.planes.model.Planes;
import com.proyectogimnasio.planes.model.Suscripcion;
import com.proyectogimnasio.planes.repository.PagosRepository;
import com.proyectogimnasio.planes.repository.PlanesRepository;
import com.proyectogimnasio.planes.repository.SuscripcionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanesServiceTest {

    @Mock
    private PlanesRepository planesRepository;

    @Mock
    private PagosRepository pagosRepository;

    @Mock
    private SuscripcionRepository suscripcionRepository;

    @Mock
    private ClienteClient clienteClient;

    @InjectMocks
    private PlanesService planesService;

    private ClienteResponse clienteMock;

    @BeforeEach
    void setUp() {
        clienteMock = ClienteResponse.builder()
                .nombres("Juan")
                .apellidos("Pérez")
                .build();
    }



    @Test
    void addPlan_DebeGuardarYRetornarPlan() {
        PlanesRequest request = new PlanesRequest();
        request.setNombrePlan("Plan Premium");
        request.setPrecioPlan(new BigDecimal("45000"));
        request.setDescripcionPlan("Acceso completo");
        request.setBeneficios("Piscina + Máquinas");

        Planes planGuardado = new Planes(1L, "Plan Premium", new BigDecimal("45000"), "Acceso completo", "Piscina + Máquinas");
        when(planesRepository.save(any(Planes.class))).thenReturn(planGuardado);

        PlanesResponse response = planesService.addPlan(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Plan Premium", response.getNombrePlan());
        verify(planesRepository, times(1)).save(any(Planes.class));
    }

    @Test
    void findByIdPlan_CuandoExiste_DebeRetornarPlan() {
        Planes plan = new Planes(1L, "Plan Básico", new BigDecimal("20000"), "Solo máquinas", "Ninguno");
        when(planesRepository.findById(1L)).thenReturn(Optional.of(plan));

        PlanesResponse response = planesService.findByIdPlan(1L);

        assertNotNull(response);
        assertEquals("Plan Básico", response.getNombrePlan());
    }

    @Test
    void findByIdPlan_CuandoNoExiste_DebeLanzarException() {
        when(planesRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> planesService.findByIdPlan(1L));
    }

    @Test
    void getAllPlanes_DebeRetornarLista() {
        List<Planes> lista = List.of(new Planes(1L, "Plan 1", BigDecimal.TEN, "", ""));
        when(planesRepository.findAll()).thenReturn(lista);

        List<PlanesResponse> response = planesService.getAllPlanes();

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void updatePlan_CuandoExiste_DebeActualizar() {
        Planes planExistente = new Planes(1L, "Plan Viejo", BigDecimal.ONE, "", "");
        PlanesRequest request = new PlanesRequest();
        request.setNombrePlan("Plan Nuevo");

        when(planesRepository.findById(1L)).thenReturn(Optional.of(planExistente));
        when(planesRepository.save(any(Planes.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PlanesResponse response = planesService.updatePlan(1L, request);

        assertNotNull(response);
        assertEquals("Plan Nuevo", response.getNombrePlan());
    }

    @Test
    void deletePlan_CuandoExiste_DebeEliminar() {
        when(planesRepository.existsById(1L)).thenReturn(true);
        doNothing().when(planesRepository).deleteById(1L);

        assertDoesNotThrow(() -> planesService.deletePlan(1L));
        verify(planesRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePlan_CuandoNoExiste_DebeLanzarException() {
        when(planesRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> planesService.deletePlan(1L));
        verify(planesRepository, never()).deleteById(anyLong());
    }


    @Test
    void addPago_CuandoClienteExiste_DebeGuardar() {
        PagosRequest request = new PagosRequest();
        request.setIdCliente(10L);
        request.setTipoPago("Tarjeta Crédito");

        Pagos pagoGuardado = new Pagos(1L, "Tarjeta Crédito", "1234", "12/30", 123, "Calle 1", "123", 10L);
        when(clienteClient.getCliente(10L)).thenReturn(clienteMock);
        when(pagosRepository.save(any(Pagos.class))).thenReturn(pagoGuardado);

        PagosResponse response = planesService.addPago(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(10L, response.getIdCliente());
    }

    @Test
    void addPago_CuandoClienteNoExiste_DebeLanzarException() {
        PagosRequest request = new PagosRequest();
        request.setIdCliente(99L);

        when(clienteClient.getCliente(99L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> planesService.addPago(request));
        verify(pagosRepository, never()).save(any(Pagos.class));
    }

    @Test
    void findByIdPago_CuandoExiste_DebeRetornarPago() {
        Pagos pago = new Pagos(1L, "Efectivo", null, null, null, null, null, 10L);
        when(pagosRepository.findById(1L)).thenReturn(Optional.of(pago));

        PagosResponse response = planesService.findByIdPago(1L);

        assertNotNull(response);
        assertEquals("Efectivo", response.getTipoPago());
    }

    @Test
    void updatePago_CuandoExisteYClienteEsValido_DebeActualizar() {
        Pagos pagoExistente = new Pagos(1L, "Viejo", null, null, null, null, null, 10L);
        PagosRequest request = new PagosRequest();
        request.setIdCliente(10L);
        request.setTipoPago("Nuevo");

        when(pagosRepository.findById(1L)).thenReturn(Optional.of(pagoExistente));
        when(clienteClient.getCliente(10L)).thenReturn(clienteMock);
        when(pagosRepository.save(any(Pagos.class))).thenAnswer(inv -> inv.getArgument(0));

        PagosResponse response = planesService.updatePago(1L, request);

        assertNotNull(response);
        assertEquals("Nuevo", response.getTipoPago());
    }

    @Test
    void updatePago_CuandoClienteNoExiste_DebeLanzarException() {
        Pagos pagoExistente = new Pagos(1L, "Viejo", null, null, null, null, null, 10L);
        PagosRequest request = new PagosRequest();
        request.setIdCliente(99L);

        when(pagosRepository.findById(1L)).thenReturn(Optional.of(pagoExistente));
        when(clienteClient.getCliente(99L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> planesService.updatePago(1L, request));
    }

    @Test
    void deletePago_CuandoNoExiste_DebeLanzarException() {
        when(pagosRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> planesService.deletePago(1L));
    }


    @Test
    void crearSuscripcion_CuandoClienteYPlanExisten_DebeProcesarCorrectamente() {
        SuscripcionRequest request = new SuscripcionRequest();
        request.setIdCliente(10L);
        request.setIdPlan(2L);
        PagosRequest pagoReq = new PagosRequest();
        pagoReq.setTipoPago("Debito");
        request.setPago(pagoReq);

        Planes plan = new Planes(2L, "Plan Trimestral", BigDecimal.ONE, "", "");
        Pagos pagoGuardado = new Pagos(5L, "Debito", null, null, null, null, null, 10L);
        Suscripcion suscripcionGuardada = new Suscripcion(1L, 10L, plan, pagoGuardado, LocalDate.now(), LocalDate.now().plusMonths(1), "ACTIVA");

        when(clienteClient.getCliente(10L)).thenReturn(clienteMock);
        when(planesRepository.findById(2L)).thenReturn(Optional.of(plan));
        when(pagosRepository.save(any(Pagos.class))).thenReturn(pagoGuardado);
        when(suscripcionRepository.save(any(Suscripcion.class))).thenReturn(suscripcionGuardada);

        SuscripcionResponse response = planesService.crearSuscripcion(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("ACTIVA", response.getEstado());
        assertEquals(LocalDate.now().plusMonths(1), response.getFechaFin());
        verify(suscripcionRepository, times(1)).save(any(Suscripcion.class));
    }

    @Test
    void crearSuscripcion_CuandoClienteInexistente_DebeLanzarException() {
        SuscripcionRequest request = new SuscripcionRequest();
        request.setIdCliente(99L);

        when(clienteClient.getCliente(99L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> planesService.crearSuscripcion(request));
    }

    @Test
    void getSuscripcionByCliente_CuandoExiste_DebeRetornarSuscripcion() {
        Planes plan = new Planes(1L, "Plan", BigDecimal.ONE, "", "");
        Pagos pago = new Pagos(1L, "Cash", null, null, null, null, null, 10L);
        Suscripcion suscripcion = new Suscripcion(1L, 10L, plan, pago, LocalDate.now(), LocalDate.now().plusMonths(1), "ACTIVA");

        when(suscripcionRepository.findByIdCliente(10L)).thenReturn(Optional.of(suscripcion));
        when(clienteClient.getCliente(10L)).thenReturn(clienteMock);

        SuscripcionResponse response = planesService.getSuscripcionByCliente(10L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("ACTIVA", response.getEstado());
    }

    @Test
    void updateSuscripcion_CuandoSeCancela_DebeFijarFechaFinAlDiaDeHoy() {
        Planes plan = new Planes(1L, "Plan", BigDecimal.ONE, "", "");
        Pagos pago = new Pagos(1L, "Cash", null, null, null, null, null, 10L);
        Suscripcion suscripcion = new Suscripcion(1L, 10L, plan, pago, LocalDate.now().minusDays(5), LocalDate.now().plusDays(25), "ACTIVA");

        when(suscripcionRepository.findById(1L)).thenReturn(Optional.of(suscripcion));
        when(suscripcionRepository.save(any(Suscripcion.class))).thenAnswer(inv -> inv.getArgument(0));
        when(clienteClient.getCliente(10L)).thenReturn(clienteMock);

        SuscripcionResponse response = planesService.updateSuscripcion(1L, "cancelada");

        assertNotNull(response);
        assertEquals("CANCELADA", response.getEstado());
        assertEquals(LocalDate.now(), response.getFechaFin());
    }

    @Test
    void deleteSuscripcion_CuandoNoExiste_DebeLanzarException() {
        when(suscripcionRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> planesService.deleteSuscripcion(1L));
    }

    @Test
    void mapToResponseSuscripcion_CuandoFallaClienteExterno_DebeAmortiguarException() {
        Planes plan = new Planes(1L, "Plan", BigDecimal.ONE, "", "");
        Pagos pago = new Pagos(1L, "Cash", null, null, null, null, null, 10L);
        Suscripcion suscripcion = new Suscripcion(1L, 10L, plan, pago, LocalDate.now(), LocalDate.now().plusMonths(1), "ACTIVA");

        when(suscripcionRepository.findByIdCliente(10L)).thenReturn(Optional.of(suscripcion));
        when(clienteClient.getCliente(10L)).thenThrow(new RuntimeException("Error de red de prueba"));

        assertDoesNotThrow(() -> {
            SuscripcionResponse response = planesService.getSuscripcionByCliente(10L);
            assertNotNull(response);
            assertEquals(1L, response.getId());
        });
    }
}