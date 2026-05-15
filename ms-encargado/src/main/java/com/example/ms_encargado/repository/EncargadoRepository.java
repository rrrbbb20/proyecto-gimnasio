package com.example.ms_encargado.repository;


import com.example.ms_encargado.model.Encargado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncargadoRepository extends JpaRepository<Encargado, Long> {


}
