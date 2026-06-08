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

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {
    private final ClienteRepository repo;
    private final PlanesClient client;

    //metodo para agregar clientes
    private ClienteResponse add(ClienteRequest c, String token){
        log.info("Anadir Cliente", keyValue("cliente", c.getNombres()));//cuando se agrega un cliente se usa el log para poder dejar guardada la accion
        var plan = client.getPlan(c.getIdPlan(), token);
        //si es que el plan no existe, el metodo va a advertir de ello y no va a seguir
        if(plan == null){
            log.warn("Plan no existe",
                keyValue("idPlan",c.getIdPlan()));
            throw new EntityNotFoundException("Plan no encontrado");
        }
        //datos del cliente
        Cliente cliente1 = new Cliente();
        cliente1.setNombres(c.getNombres());
        cliente1.setApellidos(c.getApellidos());
        cliente1.setRun(c.getRun());
        cliente1.setCorreo(c.getCorreo());
        cliente1.setIdPlan(c.getIdPlan());
        Cliente saveCliente = repo.save(cliente1);
        log.info("Cliente creado correctamente",
                keyValue("idCliente", saveCliente.getId()));
        return mapToResponse(saveCliente,token);


    }



//mapToResponse sirve para que todo quede ordenado o mapeado para que cuando se retorne, muestre los datos del cliente
    private ClienteResponse mapToResponse(Cliente c, String token) {
        log.info("Mapeando cliente",
                keyValue("idCliente", c.getId())
        );
        var plan1 = client.getPlan(c.getIdPlan(), token);


        return ClienteResponse.builder().id(c.getId()).nombres(c.getNombres()).
                apellidos(c.getApellidos()).
                run(c.getRun()).
                correo(c.getCorreo()).
                idPlan(c.getIdPlan()).
                build();

    }
}
