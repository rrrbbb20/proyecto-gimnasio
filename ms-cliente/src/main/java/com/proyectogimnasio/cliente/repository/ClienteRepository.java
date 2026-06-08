package com.proyectogimnasio.cliente.repository;

import com.proyectogimnasio.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Long, Cliente> {
}
