package com.example.ms_planes.service;

import com.example.ms_planes.dto.PlanesRequest;
import com.example.ms_planes.dto.PlanesResponse;
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
    public PlanesResponse add (PlanesRequest request){
        log.info ("Crear Planes", keyValue("nombre", request.getNombrePlan()));
        Planes planes1 = new Planes();
        planes1.setNombrePlan(request.getNombrePlan());
        planes1.setPrecioPlan(request.getPrecioPlan());
        planes1.setIdPago(request.getIdPago());
        planesRepository.save(planes1);
        return mapToResponse(planes1);
    }
    public PlanesResponse findById(Long id){
        log.info("Obtener id de Planes", keyValue("id", id));
        Planes planes1 = planesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Plan no Existe"));
        return mapToResponse(planes1);
    }
    public List<PlanesResponse> getAll(){
        return planesRepository.findAll().stream().map(cliente -> mapToResponse(cliente)).toList();
    }
    public PlanesResponse update(Long id, PlanesRequest p){
        Planes planes1 = planesRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("No se ha encontrado el plan"));
        log.info("Actualizar Planes", keyValue("id", id));
        planes1.setId(id);
        planes1.setNombrePlan(p.getNombrePlan());
        planes1.setPrecioPlan(p.getPrecioPlan());
        planes1.setIdPago(p.getIdPago());
        planesRepository.save(planes1);
        return mapToResponse(planes1);
    }
    public void delete(Long id){
        log.info("Eliminar cliente",keyValue("id", id));
        planesRepository.deleteById(id);
    }
    private PlanesResponse mapToResponse(Planes p) {
        return PlanesResponse.builder()
                .id(p.getId()) //
                .nombrePlan(p.getNombrePlan())
                .precioPlan(p.getPrecioPlan())
                .idPago(p.getIdPago())
                .build();
    }

}
