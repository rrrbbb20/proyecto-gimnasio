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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class InscripcionClaseService {

    private final ClaseClient claseClient;
    private final ClienteClient clienteClient;
    private final InscripcionClaseRepository repository;

    public InscripcionClaseResponse add(InscripcionClaseRequest ir, String token){
        log.info("Creando inscripcion",
                keyValue("idClase", ir.getIdClase()),
                keyValue("idCliente", ir.getIdCliente()));
        verificarExistencia(ir,token);
        InscripcionClase insClase = new InscripcionClase();
        insClase.setFechaInscripcion(LocalDate.now());
        insClase.setHoraInscripcion(LocalTime.now());
        insClase.setIdClase(ir.getIdClase());
        insClase.setIdCliente(ir.getIdCliente());
        InscripcionClase claseResp = repository.save(insClase);
        log.info("Inscripción guardada",
                keyValue("idInscripcion", claseResp.getId())
        );
        claseClient.restarCupo(claseResp.getIdClase(),token);
        log.info("Cupo descontado",
                keyValue("idClase", claseResp.getIdClase()));

        return mapToResponse(claseResp,token);
    }

    public InscripcionClaseResponse findById(Long id,String token){
        log.info("Buscar inscripcion {}", keyValue("Id de la Inscripcion", id));
        InscripcionClase clase1= repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inscripcion a clase no encontrada"));
        log.info("Inscripcion encontrada{}", keyValue("Id de la Inscripcion", id));
        return mapToResponse(clase1,token);
    }
    public List<InscripcionClaseResponse> getAll(String token){
        log.info("Listando inscripciones");
        return repository.findAll().stream()
                .map(ic -> mapToResponse(ic,token))
                .toList();
    }

    /*public InscripcionClaseResponse update(Long id, InscripcionClaseRequest ir, String token ){
        verificarExistencia(ir,token);
        InscripcionClase insClase = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inscripcion a clase no encontrada"));

        insClase.setIdClase(ir.getIdClase());
        insClase.setIdCliente(ir.getIdCliente());
        return mapToResponse(repository.save(insClase),token);
    }*/

    public void delete(Long id,String token){
        log.info("Eliminando inscripción",
                keyValue("idInscripcion", id));
        if (!repository.existsById(id)) {
            log.warn("Intento de eliminar inscripcion inexistente",
                    keyValue("idInscripcion", id));

            throw new EntityNotFoundException("No se puede eliminar inscripcion no encontrada");
        }
        InscripcionClase clase = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inscripcion a clase no encontrada"));
        claseClient.sumarCupo(clase.getIdClase(),token);
        log.info("Cupo restaurado",
                keyValue("idClase", clase.getIdClase()));
        repository.deleteById(id);

    }

    private InscripcionClaseResponse mapToResponse(InscripcionClase ic, String token) {
        log.info("Mapeando inscripcion",
                keyValue("idInscripcion", ic.getId()));
        var insClase = claseClient.getClase(ic.getIdClase(), token);

        var cliente = clienteClient.getCliente(ic.getIdCliente(), token);
        return InscripcionClaseResponse.builder()
                .idInscripcion(ic.getId())
                .clase(insClase)
                .cliente(cliente)
                .fechaInscripcion(ic.getFechaInscripcion())
                .horaInscripcion(ic.getHoraInscripcion())
                .build();
    }



    public void verificarExistencia(InscripcionClaseRequest ir ,String token){
        log.info("Verificando existencia de clase y cliente",
                keyValue("idClase", ir.getIdClase()),
                keyValue("idCliente", ir.getIdCliente()));
        ClaseResponse claseResponse1  = claseClient.getClase(ir.getIdClase(), token);
        if (claseResponse1 == null) {
            log.warn("Clase no encontrada",
                    keyValue("idClase", ir.getIdClase()));
            throw new EntityNotFoundException("Clase no encontrada");
        }
        ClienteResponse clienteResponse1  = clienteClient.getCliente(ir.getIdCliente(), token);

        if (clienteResponse1 == null) {
            log.warn("Cliente no encontrado",
                    keyValue("idCliente", ir.getIdCliente()));
            throw new EntityNotFoundException("Cliente no encontrado");
        }
        log.info("Validacion completada correctamente");
    }


    public List<ClaseResponse> buscarClasePorNombre(String nombre , String token){
        log.info("Buscando clases por nombre ",keyValue("nombre:",nombre));
        List<ClaseResponse> clase = claseClient.buscarPorNombre(nombre,token);
        if (clase.isEmpty()){
            log.info("No se encontraron clases por ese nombre",keyValue("nombre:",nombre));
            throw new EntityNotFoundException("No se encontro la clase que buscabas");
        }
        log.info("se encontraron clases por ese nombre",keyValue("nombre:",nombre));
        return clase;
    }
}
