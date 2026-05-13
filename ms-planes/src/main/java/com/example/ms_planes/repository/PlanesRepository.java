package com.example.ms_planes.repository;

import com.example.ms_planes.model.Planes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanesRepository extends JpaRepository<Planes, Long> {
}
