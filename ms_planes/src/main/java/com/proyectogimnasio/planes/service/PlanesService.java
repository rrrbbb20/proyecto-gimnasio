package com.proyectogimnasio.planes.service;

import com.proyectogimnasio.planes.client.ClienteClient;
import com.proyectogimnasio.planes.dto.*;
import com.proyectogimnasio.planes.model.Pagos;
import com.proyectogimnasio.planes.model.Planes;
import com.proyectogimnasio.planes.repository.PagosRepository;
import com.proyectogimnasio.planes.repository.PlanesRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanesService {
    private final PlanesRepository planesRepository;
    private final PagosRepository pagosRepository;
    private final ClienteClient client;

    public PlanesResponse addPlan(PlanesRequest p, String token) {
        log.info("Crear Planes", keyValue("nombre", p.getNombrePlan()));
        Pagos pago = pagosRepository.findById(p.getIdPago())
                .orElseThrow(() -> new EntityNotFoundException("El método de pago especificado no existe"));

        Planes planes1 = new Planes();
        planes1.setNombrePlan(p.getNombrePlan());
        planes1.setPrecioPlan(p.getPrecioPlan());
        planes1.setDescripcionPlan(p.getDescripcionPlan());
        planes1.setBeneficios(p.getBeneficios());
        planes1.setIdPago(pago);

        Planes savePlan = planesRepository.save(planes1);
        return mapToResponsePlan(savePlan, token);
    }

    public PagosResponse addPago(PagosRequest pa, String token) {
        log.info("Crear Pago", keyValue("tipo", pa.getTipoPago()));


        var cliente = client.getCliente(pa.getIdCliente(), token);
        if (cliente == null) {
            log.warn("Cliente no existe", keyValue("idCliente", pa.getIdCliente()));
            throw new EntityNotFoundException("El cliente especificado no existe en el sistema");
        }

        Pagos pagos1 = new Pagos();
        pagos1.setTipoPago(pa.getTipoPago());
        pagos1.setNumTarjeta(pa.getNumTarjeta());
        pagos1.setFechaVencimiento(pa.getFechaVencimiento());
        pagos1.setCvc(pa.getCvc());
        pagos1.setDireccionFacturacion(pa.getDireccionFacturacion());
        pagos1.setCodigoPostal(pa.getCodigoPostal());
        pagos1.setIdCliente(pa.getIdCliente());

        Pagos savePago = pagosRepository.save(pagos1);
        log.info("Metodo de pago creado correctamente", keyValue("id", savePago.getId()));
        return mapToResponsePago(savePago, token);
    }

    public PlanesResponse findByIdPlan(Long id, String token) {
        log.info("Obtener id de Planes", keyValue("id", id));
        Planes planes1 = planesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Plan no Existe"));
        return mapToResponsePlan(planes1, token);
    }

    public PagosResponse findByIdPago(Long id, String token) {
        log.info("Buscar metodo de pago", keyValue("idPago", id));
        Pagos pago = pagosRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Metodo de pago no encontrado"));
        return mapToResponsePago(pago, token);
    }

    public List<PlanesResponse> getAllPlanes(String token) {
        return planesRepository.findAll().stream().map(planes -> mapToResponsePlan(planes, token)).toList();
    }

    public List<PagosResponse> getAllPagos(String token) {
        return pagosRepository.findAll().stream().map(pagos -> mapToResponsePago(pagos, token)).toList();
    }

    public PlanesResponse updatePlan(Long id, PlanesRequest p, String token) {
        log.info("Actualizar Planes", keyValue("idPlan", id));
        Planes planes1 = planesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Plan no encontrado"));

        Pagos pago = pagosRepository.findById(p.getIdPago())
                .orElseThrow(() -> new EntityNotFoundException("El método de pago especificado no existe"));

        planes1.setNombrePlan(p.getNombrePlan());
        planes1.setPrecioPlan(p.getPrecioPlan());
        planes1.setDescripcionPlan(p.getDescripcionPlan());
        planes1.setBeneficios(p.getBeneficios());
        planes1.setIdPago(pago);

        Planes updatePlan = planesRepository.save(planes1);
        log.info("Plan actualizado correctamente", keyValue("idPlan", updatePlan.getId()));
        return mapToResponsePlan(updatePlan, token);
    }

    public PagosResponse updatePago(Long id, PagosRequest pa, String token) {
        Pagos pagos1 = pagosRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No se ha encontrado el pago"));
        log.info("Actualizar Pago", keyValue("idPago", id));

        var cliente = client.getCliente(pa.getIdCliente(), token);
        if (cliente == null) {
            log.warn("Cliente no encontrado", keyValue("idCliente", pa.getIdCliente()));
            throw new EntityNotFoundException("Cliente no encontrado");
        }

        pagos1.setTipoPago(pa.getTipoPago());
        pagos1.setNumTarjeta(pa.getNumTarjeta());
        pagos1.setFechaVencimiento(pa.getFechaVencimiento());
        pagos1.setCvc(pa.getCvc());
        pagos1.setDireccionFacturacion(pa.getDireccionFacturacion());
        pagos1.setCodigoPostal(pa.getCodigoPostal());
        pagos1.setIdCliente(pa.getIdCliente());

        Pagos updatePago = pagosRepository.save(pagos1);
        log.info("Metodo de pago actualizado correctamente", keyValue("idPago", updatePago.getId()));
        return mapToResponsePago(updatePago, token);
    }

    public void deletePlan(Long id) {
        log.info("Eliminando plan", keyValue("idPlan", id));
        if (!planesRepository.existsById(id)) {
            throw new EntityNotFoundException("No se puede eliminar un plan inexistente");
        }
        planesRepository.deleteById(id);
    }

    public void deletePago(Long id) {
        log.info("Eliminando metodo de pago", keyValue("idPago", id));
        if (!pagosRepository.existsById(id)) {
            throw new EntityNotFoundException("No se puede eliminar un metodo de pago inexistente");
        }
        pagosRepository.deleteById(id);
    }

    private PlanesResponse mapToResponsePlan(Planes p, String token) {
        return PlanesResponse.builder()
                .id(p.getId())
                .nombrePlan(p.getNombrePlan())
                .precioPlan(p.getPrecioPlan())
                .descripcionPlan(p.getDescripcionPlan())
                .beneficios(p.getBeneficios())
                .idPago(p.getIdPago() != null ? p.getIdPago().getId() : null)
                .build();
    }

    private PagosResponse mapToResponsePago(Pagos pa, String token) {
        return PagosResponse.builder()
                .id(pa.getId())
                .tipoPago(pa.getTipoPago())
                .numTarjeta(pa.getNumTarjeta())
                .fechaVencimiento(pa.getFechaVencimiento())
                .cvc(pa.getCvc())
                .direccionFacturacion(pa.getDireccionFacturacion())
                .codigoPostal(pa.getCodigoPostal())
                .idCliente(pa.getIdCliente())
                .build();
    }
}