package com.example.ms_inventario.service;


import com.example.ms_inventario.dto.InventarioRequest;
import com.example.ms_inventario.dto.InventarioResponse;
import com.example.ms_inventario.model.Inventario;
import com.example.ms_inventario.repository.InventarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventarioService {

    private final InventarioRepository repository;


    public InventarioResponse add(InventarioRequest request){

        log.info("Agregar equipamiento al inventario", keyValue("nombre", request.getNombre()));

        Inventario inventario1 = new Inventario();

        inventario1.setNombre(request.getNombre());
        inventario1.setDescripcion(request.getDescripcion());
        inventario1.setPrecio(request.getPrecio());
        inventario1.setFechaRegistro(request.getFechaRegistro());

        Inventario guardado = repository.save(inventario1);

        return mapToResponse(guardado);


    }

    public InventarioResponse findById(Long id){
        log.info("Obtener Entrenador", keyValue("id", id));
        Inventario inventario1 = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entrenador no encontrado"));
        return mapToResponse(inventario1);
    }

    public List<InventarioResponse> getAll(){
        return repository.findAll()
                .stream()
                .map(e -> mapToResponse(e))
                .toList();

    }
    public InventarioResponse update(Long id , InventarioRequest i){
        Inventario inventario1 = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entrenador no Encontrado"));;
        log.info("Actualizar Entrenador", keyValue("id", id));
        inventario1.setNombre(i.getNombre());
        inventario1.setDescripcion(i.getDescripcion());
        inventario1.setPrecio(i.getPrecio());
        inventario1.setFechaRegistro(i.getFechaRegistro());
        repository.save(inventario1);
        return mapToResponse(inventario1);
    }

    public void delete(Long id){
        log.info("Eliminar Entrenador", keyValue("id", id));
        repository.deleteById(id);
    }

    private InventarioResponse mapToResponse(Inventario i) {
        return InventarioResponse.builder()
                .id(i.getId())
                .nombre(i.getNombre())
                .descripcion(i.getDescripcion())
                .precio(i.getPrecio())
                .fechaRegistro(i.getFechaRegistro())
                .build();
    }
}
