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

        log.info("Crear Mantenimiento", keyValue("empresa", request.getEmpresa()));

        Mantenimiento mantenimiento1 = new Mantenimiento();

        mantenimiento1.setEmpresa(request.getEmpresa());
        mantenimiento1.setDescripcionMantenimiento(request.getDescripcionMantenimiento());
        mantenimiento1.setPrecio(request.getPrecio());
        mantenimiento1.setFechaMantenimiento(request.getFechaMantenimiento());

        Mantenimiento guardado = repository.save(mantenimiento1);

        log.info("Mantenimiento creado correctamente",
                keyValue("id", guardado.getId())
        );

        return mapToResponse(guardado);
    }

    public MantenimientoResponse findById(Long id){
        log.info("Obtener mantenimiento", keyValue("id", id));
        Mantenimiento mantenimiento1 = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mantenimiento no encontrado"));
        log.info("Mantenimiento encontrado",
                keyValue("id", mantenimiento1.getId()),
                keyValue("empresa", mantenimiento1.getEmpresa())
        );
        return mapToResponse(mantenimiento1);
    }

    public List<MantenimientoResponse> getAll(){
        log.info("Listando mantenimientos");
        return repository.findAll()
                .stream()
                .map(m -> mapToResponse(m))
                .toList();
    }

    public MantenimientoResponse update(Long id, MantenimientoRequest request){

        Mantenimiento mantenimiento1 = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mantenimiento no encontrado"));

        log.info("Actualizar Mantenimiento", keyValue("id", id));

        mantenimiento1.setEmpresa(request.getEmpresa());
        mantenimiento1.setDescripcionMantenimiento(request.getDescripcionMantenimiento());
        mantenimiento1.setPrecio(request.getPrecio());
        mantenimiento1.setFechaMantenimiento(request.getFechaMantenimiento());

        Mantenimiento actualizado = repository.save(mantenimiento1);

        return mapToResponse(actualizado);
    }

    public void delete(Long id){
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("No se puede eliminar mantenimiento no encontrado");
        }
        log.info("Eliminar Mantenimiento", keyValue("id", id));
        repository.deleteById(id);
        log.info("Mantenimiento eliminado correctamente",
                keyValue("id", id));
    }

    private MantenimientoResponse mapToResponse(Mantenimiento m) {
        log.info("Mapeando mantenimiento",
                keyValue("id", m.getId())
        );
        return MantenimientoResponse.builder()
                .id(m.getId())
                .empresa(m.getEmpresa())
                .descripcionMantenimiento(m.getDescripcionMantenimiento())
                .fechaMantenimiento(m.getFechaMantenimiento())
                .precio(m.getPrecio())
                .build();
    }
}
