package com.proyectogimnasio.planes.repository;

import com.proyectogimnasio.planes.model.Planes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanesRepository extends JpaRepository<Planes,Long> {
}
