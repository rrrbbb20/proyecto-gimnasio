package com.example.ms_rutina.repository;

import com.example.ms_rutina.model.Ejercicio;
import com.example.ms_rutina.model.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface EjercicioRepository extends JpaRepository<Ejercicio, Long>{
}
