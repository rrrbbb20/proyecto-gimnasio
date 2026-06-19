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

        // 1. Obtener el plan base del catálogo
        var plan = client.getPlan(c.getIdPlan());
        if(plan == null){
            throw new EntityNotFoundException("Plan no encontrado");
        }

        // 2. Guardar el cliente para obtener su ID
        Cliente cliente1 = new Cliente();
        cliente1.setNombres(c.getNombres());
        cliente1.setApellidos(c.getApellidos());
        cliente1.setRun(c.getRun());
        cliente1.setCorreo(c.getCorreo());
        cliente1.setIdPlan(c.getIdPlan());
        cliente1.setFechaNac(c.getFechaNac());
        Cliente clienteGuardado = repo.save(cliente1);

        // 3. Crear la suscripción y enviar los datos de pago a ms-planes
        Map<String, Object> suscripcionRequest = Map.of(
                "idCliente", clienteGuardado.getId(),
                "idPlan", clienteGuardado.getIdPlan(),
                "pago", c.getPago()
        );

        try {
            // Llamamos a ms-planes. Este endpoint devuelve un ApiResponse con la SuscripcionResponse completa
            Object respuestaSuscripcion = client.activarSuscripcion(suscripcionRequest);

            // Extraemos de forma segura el objeto de pago que generó ms-planes
            if (respuestaSuscripcion instanceof Map) {
                Map<?, ?> body = (Map<?, ?>) respuestaSuscripcion;
                Map<?, ?> data = (Map<?, ?>) body.get("data"); // Entramos al "data" del ApiResponse

                if (data != null && data.get("pago") != null) {
                    plan.setIdPago(data.get("pago"));
                }
            }
        } catch (Exception e) {
            log.error("Error al activar la suscripción o recuperar el pago", e);
            throw new RuntimeException("Error de red o pago rechazado", e);
        }

        // 4. Mapear y responder
        ClienteResponse response = mapToResponse(clienteGuardado);
        response.setDetallesPlan(plan); // 'plan' ahora lleva el objeto pago enriquecido
        return response;
    }


    public ClienteResponse findById(Long id){
        Cliente c = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        ClienteResponse response = mapToResponse(c);

        var plan = client.getPlan(c.getIdPlan());

        if (plan != null) {
            try {
                // Buscamos la suscripción activa asociada a este cliente específico
                Object respuestaSuscripcion = client.getSuscripcionPorCliente(c.getId());
                if (respuestaSuscripcion instanceof Map) {
                    Map<?, ?> body = (Map<?, ?>) respuestaSuscripcion;
                    Map<?, ?> data = (Map<?, ?>) body.get("data");

                    if (data != null && data.get("pago") != null) {
                        // Seteamos el objeto de pago recuperado dentro de los detalles del plan
                        plan.setIdPago(data.get("pago"));
                    }
                }
            } catch (Exception e) {
                log.error("Error al traer el pago de la suscripción para el cliente {}", c.getId(), e);
            }
            response.setDetallesPlan(plan);
        }

        return response;
    }
    public List<ClienteResponse> getAll(){
        return repo.findAll().stream()
                .map(cliente -> {
                    ClienteResponse res = mapToResponse(cliente);

                    // 1. Obtener datos estáticos del catálogo
                    var plan = client.getPlan(cliente.getIdPlan());

                    if (plan != null) {
                        // 2. Consultar el nuevo endpoint que acabamos de crear en ms-planes
                        Object respuestaSuscripcion = client.getSuscripcionPorCliente(cliente.getId());

                        if (respuestaSuscripcion instanceof Map) {
                            Map<?, ?> body = (Map<?, ?>) respuestaSuscripcion;
                            Map<?, ?> data = (Map<?, ?>) body.get("data"); // Entramos al "data" de la ApiResponse

                            if (data != null && data.get("pago") != null) {
                                // Seteamos el objeto de pago completo que tiene el id_pago de la BD
                                plan.setIdPago(data.get("pago"));
                            }
                        }
                        res.setDetallesPlan(plan);
                    }
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
        if (plan != null) {
            try {
                Object respuestaSuscripcion = client.getSuscripcionPorCliente(updateCliente.getId());
                if (respuestaSuscripcion instanceof Map) {
                    Map<?, ?> body = (Map<?, ?>) respuestaSuscripcion;
                    Map<?, ?> data = (Map<?, ?>) body.get("data"); // Entramos al "data" de la ApiResponse

                    if (data != null && data.get("pago") != null) {
                        // Seteamos el objeto de pago completo que tiene el id de la BD
                        plan.setIdPago(data.get("pago"));
                    }
                }
            } catch (Exception e) {
                log.error("Error al traer el pago de la suscripción en el update", e);
            }
        }
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