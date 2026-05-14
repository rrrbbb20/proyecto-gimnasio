package com.example.ms_mantenimiento.service;

import com.example.ms_mantenimiento.dto.MantenimientoRequest;
import com.example.ms_mantenimiento.dto.MantenimientoResponse;
import com.example.ms_mantenimiento.model.Mantenimiento;
import com.example.ms_mantenimiento.repository.MantenimientoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class MantenimientoService {

    private final MantenimientoRepository repository;


    public MantenimientoResponse add(MantenimientoRequest request){

        log.info("Agregar informaciond de mantenimiento", keyValue("nombre de la empresa", request.getEmpresa()));

        Mantenimiento mantenimiento1 = new Mantenimiento();

        mantenimiento1.setEmpresa(request.getEmpresa());
        mantenimiento1.setDescripcionMantenimiento(request.getDescripcionMantenimiento());
        mantenimiento1.setPrecio(request.getPrecio());
        mantenimiento1.setFechaMantenimiento(request.getFechaMantenimiento());

        Mantenimiento guardado = repository.save(mantenimiento1);

        return mapToResponse(guardado);

    }

    public MantenimientoResponse findById(Long id){
        log.info("Obtener Mantenimiento", keyValue("id", id));
        Mantenimiento mantenimiento1 = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mantenimiento no encontrado"));
        return mapToResponse(mantenimiento1);
    }

    public List<MantenimientoResponse> getAll(){
        return repository.findAll()
                .stream()
                .map(m -> mapToResponse(m))
                .toList();

    }
    public MantenimientoResponse update(Long id , MantenimientoRequest request){
        Mantenimiento mantenimiento1 = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entrenador no Encontrado"));;
        log.info("Actualizar Entrenador", keyValue("id", id));
        mantenimiento1.setEmpresa(request.getEmpresa());
        mantenimiento1.setDescripcionMantenimiento(request.getDescripcionMantenimiento());
        mantenimiento1.setPrecio(request.getPrecio());
        mantenimiento1.setFechaMantenimiento(request.getFechaMantenimiento());
        repository.save(mantenimiento1);
        return mapToResponse(mantenimiento1);
    }

    public void delete(Long id){
        log.info("Eliminar Entrenador", keyValue("id", id));
        repository.deleteById(id);
    }

    private MantenimientoResponse mapToResponse(Mantenimiento m) {
        return MantenimientoResponse.builder()
                .id(m.getId())
                .empresa(m.getEmpresa())
                .descripcionMantenimiento(m.getDescripcionMantenimiento())
                .fechaMantenimiento(m.getFechaMantenimiento())
                .precio(m.getPrecio())
                .build();
    }
}
