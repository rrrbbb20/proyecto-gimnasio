package com.example.ms_inventario.service;


import com.example.ms_inventario.client.MantenimientoClient;
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
    private final MantenimientoClient mantenimientoClient;


    public InventarioResponse add(InventarioRequest request, String token) {

        log.info("Agregar equipamiento al inventario", keyValue("nombre", request.getNombre()));

        var mantenimiento = mantenimientoClient.obtenerMantenimiento(request.getIdMantenimiento(), token);

        if (mantenimiento == null) {
            throw new RuntimeException("mantenimiento no existe");
        }

        Inventario inventario = repository.save(
                new Inventario(null,request.getNombre(),request.getDescripcion(),request.getPrecio()
                ,request.getFechaRegistro(),request.getIdMantenimiento())
        );

        return mapToResponse(inventario, token);


    }

    public InventarioResponse findById(Long id, String token) {
        log.info("Obtener Inventario", keyValue("id", id));
        Inventario inventario1 = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado"));
        return mapToResponse(inventario1, token);
    }

    public List<InventarioResponse> getAll(String token){
        return repository.findAll()
                .stream()
                .map(i -> mapToResponse(i,token))
                .toList();

    }
    public InventarioResponse update(Long id , InventarioRequest i, String token) {
        var mantenimiento = mantenimientoClient.obtenerMantenimiento(i.getIdMantenimiento(), token);
        if (mantenimiento == null) {
            throw new RuntimeException("mantenimiento no existe");
        }
        Inventario inventario1 = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no Encontrado"));;
        log.info("Actualizar Inventario", keyValue("id", id));
        inventario1.setNombre(i.getNombre());
        inventario1.setDescripcion(i.getDescripcion());
        inventario1.setPrecio(i.getPrecio());
        inventario1.setFechaRegistro(i.getFechaRegistro());
        inventario1.setIdMantenimiento(i.getIdMantenimiento());
        return mapToResponse(repository.save(inventario1), token);
    }

    public void delete(Long id){
        log.info("Eliminar Inventario", keyValue("id", id));
        repository.deleteById(id);
    }

    private InventarioResponse mapToResponse(Inventario i, String token) {
        var mantenimiento = mantenimientoClient.obtenerMantenimiento(i.getId(), token);
        return InventarioResponse.builder()
                .id(i.getId())
                .nombre(i.getNombre())
                .descripcion(i.getDescripcion())
                .precio(i.getPrecio())
                .fechaRegistro(i.getFechaRegistro())
                .infoMantenimiento(mantenimiento)
                .build();
    }
}
