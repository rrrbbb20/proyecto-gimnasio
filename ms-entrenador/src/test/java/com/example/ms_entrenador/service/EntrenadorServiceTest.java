package com.example.ms_entrenador.service;


import com.example.ms_entrenador.dto.EntrenadorRequest;
import com.example.ms_entrenador.dto.EntrenadorResponse;
import com.example.ms_entrenador.model.Entrenador;
import com.example.ms_entrenador.repository.EntrenadorRepository;
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
public class EntrenadorServiceTest {

    @Mock
    private EntrenadorRepository repository;
    @InjectMocks
    private EntrenadorService service;

    @Test
    public void cuandoAgregaEntrenador(){
        EntrenadorRequest eRequest = new EntrenadorRequest();
        eRequest.setNombreCompleto("tito");
        eRequest.setFechaNacimiento(LocalDate.parse("1999-08-15"));
        eRequest.setRun("111-1");
        //creo el entrenador request que se ingresa por el service
        Entrenador entrenador = new Entrenador(1L,"tito",
                "111-1",LocalDate.parse("1999-08-15"));
        //creo al entrenador que al ser creado el repositorio deberia retornar
        when(repository.existsByRun("111-1")).thenReturn(false);
        //verificacion en falso por el run
        when(repository.save(any(Entrenador.class))).thenReturn(entrenador);
        //cuando en el repositorio se guarde cualquier Entrenador se
        //retorna al entrenador creado anteriormente
        EntrenadorResponse respuesta = service.add(eRequest);
        // se toma un entrenadorresponse que es lo que devuelve el service
        assertNotNull(respuesta);
        //comprueba solo que el objeto no es null
        assertEquals(1L,respuesta.getId());
        //aqui hace la comparacion del id y que coincidam
        assertEquals("tito",respuesta.getNombreCompleto());
        //mismo para nombre
        assertEquals("111-1",respuesta.getRun());
        //mismo para run
        assertEquals(LocalDate.parse("1999-08-15"),respuesta.getFechaNacimiento());
        //mismo para fecha
        verify(repository).save((any(Entrenador.class)));
        verify(repository).existsByRun("111-1");
    }
    @Test
    public void cuandoNoPuedeAgregarPorRunRepetido(){
        EntrenadorRequest eRequest = new EntrenadorRequest();
        eRequest.setNombreCompleto("tito");
        eRequest.setFechaNacimiento(LocalDate.parse("1992-02-13"));
        eRequest.setRun("111-1");
        //creo el request de entrenador
        when(repository.existsByRun("111-1")).thenReturn(true);
        //cuando se haga la verificacion del run retornara verdadero
        IllegalStateException excepcion = assertThrows(IllegalStateException.class,
                () -> service.add(eRequest));
        // hago la excepcion correcta con lo que deberia devolver
        assertEquals("Entrenador ya se encuentra registrado",excepcion.getMessage());
        verify(repository).existsByRun("111-1");

    }


    @Test
    public void puedoActualizarEntrenador(){
        Entrenador entrenador = new Entrenador(1L,"tito",
                "111-1",LocalDate.parse("1992-02-13"));
        EntrenadorRequest eRequest = new EntrenadorRequest();
        eRequest.setNombreCompleto("paco");
        eRequest.setFechaNacimiento(LocalDate.parse("1996-04-14"));
        eRequest.setRun("222-2");
        when(repository.findById(1L)).thenReturn(Optional.of(entrenador));
        when(repository.save(any(Entrenador.class))).
                thenAnswer(invocation
                        -> invocation.getArgument(0));

        EntrenadorResponse resultado = service.update(1L,eRequest);
       assertNotNull(resultado);
       assertEquals("paco",resultado.getNombreCompleto());
       assertEquals(LocalDate.parse("1996-04-14"),resultado.getFechaNacimiento());
       assertEquals("222-2",resultado.getRun());
       verify(repository).findById(1L);
    }

    @Test
    public void debeListarEntrenadores(){
        Entrenador e = new Entrenador(1L,"tito",
                "111-1",LocalDate.parse("1992-02-13"));

        when(repository.findAll()).thenReturn(List.of(e));
        List<EntrenadorResponse> resultado = service.getAll();
        assertFalse(resultado.isEmpty());
        assertEquals(1,resultado.size());
        assertEquals("tito",resultado.get(0).getNombreCompleto());
        verify(repository).findAll();
    }
    @Test
    public void debeBorrar(){
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);
        service.delete(1L);
        verify(repository).deleteById(1L);
        verify(repository).existsById(1L);
    }
    @Test
    public void noPuedoBorrar(){
        when(repository.existsById(1L)).thenReturn(false);
        EntityNotFoundException excepcion = assertThrows(EntityNotFoundException.class,
                ()->service.delete(1L));
        assertEquals("No se puede eliminar entrenador no encontrado",excepcion.getMessage());
        verify(repository).existsById(1L);
    }
    @Test
    public void retornarBusquedaPorRun(){
        Entrenador e = new Entrenador(1L,"tito",
                "111-1",LocalDate.parse("1992-02-13"));
        when(repository.findByRun("111-1")).thenReturn(Optional.of(e));
        EntrenadorResponse respuesta = service.buscarPorRun("111-1");
        assertNotNull(respuesta);
        assertEquals(1L,respuesta.getId());
        assertEquals("tito",respuesta.getNombreCompleto());
        assertEquals("111-1",respuesta.getRun());
        assertEquals(LocalDate.parse("1992-02-13"),respuesta.getFechaNacimiento());
        verify(repository).findByRun("111-1");
    }
    @Test
    public void cuandoNoEncuentroPorRun(){
        when(repository.findByRun("111-1")).thenReturn(Optional.empty());
        EntityNotFoundException excepcion = assertThrows(EntityNotFoundException.class,()-> service.buscarPorRun("111-1"));
        assertEquals("Entrenador no Encontrado",excepcion.getMessage());
        verify(repository).findByRun("111-1");
    }
}
