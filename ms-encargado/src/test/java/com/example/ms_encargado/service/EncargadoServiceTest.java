package com.example.ms_encargado.service;

import com.example.ms_encargado.dto.EncargadoRequest;
import com.example.ms_encargado.dto.EncargadoResponse;
import com.example.ms_encargado.model.Encargado;
import com.example.ms_encargado.repository.EncargadoRepository;
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
public class EncargadoServiceTest {


    //Creacionde repo falso
    @Mock
    private EncargadoRepository repository;

    //Instanciamos e inyectamos el mock
    @InjectMocks
    private EncargadoService service;

    @Test
    void cuandoAgregaEncargado() {

        //Simula la informacion que llegaria desde controller
        EncargadoRequest request = new EncargadoRequest();
        request.setNombreCompleto("Juan Diaz");
        request.setRun("111-1");
        request.setDireccion("Santiago");
        request.setFechaNacimiento(
                LocalDate.parse("1992-02-13"));
        //{
        //    "nombreCompleto": "Juan Diaz",
        //        "run": "111-1",
        //        "direccion": "Santiago",
        //        "fechaNacimiento": "1992-02-13"
        //}


        //creo el objeto que devolverá el repositorio
        Encargado encargado = new Encargado(
                1L,
                "Juan Diaz",
                "111-1",
                "Santiago",
                LocalDate.parse("1992-02-13")
        );

        when(repository.save(any(Encargado.class)))
                .thenReturn(encargado);


        //aqui se ejecuta el código del servicio.
        EncargadoResponse respuesta =
                service.add(request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals("Juan Diaz",
                respuesta.getNombreCompleto());
        assertEquals("111-1",
                respuesta.getRun());
        assertEquals("Santiago",
                respuesta.getDireccion());
        assertEquals(
                LocalDate.parse("1992-02-13"),
                respuesta.getFechaNacimiento());
        verify(repository)
                .save(any(Encargado.class));
    }

    @Test
    void retornarEncargadoCuandoExiste() {

        Encargado encargado = new Encargado(
                1L,
                "Juan Diaz",
                "111-1",
                "Santiago",
                LocalDate.parse("1992-02-13")
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(encargado));

        EncargadoResponse respuesta =
                service.findById(1L, null);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals("Juan Diaz",
                respuesta.getNombreCompleto());
        assertEquals("111-1",
                respuesta.getRun());
        assertEquals("Santiago",
                respuesta.getDireccion());

        verify(repository).findById(1L);
    }

    @Test
    void cuandoEncargadoNoExiste() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        EntityNotFoundException excepcion =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> service.findById(1L, null));

        assertEquals(
                "encargado no encontrado",
                excepcion.getMessage());

        verify(repository).findById(1L);
    }

    @Test
    void puedoActualizarEncargado() {

        Encargado encargado = new Encargado(
                1L,
                "Juan Diaz",
                "111-1",
                "Santiago",
                LocalDate.parse("1992-02-13")
        );

        EncargadoRequest request =
                new EncargadoRequest();

        request.setNombreCompleto("Pedro Diaz");
        request.setRun("222-2");
        request.setDireccion("Valparaiso");
        request.setFechaNacimiento(
                LocalDate.parse("1996-04-14"));

        when(repository.findById(1L))
                .thenReturn(Optional.of(encargado));

        when(repository.save(any(Encargado.class)))
                .thenAnswer(
                        invocation ->
                                invocation.getArgument(0));

        EncargadoResponse resultado =
                service.update(
                        1L,
                        request,
                        null);

        assertNotNull(resultado);
        assertEquals(
                "Pedro Diaz",
                resultado.getNombreCompleto());
        assertEquals(
                "222-2",
                resultado.getRun());
        assertEquals(
                "Valparaiso",
                resultado.getDireccion());
        assertEquals(
                LocalDate.parse("1996-04-14"),
                resultado.getFechaNacimiento());

        verify(repository).findById(1L);
        verify(repository)
                .save(any(Encargado.class));
    }

    @Test
    void debeListarEncargados() {

        Encargado encargado = new Encargado(
                1L,
                "Juan Diaz",
                "111-1",
                "Santiago",
                LocalDate.parse("1992-02-13")
        );

        when(repository.findAll())
                .thenReturn(List.of(encargado));

        List<EncargadoResponse> resultado =
                service.getAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(
                "Juan Diaz",
                resultado.get(0)
                        .getNombreCompleto());

        verify(repository).findAll();
    }

    @Test
    void debeBorrar() {

        when(repository.existsById(1L))
                .thenReturn(true);

        doNothing()
                .when(repository)
                .deleteById(1L);

        service.delete(1L);

        verify(repository)
                .existsById(1L);

        verify(repository)
                .deleteById(1L);
    }

    @Test
    void noPuedoBorrar() {

        when(repository.existsById(1L))
                .thenReturn(false);

        EntityNotFoundException excepcion =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> service.delete(1L));

        assertEquals(
                "No se puede eliminar encargado no encontrado",
                excepcion.getMessage());

        verify(repository)
                .existsById(1L);
    }

}
