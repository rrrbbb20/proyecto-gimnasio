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

    //metodo para agregar clientes
    public ClienteResponse add(ClienteRequest c, String token){
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
        cliente1.setFechaNac(c.getFechaNac());
        Cliente saveCliente = repo.save(cliente1);
        log.info("Cliente creado correctamente",
                keyValue("idCliente", saveCliente.getId()));
        return mapToResponse(saveCliente,token);


    }
    public ClienteResponse findById(Long id, String token){
        log.info("Buscar cliente",
                keyValue("idCliente", id));
        Cliente cliente = repo.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Cliente no encontrado"));
        log.info("Cliente encontrado",
                keyValue("Nombres", cliente.getNombres()));
        return mapToResponse(cliente,token);
    }
    public List<ClienteResponse> getAll(String token){
        log.info("Listando clientes");
        //esto es para que retorne todos los clientes en base a la estructura del maptoresponse
        return repo.findAll().stream()
                .map(c -> mapToResponse(c,token))
                .toList();
    }
    public ClienteResponse update(Long id, ClienteRequest c, String token){
        log.info("Actualizando Cliente",
                keyValue("idCliente", id));
        //aca solo verifica que el cliente ya existe para poder actualizarlo
        Cliente cliente1 = repo.findById(id).orElseThrow(()->new EntityNotFoundException("Cliente no encontrado"));
        var plan = client.getPlan(c.getIdPlan(),token);

        if(plan == null){
            log.warn("Plan no encontrado",
                    keyValue("idPlan", c.getIdPlan()));
            throw new EntityNotFoundException("Plan no encontrado");
        }
        cliente1.setNombres(c.getNombres());
        cliente1.setApellidos(c.getApellidos());
        cliente1.setRun(c.getRun());
        cliente1.setCorreo(c.getCorreo());
        cliente1.setIdPlan(c.getIdPlan());
        cliente1.setFechaNac(c.getFechaNac());
        Cliente updateCliente = repo.save(cliente1);
        log.info("Cliente actualizado correctamente",
                keyValue("idCliente", updateCliente.getId()));
        return mapToResponse(updateCliente,token);

    }
    public void delete(Long id){
        log.info("Eliminando cliente",
                keyValue("idCliente", id));
        //esto es una validacion de que realmente existe el cliente que se quiere eliminar
        if(!repo.existsById(id)){
            log.warn("Cliente a eliminar inexistente",
                    keyValue("idCliente", id));
            throw new EntityNotFoundException("No se puede eliminar un cliente inexistente");
        }
        repo.deleteById(id);
        log.info("Cliente eliminado correctamente",
                keyValue("idClase",id));
    }



    //El maptoresponse sirve para establecer una estructura para cuando se retorne los datos del modelo, en este caso cliente
    private ClienteResponse mapToResponse(Cliente c, String token) {
        log.info("Mapeando cliente",
                keyValue("idCliente", c.getId())
        );
        //llama al client para conseguir la id del plan
        var plan1 = client.getPlan(c.getIdPlan(), token);


        return ClienteResponse.builder().id(c.getId()).nombres(c.getNombres()).
                apellidos(c.getApellidos()).
                run(c.getRun()).
                correo(c.getCorreo()).
                idPlan(c.getIdPlan()).
                build();

    }
}
