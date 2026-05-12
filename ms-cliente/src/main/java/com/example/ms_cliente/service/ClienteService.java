package com.example.ms_cliente.service;

import com.example.ms_cliente.dto.ClienteRequest;
import com.example.ms_cliente.dto.ClienteResponse;
import com.example.ms_cliente.model.Cliente;
import com.example.ms_cliente.repository.ClienteRepository;
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
    private final ClienteRepository clienteRepository;

    public ClienteResponse add (ClienteRequest request){
        log.info ("Crear Cliente", keyValue("nombre", request.getNombreCompletoCliente()));
        Cliente cliente1 = new Cliente();
        cliente1.setNombreCompletoCliente(request.getNombreCompletoCliente());
        cliente1.setRun(request.getRun());
        cliente1.setFechaNacCliente(request.getFechaNacCliente());
        cliente1.setTipoPlan(request.getTipoPlan());
        clienteRepository.save(cliente1);
        return mapToResponse(cliente1);
    }
    public ClienteResponse findById(Long id){
        log.info("Obtener id de Cliente", keyValue("id", id));
        Cliente cliente1 = clienteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente no Existe"));
        return mapToResponse(cliente1);
    }
    public List<ClienteResponse> getAll(){
        return clienteRepository.findAll().stream().map(cliente -> mapToResponse(cliente)).toList();
    }
    public ClienteResponse update(Long id, ClienteRequest c){
        Cliente cliente1 = clienteRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("No se ha encontrado el cliente"));
        log.info("Actualizar Cliente", keyValue("id", id));
        cliente1.setId(id);
        cliente1.setNombreCompletoCliente(c.getNombreCompletoCliente());
        cliente1.setRun(c.getRun());
        cliente1.setTipoPlan(c.getTipoPlan());
        cliente1.setFechaNacCliente(c.getFechaNacCliente());
        clienteRepository.save(cliente1);
        return mapToResponse(cliente1);
    }
    public void delete(Long id){
        log.info("Eliminar cliente",keyValue("id", id));
        clienteRepository.deleteById(id);
    }

    private ClienteResponse mapToResponse(Cliente c) {
        return ClienteResponse.builder()
                .id(c.getId()) //
                .nombreCompletoCliente(c.getNombreCompletoCliente())
                .run(c.getRun())
                .fechaNacCliente(c.getFechaNacCliente())
                .build();
    }

}
