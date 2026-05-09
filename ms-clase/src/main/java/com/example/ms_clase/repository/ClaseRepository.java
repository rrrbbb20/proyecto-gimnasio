package com.example.ms_clase.repository;


import com.example.ms_clase.model.Clase;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Id> {



}
