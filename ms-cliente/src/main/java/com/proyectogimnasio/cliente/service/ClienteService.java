package com.proyectogimnasio.cliente.service;

import com.proyectogimnasio.cliente.client.PlanesClient;
import com.proyectogimnasio.cliente.dto.ClienteRequest;
import com.proyectogimnasio.cliente.dto.ClienteResponse;
import com.proyectogimnasio.cliente.model.Cliente;
import com.proyectogimnasio.cliente.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {
    private final ClienteRepository repo;
    private final PlanesClient client;


    public ClienteResponse add(ClienteRequest c, String token){
        log.info("Anadir Cliente", keyValue("cliente", c.getNombres()));
        var plan = client.getPlan(c.getIdPlan(), token);

        if(plan == null){
            log.warn("Plan no existe", keyValue("idPlan", c.getIdPlan()));
            throw new EntityNotFoundException("Plan no encontrado");
        }

        Cliente cliente1 = new Cliente();
        cliente1.setNombres(c.getNombres());
        cliente1.setApellidos(c.getApellidos());
        cliente1.setRun(c.getRun());
        cliente1.setCorreo(c.getCorreo());
        cliente1.setIdPlan(c.getIdPlan());
        cliente1.setFechaNac(c.getFechaNac());

        Cliente saveCliente = repo.save(cliente1);
        log.info("Cliente creado correctamente", keyValue("idCliente", saveCliente.getId()));
        ClienteResponse response = mapToResponse(saveCliente);
        response.setDetallesPlan(plan);
        return response;
    }

    public ClienteResponse findById(Long id, String token) {
        log.info("Buscar cliente", keyValue("idCliente", id));
        Cliente cliente = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));


        ClienteResponse response = mapToResponse(cliente);

        try {
            var plan = client.getPlan(cliente.getIdPlan(), token);
            response.setDetallesPlan(plan);
        } catch (Exception e) {
            log.error("No se pudo obtener el detalle del plan desde el microservicio", e);
        }

        return response;
    }

    public List<ClienteResponse> getAll(String token){
        log.info("Listando clientes");
        return repo.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ClienteResponse update(Long id, ClienteRequest c, String token){
        log.info("Actualizando Cliente", keyValue("idCliente", id));
        Cliente cliente1 = repo.findById(id).orElseThrow(()->new EntityNotFoundException("Cliente no encontrado"));
        var plan = client.getPlan(c.getIdPlan(), token);

        if(plan == null){
            log.warn("Plan no encontrado", keyValue("idPlan", c.getIdPlan()));
            throw new EntityNotFoundException("Plan no encontrado");
        }

        cliente1.setNombres(c.getNombres());
        cliente1.setApellidos(c.getApellidos());
        cliente1.setRun(c.getRun());
        cliente1.setCorreo(c.getCorreo());
        cliente1.setIdPlan(c.getIdPlan());
        cliente1.setFechaNac(c.getFechaNac());

        Cliente updateCliente = repo.save(cliente1);
        log.info("Cliente actualizado correctamente", keyValue("idCliente", updateCliente.getId()));
        return mapToResponse(updateCliente);
    }

    public void delete(Long id){
        log.info("Eliminando cliente", keyValue("idCliente", id));
        if(!repo.existsById(id)){
            log.warn("Cliente a eliminar inexistente", keyValue("idCliente", id));
            throw new EntityNotFoundException("No se puede eliminar un cliente nonexistent");
        }
        repo.deleteById(id);
        log.info("Cliente eliminado correctamente", keyValue("idCliente", id));
    }
    private ClienteResponse mapToResponse(Cliente c) {
        return ClienteResponse.builder()
                .id(c.getId())
                .nombres(c.getNombres())
                .apellidos(c.getApellidos())
                .run(c.getRun())
                .correo(c.getCorreo())
                .idPlan(c.getIdPlan())
                .fechaNac(c.getFechaNac())
                .build();
    }

}