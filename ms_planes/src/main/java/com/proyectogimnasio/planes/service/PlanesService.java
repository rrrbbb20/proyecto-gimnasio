package com.proyectogimnasio.planes.service;

import com.proyectogimnasio.planes.client.ClienteClient;
import com.proyectogimnasio.planes.dto.*;
import com.proyectogimnasio.planes.model.Pagos;
import com.proyectogimnasio.planes.model.Planes;
import com.proyectogimnasio.planes.model.Suscripcion;
import com.proyectogimnasio.planes.repository.PagosRepository;
import com.proyectogimnasio.planes.repository.PlanesRepository;
import com.proyectogimnasio.planes.repository.SuscripcionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanesService {
    private final PlanesRepository planesRepository;
    private final PagosRepository pagosRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final ClienteClient client;

    public PlanesResponse addPlan(PlanesRequest p) {
        log.info("Crear Planes", keyValue("nombre", p.getNombrePlan()));
        Planes planes1 = new Planes();
        planes1.setNombrePlan(p.getNombrePlan());
        planes1.setPrecioPlan(p.getPrecioPlan());
        planes1.setDescripcionPlan(p.getDescripcionPlan());
        planes1.setBeneficios(p.getBeneficios());

        Planes savePlan = planesRepository.save(planes1);
        return mapToResponsePlan(savePlan);
    }

    public PagosResponse addPago(PagosRequest pa) {
        log.info("Crear Pago", keyValue("tipo", pa.getTipoPago()));


        var cliente = client.getCliente(pa.getIdCliente());
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
        return mapToResponsePago(savePago);
    }

    public PlanesResponse findByIdPlan(Long id) {
        log.info("Obtener id de Planes", keyValue("id", id));
        Planes planes1 = planesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Plan no Existe"));
        return mapToResponsePlan(planes1);
    }

    public PagosResponse findByIdPago(Long id) {
        log.info("Buscar metodo de pago", keyValue("idPago", id));
        Pagos pago = pagosRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Metodo de pago no encontrado"));
        return mapToResponsePago(pago);
    }

    public List<PlanesResponse> getAllPlanes() {
        return planesRepository.findAll().stream().map(this::mapToResponsePlan).toList();
    }

    public List<PagosResponse> getAllPagos() {
        return pagosRepository.findAll().stream().map(this::mapToResponsePago).toList();
    }

    public PlanesResponse updatePlan(Long id, PlanesRequest p) {
        log.info("Actualizar Planes", keyValue("idPlan", id));
        Planes planes1 = planesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Plan no encontrado"));


        planes1.setNombrePlan(p.getNombrePlan());
        planes1.setPrecioPlan(p.getPrecioPlan());
        planes1.setDescripcionPlan(p.getDescripcionPlan());
        planes1.setBeneficios(p.getBeneficios());

        Planes updatePlan = planesRepository.save(planes1);
        log.info("Plan actualizado correctamente", keyValue("idPlan", updatePlan.getId()));
        return mapToResponsePlan(updatePlan);
    }

    public PagosResponse updatePago(Long id, PagosRequest pa) {
        Pagos pagos1 = pagosRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No se ha encontrado el pago"));
        log.info("Actualizar Pago", keyValue("idPago", id));

        var cliente = client.getCliente(pa.getIdCliente());
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
        return mapToResponsePago(updatePago);
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
    @Transactional
    public SuscripcionResponse crearSuscripcion(SuscripcionRequest request) {
        Planes plan = planesRepository.findById(request.getIdPlan())
                .orElseThrow(() -> new EntityNotFoundException("Plan no encontrado"));

        Pagos pago = new Pagos();
        pago.setTipoPago(request.getPago().getTipoPago());
        pago.setNumTarjeta(request.getPago().getNumTarjeta());
        pago.setFechaVencimiento(request.getPago().getFechaVencimiento());
        pago.setCvc(request.getPago().getCvc());
        pago.setDireccionFacturacion(request.getPago().getDireccionFacturacion());
        pago.setCodigoPostal(request.getPago().getCodigoPostal());
        pago.setIdCliente(request.getIdCliente());

        pago = pagosRepository.save(pago);

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setIdCliente(request.getIdCliente());
        suscripcion.setPlan(plan);
        suscripcion.setPago(pago);
        suscripcion.setFechaInicio(LocalDate.now());
        suscripcion.setFechaFin(LocalDate.now().plusMonths(1));
        suscripcion.setEstado("ACTIVA");

        Suscripcion suscripcionGuardada = suscripcionRepository.save(suscripcion);

        return mapToResponseSuscripcion(suscripcionGuardada);
    }
    public List<SuscripcionResponse> getAllSuscripciones() {
        log.info("Obteniendo listado de todas las suscripciones");
        return suscripcionRepository.findAll().stream()
                .map(this::mapToResponseSuscripcion)
                .toList();
    }

    public SuscripcionResponse getSuscripcionByCliente(Long idCliente) {
        log.info("Buscando suscripción del cliente", keyValue("idCliente", idCliente));
        Suscripcion suscripcion = suscripcionRepository.findByIdCliente(idCliente)
                .orElseThrow(() -> new EntityNotFoundException("El cliente no tiene ninguna suscripción registrada"));
        return mapToResponseSuscripcion(suscripcion);
    }

    @Transactional
    public SuscripcionResponse updateSuscripcion(Long id, String nuevoEstado) {
        log.info("Actualizando estado de suscripción", keyValue("idSuscripcion", id), keyValue("nuevoEstado", nuevoEstado));

        Suscripcion suscripcion = suscripcionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Suscripción no encontrada"));

        suscripcion.setEstado(nuevoEstado.toUpperCase());

        if ("CANCELADA".equals(suscripcion.getEstado())) {
            suscripcion.setFechaFin(java.time.LocalDate.now());
        }

        Suscripcion actualizada = suscripcionRepository.save(suscripcion);
        return mapToResponseSuscripcion(actualizada);
    }

    @Transactional
    public void deleteSuscripcion(Long id) {
        log.info("Eliminando suscripción del sistema", keyValue("idSuscripcion", id));
        if (!suscripcionRepository.existsById(id)) {
            throw new EntityNotFoundException("No se puede eliminar una suscripción inexistente");
        }
        suscripcionRepository.deleteById(id);
    }
    private SuscripcionResponse mapToResponseSuscripcion(Suscripcion s) {
        ClienteResponse datosCliente;
        try {
            datosCliente = client.getCliente(s.getIdCliente());
        } catch (Exception e) {
            log.error("Fallo de comunicación al enriquecer datos de cliente", e);
            datosCliente = new ClienteResponse();
            datosCliente.setNombres("Desconocido (Fallo de red)");
            datosCliente.setApellidos("");
        }

        return SuscripcionResponse.builder()
                .id(s.getId())
                .idCliente(s.getIdCliente())
                .plan(mapToResponsePlan(s.getPlan()))
                .pago(mapToResponsePago(s.getPago()))
                .cliente(datosCliente)
                .fechaInicio(s.getFechaInicio())
                .fechaFin(s.getFechaFin())
                .estado(s.getEstado())
                .build();
        }

    private PlanesResponse mapToResponsePlan(Planes p) {
        return PlanesResponse.builder()
                .id(p.getId())
                .nombrePlan(p.getNombrePlan())
                .precioPlan(p.getPrecioPlan())
                .descripcionPlan(p.getDescripcionPlan())
                .beneficios(p.getBeneficios())
                .build();
    }

    private PagosResponse mapToResponsePago(Pagos pa) {
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