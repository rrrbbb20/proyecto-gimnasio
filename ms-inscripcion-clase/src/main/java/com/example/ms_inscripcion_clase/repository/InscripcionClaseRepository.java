package com.example.ms_inscripcion_clase.repository;

import com.example.ms_inscripcion_clase.model.InscripcionClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscripcionClaseRepository extends JpaRepository<InscripcionClase,Long> {


}
