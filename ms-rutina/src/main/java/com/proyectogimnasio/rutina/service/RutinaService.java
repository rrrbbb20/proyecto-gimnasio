package com.proyectogimnasio.rutina.service;

import com.proyectogimnasio.rutina.dto.*;
import com.proyectogimnasio.rutina.model.DetallesEjercicio;
import com.proyectogimnasio.rutina.model.Ejercicio;
import com.proyectogimnasio.rutina.model.Rutina;
import com.proyectogimnasio.rutina.repository.DetallesEjercicioRepository;
import com.proyectogimnasio.rutina.repository.EjercicioRepository;
import com.proyectogimnasio.rutina.repository.RutinaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.e;
import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class RutinaService {
    private final EjercicioRepository ejercicioRepository;
    private final RutinaRepository rutinaRepository;
    private final DetallesEjercicioRepository detallesEjercicioRepository;


    public RutinaResponse addRutina (RutinaRequest request, String token){
        log.info("Crear Rutina", keyValue("nombre", request.getNombreRutina()));
        Rutina rutina1 = new Rutina();
        rutina1.setNombreRutina(request.getNombreRutina());
        rutina1.setDescripcionRutina(request.getDescripcionRutina());
        rutina1.setDetalles(request.getDetalles());
        Rutina saveRutina = rutinaRepository.save(rutina1);
        log.info("Rutina creada exitosamente",
                keyValue("idRutina", saveRutina.getId()));
        return mapToResponseRutina(saveRutina, token);

    }
    public EjercicioResponse addEjercicio (EjercicioRequest request,String token){
        log.info("Crear Ejercicio", keyValue("nombre", request.getNombreEjercicio()));
        Ejercicio ejercicio1 = new Ejercicio();
        ejercicio1.setNombreEjercicio(request.getNombreEjercicio());
        ejercicio1.setZonaEjercitada(request.getZonaEjercitada());
        ejercicio1.setRepeticiones(request.getRepeticiones());
        ejercicio1.setDetalles(request.getDetalles());
        Ejercicio saveEjercicio= ejercicioRepository.save(ejercicio1);
        log.info("Ejercicio creado exitosamente",
                keyValue("idRutina", saveEjercicio.getId()));
        return mapToResponseEjercicio(saveEjercicio, token);


    }
    public DetallesEjercicioResponse addDetalles(DetallesEjercicioRequest request, String token){
        log.info("Crear detalles", keyValue("detalles",request.getEjercicio(), String.valueOf(request.getRutina())));
        DetallesEjercicio detalles1 = new DetallesEjercicio();
        detalles1.setRutina(request.getRutina());
        detalles1.setEjercicio(request.getEjercicio());
        detalles1.setDuracionRutina(request.getDuracionRutina());
        detalles1.setTiempoDescanso(request.getTiempoDescanso());
        detalles1.setNumeroEjercicios(request.getNumeroEjercicios());
        DetallesEjercicio saveDetalles = detallesEjercicioRepository.save(detalles1);
        log.info("Detalles creados exitosamente",
                keyValue("idDetalles", saveDetalles.getId()));
        return mapToResponseDetalles(saveDetalles,token);

    }
    public RutinaResponse findRutina (Long id, String token){
        log.info("Buscar rutina",
                keyValue("idRutina", id));
        Rutina rutina = rutinaRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Rutina no encontrada"));
        log.info("Rutina encontrada",
                keyValue("NombreRutina", rutina.getNombreRutina()));
        return mapToResponseRutina(rutina,token);

    }
    public EjercicioResponse findEjercicio (Long id, String token){
        log.info("Buscar ejercicio",
                keyValue("idEjercicio", id));
        Ejercicio ejercicio = ejercicioRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Ejercicio no encontrado"));
        log.info("Ejercicio encontrado",
                keyValue("NombreEjercicio", ejercicio.getNombreEjercicio()));
        return mapToResponseEjercicio(ejercicio,token);

    }
    public DetallesEjercicioResponse findDetalles (Long id, String token){
        log.info("Buscar detalles",
                keyValue("idCliente", id));
        DetallesEjercicio detalles = detallesEjercicioRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Detalles no encontrados"));
        log.info("Detalles encontrados",
                keyValue("detalles", detalles.getEjercicio(), String.valueOf(detalles.getRutina())));
        return mapToResponseDetalles(detalles,token);

    }
    public List<RutinaResponse> getRutinas (String token){
        log.info("Listando rutinas");
        return rutinaRepository.findAll().stream()
                .map(rutina -> mapToResponseRutina(rutina,token))
                .toList();

    }
    public List<EjercicioResponse> getEjercicios(String token){
        log.info("Listando ejercicio");
        //esto es para que retorne todos los clientes en base a la estructura del maptoresponse
        return ejercicioRepository.findAll().stream()
                .map(ejercicio -> mapToResponseEjercicio(ejercicio,token))
                .toList();

    }
    public List<DetallesEjercicioResponse> getDetalles (String token){
        log.info("Listando clientes");
        //esto es para que retorne todos los clientes en base a la estructura del maptoresponse
        return detallesEjercicioRepository.findAll().stream()
                .map(detalles -> mapToResponseDetalles(detalles,token))
                .toList();

    }
    public RutinaResponse updateRutina(Long id, RutinaRequest request, String token){
        log.info("Actualizando rutina",
                keyValue("idRutina", id));
        Rutina rutina1 = rutinaRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Rutina no encontrada"));
        rutina1.setNombreRutina(request.getNombreRutina());
        rutina1.setDescripcionRutina(request.getDescripcionRutina());
        rutina1.setDetalles(request.getDetalles());
        Rutina saveRutina = rutinaRepository.save(rutina1);
        log.info("Rutina creada exitosamente",
                keyValue("idRutina", saveRutina.getId()));
        return mapToResponseRutina(saveRutina, token);

    }
    public EjercicioResponse updateEjercicio(Long id, EjercicioRequest request, String token){
        log.info("Actualizando ejercicio",
                keyValue("idEjercicio", id));
        Ejercicio ejercicio1 = ejercicioRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Ejercicio no encontrado"));

        ejercicio1.setNombreEjercicio(request.getNombreEjercicio());
        ejercicio1.setZonaEjercitada(request.getZonaEjercitada());
        ejercicio1.setRepeticiones(request.getRepeticiones());
        ejercicio1.setDetalles(request.getDetalles());
        Ejercicio saveEjercicio= ejercicioRepository.save(ejercicio1);
        log.info("Ejercicio creado exitosamente",
                keyValue("idEjercicio", saveEjercicio.getId()));
        return mapToResponseEjercicio(saveEjercicio, token);

    }
    public DetallesEjercicioResponse updateDetalles(Long id, DetallesEjercicioRequest request, String token){
        log.info("Actualizando detalles",
                keyValue("idDetalles", id));
        DetallesEjercicio detalles1 = detallesEjercicioRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Detalles no encontrados"));

        detalles1.setRutina(request.getRutina());
        detalles1.setEjercicio(request.getEjercicio());
        detalles1.setDuracionRutina(request.getDuracionRutina());
        detalles1.setTiempoDescanso(request.getTiempoDescanso());
        detalles1.setNumeroEjercicios(request.getNumeroEjercicios());
        DetallesEjercicio saveDetalles = detallesEjercicioRepository.save(detalles1);
        log.info("Detalles creados exitosamente",
                keyValue("idDetalles", saveDetalles.getId()));
        return mapToResponseDetalles(saveDetalles,token);

    }
    public void deleteRutina(Long id){
        log.info("Eliminando Rutina",
                keyValue("idRutina", id));
        //esto es una validacion de que realmente existe el cliente que se quiere eliminar
        if(!rutinaRepository.existsById(id)){
            log.warn("Rutina a eliminar inexistente",
                    keyValue("idRutina", id));
            throw new EntityNotFoundException("No se puede eliminar una rutina inexistente");
        }
        rutinaRepository.deleteById(id);
        log.info("Rutina eliminado correctamente",
                keyValue("idRutina",id));

    }
    public void deleteEjercicio(Long id){
        log.info("Eliminando ejercicio",
                keyValue("idEjercicio", id));
        //esto es una validacion de que realmente existe el cliente que se quiere eliminar
        if(!ejercicioRepository.existsById(id)){
            log.warn("Ejercicio a eliminar inexistente",
                    keyValue("idEjercicio", id));
            throw new EntityNotFoundException("No se puede eliminar un ejercicio inexistente");
        }
        ejercicioRepository.deleteById(id);
        log.info("Ejercicio eliminado correctamente",
                keyValue("idEjercicio",id));

    }
    public void deleteDetalles(Long id){
        log.info("Eliminando detalles",
                keyValue("idDetalles", id));
        //esto es una validacion de que realmente existe el cliente que se quiere eliminar
        if(!detallesEjercicioRepository.existsById(id)){
            log.warn("Detalles a eliminar inexistentes",
                    keyValue("idDetalles", id));
            throw new EntityNotFoundException("No se puede eliminar unos detalles inexistentes");
        }
        detallesEjercicioRepository.deleteById(id);
        log.info("Detalles eliminados correctamente",
                keyValue("idDetalle",id));

    }
    private RutinaResponse mapToResponseRutina(Rutina rutina, String token) {
        log.info("Mapeando rutina",
                keyValue("idRutina", rutina.getId())
        );


        return RutinaResponse.builder().id(rutina.getId()).
                nombreRutina(rutina.getNombreRutina()).
                descripcionRutina(rutina.getDescripcionRutina()).
                detalles(rutina.getDetalles()).
                build();

    }
    private EjercicioResponse mapToResponseEjercicio(Ejercicio ejercicio, String token) {
        log.info("Mapeando ejercicio",
                keyValue("idEjercicio", ejercicio.getId())
        );


        return EjercicioResponse.builder().id(ejercicio.getId()).
                nombreEjercicio(ejercicio.getNombreEjercicio()).
                zonaEjercitada(ejercicio.getZonaEjercitada()).
                repeticiones(ejercicio.getRepeticiones()).
                detalles(ejercicio.getDetalles()).
                build();

    }
    private DetallesEjercicioResponse mapToResponseDetalles(DetallesEjercicio detalles, String token) {
        log.info("Mapeando detalles",
                keyValue("idDetalles", detalles.getId())
        );

        return DetallesEjercicioResponse.builder().id(detalles.getId()).
                ejercicio(detalles.getEjercicio()).
                rutina(detalles.getRutina()).
                numeroEjercicios(detalles.getNumeroEjercicios()).
                duracionRutina(detalles.getDuracionRutina()).
                tiempoDescanso(detalles.getTiempoDescanso()).
                build();

    }
}
