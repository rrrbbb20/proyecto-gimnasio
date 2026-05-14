package com.example.ms_clase.repository;


import com.example.ms_clase.model.Clase;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Clase c SET c.cupos = c.cupos - 1 WHERE c.id = :id AND c.cupos > 0")
    int restarCupo(Long id);
    @Modifying
    @Transactional
    @Query("UPDATE Clase c SET c.cupos = c.cupos + 1 WHERE c.id = :id ")
    int sumarCupo(Long id);
}
