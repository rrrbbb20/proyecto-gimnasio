package com.example.ms_encargado.service;

import com.example.ms_encargado.dto.EncargadoRequest;
import com.example.ms_encargado.dto.EncargadoResponse;
import com.example.ms_encargado.model.Encargado;
import com.example.ms_encargado.repository.EncargadoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.keyValue;


@Service
@RequiredArgsConstructor
@Slf4j
public class EncargadoService {

    private final EncargadoRepository repository;
    public EncargadoResponse add(EncargadoRequest request){
        log.info("Crear Encargado", keyValue("nombre", request.getNombreCompleto()));
        Encargado encargado1 = new Encargado();
        encargado1.setNombreCompleto(request.getNombreCompleto());
        encargado1.setRun(request.getRun());
        encargado1.setFechaNacimiento(request.getFechaNacimiento());
        encargado1.setDireccion(request.getDireccion());
        Encargado saveEncargado = repository.save(encargado1);
        log.info("Encargado creado correctamente",
                keyValue("id", saveEncargado.getId())
        );
        return mapToResponse(saveEncargado);


    }


    public EncargadoResponse findById(Long id){
        log.info("Obtener encargado", keyValue("id", id));
        Encargado encargado1 = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("encargado no encontrado"));
        log.info("Entrenador encargado",
                keyValue("id", encargado1.getId()),
                keyValue("nombre", encargado1.getNombreCompleto())
        );
        return mapToResponse(encargado1);
    }


    public List<EncargadoResponse> getAll(){
        log.info("Listando encargados");
        return repository.findAll()
                .stream()
                .map(e -> mapToResponse(e))
                .toList();

    }
    public EncargadoResponse update(Long id , EncargadoRequest e){

        Encargado encargado = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Encargado no Encontrado"));
        log.info("Actualizar Encargado", keyValue("id", id));
        encargado.setId(id);
        encargado.setNombreCompleto(e.getNombreCompleto());
        encargado.setRun(e.getRun());
        encargado.setFechaNacimiento(e.getFechaNacimiento());
        Encargado updateEncargado = repository.save(encargado);
        log.info("Encargado actualizado correctamente",
                keyValue("id", updateEncargado.getId())
        );

        return mapToResponse(updateEncargado);
    }

    public void delete(Long id){
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("No se puede eliminar encargado no encontrado");
        }
        log.info("Eliminar Encargado", keyValue("id", id));
        repository.deleteById(id);
        log.info("Encargado eliminado correctamente",
                keyValue("id", id));
    }

    private EncargadoResponse mapToResponse(Encargado e) {
        log.info("Mapeando encargador",
                keyValue("id", e.getId())
        );
        return EncargadoResponse.builder()
                .id(e.getId())
                .nombreCompleto(e.getNombreCompleto())
                .run(e.getRun())
                .fechaNacimiento(e.getFechaNacimiento())
                .direccion(e.getDireccion())
                .build();
    }

}
