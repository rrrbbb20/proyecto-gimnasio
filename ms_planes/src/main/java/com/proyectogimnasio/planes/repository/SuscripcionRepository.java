package com.proyectogimnasio.planes.repository;

import com.proyectogimnasio.planes.model.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {
    Optional<Suscripcion> findByIdCliente(Long idCliente);
}
