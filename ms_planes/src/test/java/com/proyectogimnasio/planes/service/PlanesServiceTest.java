package com.proyectogimnasio.planes.service;

import com.proyectogimnasio.planes.client.ClienteClient;
import com.proyectogimnasio.planes.dto.*;
import com.proyectogimnasio.planes.model.Pagos;
import com.proyectogimnasio.planes.model.Planes;
import com.proyectogimnasio.planes.repository.PagosRepository;
import com.proyectogimnasio.planes.repository.PlanesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanesServiceTest {

    @Mock
    private PlanesRepository planesRepository;

    @Mock
    private PagosRepository pagosRepository;

    @Mock
    private ClienteClient clienteClient;

    @InjectMocks
    private PlanesService planesService;

    private String tokenDummy;
    private Pagos pagoMock;
    private Planes planMock;

    @BeforeEach
    public void setUp() {
        tokenDummy = "Bearer token-de-prueba-123";

        pagoMock = new Pagos();
        pagoMock.setId(10L);
        pagoMock.setTipoPago("Debito");
        pagoMock.setNumTarjeta("123456******7890");
        pagoMock.setIdCliente(1L);

        planMock = new Planes();
        planMock.setId(1L);
        planMock.setNombrePlan("Plan Anual");
        planMock.setPrecioPlan(new BigDecimal("250000"));
        planMock.setDescripcionPlan("Acceso libre");
        planMock.setBeneficios("Lockers incluidos");
        planMock.setIdPago(pagoMock);
    }

    @Nested
    public class PlanesTests {

        @Test
        public void debeAgregarPlanExitosamenteCuandoPagoExiste() {
            // Arrange
            PlanesRequest request = new PlanesRequest();
            request.setNombrePlan("Plan Anual");
            request.setPrecioPlan(new BigDecimal("250000"));
            request.setIdPago(10L);

            when(pagosRepository.findById(10L)).thenReturn(Optional.of(pagoMock));
            when(planesRepository.save(any(Planes.class))).thenReturn(planMock);

            // Act
            PlanesResponse response = planesService.addPlan(request, tokenDummy);

            // Assert
            assertNotNull(response);
            assertEquals(1L, response.getId());
            assertEquals("Plan Anual", response.getNombrePlan());
            assertEquals(10L, response.getIdPago());
            verify(planesRepository, times(1)).save(any(Planes.class));
        }

        @Test
        public void debeLanzarExceptionAlAgregarPlanSiPagoNoExiste() {
            PlanesRequest request = new PlanesRequest();
            request.setIdPago(99L);

            when(pagosRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> planesService.addPlan(request, tokenDummy));
            verify(planesRepository, never()).save(any());
        }

        @Test
        public void debeBuscarPlanPorIdExitosamente() {
            when(planesRepository.findById(1L)).thenReturn(Optional.of(planMock));

            PlanesResponse response = planesService.findByIdPlan(1L, tokenDummy);

            assertNotNull(response);
            assertEquals("Plan Anual", response.getNombrePlan());
        }

        @Test
        public void debeLanzarExceptionSiPlanNoExiste() {
            when(planesRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> planesService.findByIdPlan(1L, tokenDummy));
        }

        @Test
        public void debeListarTodosLosPlanes() {
            when(planesRepository.findAll()).thenReturn(List.of(planMock));

            List<PlanesResponse> list = planesService.getAllPlanes(tokenDummy);

            assertFalse(list.isEmpty());
            assertEquals(1, list.size());
            assertEquals(1L, list.get(0).getId());
        }

        @Test
        public void debeActualizarPlanExitosamente() {
            PlanesRequest request = new PlanesRequest();
            request.setNombrePlan("Plan Anual Modificado");
            request.setPrecioPlan(new BigDecimal("260000"));
            request.setIdPago(10L);

            when(planesRepository.findById(1L)).thenReturn(Optional.of(planMock));
            when(pagosRepository.findById(10L)).thenReturn(Optional.of(pagoMock));

            planMock.setNombrePlan("Plan Anual Modificado");
            when(planesRepository.save(any(Planes.class))).thenReturn(planMock);

            PlanesResponse response = planesService.updatePlan(1L, request, tokenDummy);

            assertNotNull(response);
            assertEquals("Plan Anual Modificado", response.getNombrePlan());
        }

        @Test
        public void debeEliminarPlanSiExiste() {
            when(planesRepository.existsById(1L)).thenReturn(true);
            doNothing().when(planesRepository).deleteById(1L);

            assertDoesNotThrow(() -> planesService.deletePlan(1L));
            verify(planesRepository, times(1)).deleteById(1L);
        }

        @Test
        public void debeLanzarExceptionAlEliminarPlanInexistente() {
            when(planesRepository.existsById(1L)).thenReturn(false);

            assertThrows(EntityNotFoundException.class, () -> planesService.deletePlan(1L));
            verify(planesRepository, never()).deleteById(anyLong());
        }
    }


    @Nested
    public class PagosTests {

        @Test
        public void debeAgregarPagoExitosamenteCuandoClienteExiste() {
            // Arrange
            PagosRequest request = new PagosRequest();
            request.setTipoPago("Debito");
            request.setIdCliente(1L);

            ClienteResponse clienteMock = ClienteResponse.builder().nombres("Juan").apellidos("Perez").build();

            when(clienteClient.getCliente(1L, tokenDummy)).thenReturn(clienteMock);
            when(pagosRepository.save(any(Pagos.class))).thenReturn(pagoMock);

            // Act
            PagosResponse response = planesService.addPago(request, tokenDummy);

            // Assert
            assertNotNull(response);
            assertEquals(10L, response.getId());
            assertEquals(1L, response.getIdCliente());
            verify(pagosRepository, times(1)).save(any(Pagos.class));
        }

        @Test
        public void debeLanzarExceptionAlAgregarPagoSiClienteNoExiste() {
            PagosRequest request = new PagosRequest();
            request.setIdCliente(5L);

            when(clienteClient.getCliente(5L, tokenDummy)).thenReturn(null);

            assertThrows(EntityNotFoundException.class, () -> planesService.addPago(request, tokenDummy));
            verify(pagosRepository, never()).save(any());
        }

        @Test
        public void debeBuscarPagoPorIdExitosamente() {
            when(pagosRepository.findById(10L)).thenReturn(Optional.of(pagoMock));

            PagosResponse response = planesService.findByIdPago(10L, tokenDummy);

            assertNotNull(response);
            assertEquals("Debito", response.getTipoPago());
        }

        @Test
        public void debeListarTodosLosPagos() {
            when(pagosRepository.findAll()).thenReturn(List.of(pagoMock));

            List<PagosResponse> list = planesService.getAllPagos(tokenDummy);

            assertFalse(list.isEmpty());
            assertEquals(1, list.size());
        }

        @Test
        public void debeActualizarPagoExitosamenteCuandoClienteExiste() {
            PagosRequest request = new PagosRequest();
            request.setTipoPago("Credito");
            request.setIdCliente(1L);

            ClienteResponse clienteMock = ClienteResponse.builder().nombres("Juan").apellidos("Perez").build();

            when(pagosRepository.findById(10L)).thenReturn(Optional.of(pagoMock));
            when(clienteClient.getCliente(1L, tokenDummy)).thenReturn(clienteMock);

            pagoMock.setTipoPago("Credito");
            when(pagosRepository.save(any(Pagos.class))).thenReturn(pagoMock);

            PagosResponse response = planesService.updatePago(10L, request, tokenDummy);

            assertNotNull(response);
            assertEquals("Credito", response.getTipoPago());
        }

        @Test
        public void debeLanzarExceptionAlActualizarPagoSiClienteNoExiste() {
            PagosRequest request = new PagosRequest();
            request.setIdCliente(99L);

            when(pagosRepository.findById(10L)).thenReturn(Optional.of(pagoMock));
            when(clienteClient.getCliente(99L, tokenDummy)).thenReturn(null);

            assertThrows(EntityNotFoundException.class, () -> planesService.updatePago(10L, request, tokenDummy));
            verify(pagosRepository, never()).save(any());
        }

        @Test
        public void debeEliminarPagoSiExiste() {
            when(pagosRepository.existsById(10L)).thenReturn(true);
            doNothing().when(pagosRepository).deleteById(10L);

            assertDoesNotThrow(() -> planesService.deletePago(10L));
            verify(pagosRepository, times(1)).deleteById(10L);
        }

        @Test
        public void debeLanzarExceptionAlEliminarPagoInexistente() {
            when(pagosRepository.existsById(10L)).thenReturn(false);

            assertThrows(EntityNotFoundException.class, () -> planesService.deletePago(10L));
            verify(pagosRepository, never()).deleteById(anyLong());
        }
    }
}