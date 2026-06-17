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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {
    private final ClienteRepository repo;
    private final PlanesClient client;

    @Transactional
    public ClienteResponse add(ClienteRequest c){
        log.info("Añadir Cliente e iniciar proceso de suscripción", keyValue("cliente", c.getNombres()));

        var plan = client.getPlan(c.getIdPlan());
        if(plan == null){
            log.warn("Plan no existe en el catálogo", keyValue("idPlan", c.getIdPlan()));
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
        log.info("Cliente guardado en BD local", keyValue("idCliente", saveCliente.getId()));

        try {
            Map<String, Object> suscripcionPayload = Map.of(
                    "idCliente", saveCliente.getId(),
                    "idPlan", c.getIdPlan(),
                    "pago", c.getPago()
            );


            client.activarSuscripcion(suscripcionPayload);
            log.info("Suscripción y pago procesados con éxito en microservicio remoto");

        } catch (Exception e) {
            log.error("Error crítico: No se pudo activar la suscripción o el pago fue rechazado", e);

            throw new RuntimeException("El registro del cliente ha sido cancelado porque el método de pago falló o no pudo ser procesado.");
        }

        ClienteResponse response = mapToResponse(saveCliente);
        response.setDetallesPlan(plan);
        return response;
    }


    public ClienteResponse findById(Long id){
        Cliente cliente = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        ClienteResponse response = mapToResponse(cliente);
        try {
            response.setDetallesPlan(client.getPlan(cliente.getIdPlan()));
        } catch (Exception e) {
            log.error("Error al traer plan", e);
        }
        return response;
    }

    public List<ClienteResponse> getAll(){
        return repo.findAll().stream()
                .map(cliente -> {
                    ClienteResponse res = mapToResponse(cliente);
                    try { res.setDetallesPlan(client.getPlan(cliente.getIdPlan())); } catch (Exception e) {}
                    return res;
                }).toList();
    }

    public ClienteResponse update(Long id, ClienteRequest c){
        Cliente cliente1 = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        var plan = client.getPlan(c.getIdPlan());
        if(plan == null) throw new EntityNotFoundException("Plan no encontrado");

        cliente1.setNombres(c.getNombres());
        cliente1.setApellidos(c.getApellidos());
        cliente1.setRun(c.getRun());
        cliente1.setCorreo(c.getCorreo());
        cliente1.setIdPlan(c.getIdPlan());
        cliente1.setFechaNac(c.getFechaNac());

        Cliente updateCliente = repo.save(cliente1);
        ClienteResponse response = mapToResponse(updateCliente);
        response.setDetallesPlan(plan);
        return response;
    }

    public void delete(Long id){
        if(!repo.existsById(id)) throw new EntityNotFoundException("Cliente no encontrado");
        repo.deleteById(id);
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