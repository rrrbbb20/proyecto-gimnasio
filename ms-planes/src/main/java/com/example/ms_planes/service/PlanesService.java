package com.example.ms_planes.service;

import com.example.ms_planes.dto.PagosRequest;
import com.example.ms_planes.dto.PagosResponse;
import com.example.ms_planes.dto.PlanesRequest;
import com.example.ms_planes.dto.PlanesResponse;
import com.example.ms_planes.model.Pagos;
import com.example.ms_planes.model.Planes;
import com.example.ms_planes.repository.PagosRepository;
import com.example.ms_planes.repository.PlanesRepository;
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
    public PlanesResponse addPlan (PlanesRequest planesRequest){
        log.info ("Crear Planes", keyValue("nombre", planesRequest.getNombrePlan()));
        Planes planes1 = new Planes();
        planes1.setNombrePlan(planesRequest.getNombrePlan());
        planes1.setPrecioPlan(planesRequest.getPrecioPlan());
        planes1.setIdPago(planesRequest.getIdPago());
        planesRepository.save(planes1);
        return mapToResponsePlan(planes1);
    }
    public PagosResponse addPago(PagosRequest pagosRequest){
        log.info("Crear Pago", keyValue("tipo", pagosRequest.getTipoPago()));
        Pagos pagos1 = new Pagos();
        pagos1.setTipoPago(pagosRequest.getTipoPago());
        pagos1.setNumeroTarjeta(pagosRequest.getNumeroTarjeta());
        pagos1.setFechaCaducidad(pagosRequest.getFechaCaducidad());
        pagos1.setCvc(pagosRequest.getCvc());
        pagosRepository.save(pagos1);
        return mapToResponsePago(pagos1);
    }
    public PlanesResponse findByIdPlan(Long id){
        log.info("Obtener id de Planes", keyValue("id", id));
        Planes planes1 = planesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Plan no Existe"));
        return mapToResponsePlan(planes1);
    }
    public PagosResponse findByIdPago(Long idPago){
        log.info("Obtener id de Planes", keyValue("id", idPago));
        Pagos pagos1 = pagosRepository.findById(idPago).orElseThrow(() -> new EntityNotFoundException("Pago no Existente"));
        return mapToResponsePago(pagos1);
    }
    public List<PlanesResponse> getAllPlanes(){
        return planesRepository.findAll().stream().map(planes -> mapToResponsePlan(planes)).toList();
    }
    public List<PagosResponse> getAllPagos(){
        return pagosRepository.findAll().stream().map(pagos -> mapToResponsePago(pagos)).toList();}

    public PlanesResponse updatePlan(Long id, PlanesRequest p){
        Planes planes1 = planesRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("No se ha encontrado el plan"));
        log.info("Actualizar Planes", keyValue("id", id));
        planes1.setId(id);
        planes1.setNombrePlan(p.getNombrePlan());
        planes1.setPrecioPlan(p.getPrecioPlan());
        planes1.setIdPago(p.getIdPago());
        planesRepository.save(planes1);
        return mapToResponsePlan(planes1);
    }
    public PagosResponse updatePago (Long idPago, PagosRequest pa){
        Pagos pagos1 = pagosRepository.findById(idPago).orElseThrow(()-> new EntityNotFoundException(("No se ha encontrado el pago")));
        log.info("Actualizar Pago", keyValue("id", idPago));
        pagos1.setIdPago(idPago);
        pagos1.setTipoPago(pa.getTipoPago());
        pagos1.setNumeroTarjeta(pa.getNumeroTarjeta());
        pagos1.setFechaCaducidad(pa.getFechaCaducidad());
        pagos1.setCvc(pa.getCvc());
        pagosRepository.save(pagos1);
        return mapToResponsePago(pagos1);
    }
    public void deletePlan(Long id){
        log.info("Eliminar Plan",keyValue("id", id));
        planesRepository.deleteById(id);
    }
    public void deletePago(Long idPago){
        log.info("Eliminar Pago",keyValue("id", idPago));
        pagosRepository.deleteById(idPago);}
    private PlanesResponse mapToResponsePlan(Planes p) {
        return PlanesResponse.builder()
                .id(p.getId())
                .nombrePlan(p.getNombrePlan())
                .precioPlan(p.getPrecioPlan())
                .idPago(p.getIdPago())
                .build();
    }
    private PagosResponse mapToResponsePago(Pagos pa){
        return PagosResponse.builder()
                .idPago(pa.getIdPago())
                .tipoPago(pa.getTipoPago())
                .numeroTarjeta(pa.getNumeroTarjeta())
                .fechaCaducidad(pa.getFechaCaducidad())
                .cvc(pa.getCvc()).build();
    }

}
