package com.example.ms_sede.service;

import com.example.ms_sede.client.EncargadoClient;
import com.example.ms_sede.dto.EncargadoResponse;
import com.example.ms_sede.dto.SedeRequest;
import com.example.ms_sede.dto.SedeResponse;
import com.example.ms_sede.model.Sede;
import com.example.ms_sede.repository.SedeRepository;
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
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SedeServiceTest {

    @Mock
    private SedeRepository repository;

    @Mock
    private EncargadoClient  encargadoClient;

    @InjectMocks
    private SedeService service;

    @Test
    public void cuandoAgregaSede(){
        SedeRequest sRequest = new SedeRequest();
        when(encargadoClient.obtenerEncargado(anyLong(), any()))
                .thenReturn(new EncargadoResponse());
        sRequest.setIdEncargado(1L);
        sRequest.setNombre("Antonio Varias");
        sRequest.setDireccion("E.Yanes");
        sRequest.setHoraCierre(1900);
        sRequest.setHoraApertura(800);
        //creo el sede request que se ingresa por el service
        Sede sede = new Sede(1L,"Antonio Varias",
                "E.Yanes",800,1900,1L);
        //creo la sede que al ser creado el repositorio deberia retornar
        when(repository.save(any(Sede.class))).thenReturn(sede);
        //cuando en el repositorio se guarde cualquier Sede se
        //retorna la sede creado anteriormente
        SedeResponse respuesta = service.add(sRequest,null);
        // se toma un sederesponse que es lo que devuelve el service
        assertNotNull(respuesta);
        //comprueba solo que el objeto no es null
        assertEquals(1L,respuesta.getId());
        //aqui hace la comparacion del id y que coincidam
        assertEquals("Antonio Varias",respuesta.getNombre());
        //mismo para nombre
        assertEquals("E.Yanes",respuesta.getDireccion());
        //mismo para direccion
        assertEquals(1900,respuesta.getHoraCierre());
        //mismo para hora cierre
        assertEquals(800,respuesta.getHoraApertura());
        //mismo para hora apertura
        verify(repository).save((any(Sede.class)));}
    @Test
    public void cuandoNoPuedeAgregarPorIdEncargadoRepetido(){
        SedeRequest sRequest = new SedeRequest();
        sRequest.setIdEncargado(1L);
        sRequest.setNombre("Antonio Varias");
        sRequest.setDireccion("E. Yanes");
        sRequest.setHoraCierre(1900);
        sRequest.setHoraApertura(800);
        when(encargadoClient.obtenerEncargado(anyLong(), any()))
                .thenReturn(new EncargadoResponse());
        //creo el request de sede
        when(repository.existsByIdEncargado(1L)).thenReturn(true);
        //cuando se haga la verificacion del id retornara verdadero
        IllegalStateException excepcion = assertThrows(IllegalStateException.class,
                () -> service.add(sRequest,null));
        // hago la excepcion correcta con lo que deberia devolver
        assertEquals("El encargado ya esta ocupado en otra sede",excepcion.getMessage());
        verify(repository).existsByIdEncargado(1L);

    }
    @Test
    public void retornarSedeCuandoExiste(){
        Sede sede = new Sede();
        when(encargadoClient.obtenerEncargado(anyLong(), any()))
                .thenReturn(new EncargadoResponse());
        sede.setId(1L);
        sede.setNombre("Antonio Varias");
        sede.setDireccion("E.Yanes");
        sede.setHoraCierre(1900);
        sede.setHoraApertura(800);
        when(repository.findById(1L)).thenReturn(Optional.of(sede));
        SedeResponse respuesta = service.obtenerSede(1L,null);
        assertNotNull(respuesta);
        assertEquals(1L,respuesta.getId());
        assertEquals("Antonio Varias",respuesta.getNombre());
        assertEquals("E.Yanes",respuesta.getDireccion());
        assertEquals(1900,respuesta.getHoraCierre());
        assertEquals(800,respuesta.getHoraApertura());
        verify(repository).findById(1L);    }

    @Test
    public void cuandoEntrenadorNoExiste(){
        when(repository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException excepcion = assertThrows(
                EntityNotFoundException.class,
                ()->service.obtenerSede(1L,null));

        assertEquals("Sede no encontrada", excepcion.getMessage());
        verify(repository).findById(1L);
    }

    @Test
    public void puedoActualizarSede(){
        Sede sede = new Sede(1L,"Antonio Varas","E.Yanes",1900,800,1L);
        when(encargadoClient.obtenerEncargado(anyLong(), any()))
                .thenReturn(new EncargadoResponse());
        SedeRequest sRequest = new SedeRequest();
        sRequest.setIdEncargado(1L);
        sRequest.setNombre("Antonio Varias");
        sRequest.setDireccion("E.Yanes");
        sRequest.setHoraCierre(1900);
        sRequest.setHoraApertura(800);

        when(repository.findById(1L)).thenReturn(Optional.of(sede));
        when(repository.save(any(Sede.class))).
                thenAnswer(invocation
                        -> invocation.getArgument(0));

        SedeResponse resultado = service.actualizarSede(1L,sRequest,null);
        assertNotNull(resultado);
        assertEquals(1L,resultado.getId());
        assertEquals("Antonio Varias",resultado.getNombre());
        assertEquals("E.Yanes",resultado.getDireccion());
        assertEquals(1900,resultado.getHoraCierre());
        assertEquals(800,resultado.getHoraApertura());
        verify(repository).findById(1L);
    }

    @Test
    public void debeListarEntrenadores(){
        Sede sede = new Sede(1L,"Antonio Varias","E.Yanes",1900,800,1L);
        when(encargadoClient.obtenerEncargado(anyLong(), any()))
                .thenReturn(new EncargadoResponse());
        when(repository.findAll()).thenReturn(List.of(sede));
        List<SedeResponse> resultado = service.listar(null);
        assertFalse(resultado.isEmpty());
        assertEquals(1,resultado.size());
        assertEquals(1L,resultado.get(0).getId());
        verify(repository).findAll();
    }
    @Test
    public void debeBorrar(){
        doNothing().when(repository).deleteById(1L);
        service.eliminarSede(1L);
        verify(repository).deleteById(1L);
    }
    @Test
    public void retornarBusquedaPorId(){
        Sede sede = new Sede(1L,"Antonio Varias","E.Yanes",800,1900,1L);
        when(encargadoClient.obtenerEncargado(anyLong(), any()))
                .thenReturn(new EncargadoResponse());
        when(repository.findById(1L)).thenReturn(Optional.of(sede));
        SedeResponse resultado = service.obtenerSede(1L,null);
        assertNotNull(resultado);
        assertEquals(1L,resultado.getId());
        assertEquals("Antonio Varias",resultado.getNombre());
        assertEquals("E.Yanes",resultado.getDireccion());
        assertEquals(1900,resultado.getHoraCierre());
        assertEquals(800,resultado.getHoraApertura());
        verify(repository).findById(1L);
    }
    @Test
    public void cuandoNoEncuentroPorId(){
        when(repository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException excepcion = assertThrows(EntityNotFoundException.class,()->service.obtenerSede(1L,null ));
        assertEquals("Sede no encontrada", excepcion.getMessage());
        verify(repository).findById(1L);
    }
}
