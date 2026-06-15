package com.proyectogimnasio.planes.repository;

import com.proyectogimnasio.planes.model.Pagos;
import com.proyectogimnasio.planes.model.Planes;
import com.proyectogimnasio.planes.model.Suscripcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class SuscripcionRepositoryTest {

    @Autowired
    private SuscripcionRepository suscripcionRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Planes planPersistido;
    private Pagos pagoPersistido;

    @BeforeEach
    void setUp() {
        Planes plan = new Planes();
        plan.setNombrePlan("Plan Mensual");
        plan.setPrecioPlan(new BigDecimal("29990.00"));
        plan.setDescripcionPlan("Acceso total al gimnasio");
        plan.setBeneficios("Clases dirigidas incluidas");
        planPersistido = entityManager.persistAndFlush(plan);


        Pagos pago = new Pagos();
        pago.setTipoPago("Tarjeta de Crédito");
        pago.setNumTarjeta("1234********5678");
        pago.setIdCliente(1L);
        pagoPersistido = entityManager.persistAndFlush(pago);
    }

    @Test
    void findByIdCliente_CuandoExisteCliente_DebeRetornarSuscripcion() {
        // Arrange
        Long idClienteExistente = 1L;

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setIdCliente(idClienteExistente);
        suscripcion.setPlan(planPersistido);
        suscripcion.setPago(pagoPersistido);
        suscripcion.setFechaInicio(LocalDate.now());
        suscripcion.setFechaFin(LocalDate.now().plusMonths(1));
        suscripcion.setEstado("ACTIVA");

        entityManager.persistAndFlush(suscripcion);

        // Act
        Optional<Suscripcion> resultado = suscripcionRepository.findByIdCliente(idClienteExistente);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getIdCliente()).isEqualTo(idClienteExistente);
        assertThat(resultado.get().getEstado()).isEqualTo("ACTIVA");
        assertThat(resultado.get().getPlan().getNombrePlan()).isEqualTo("Plan Mensual");
        assertThat(resultado.get().getPago().getTipoPago()).isEqualTo("Tarjeta de Crédito");
    }

    @Test
    void findByIdCliente_CuandoNoExisteCliente_DebeRetornarOptionalVacio() {
        // Arrange
        Long idClienteInexistente = 999L;

        // Act
        Optional<Suscripcion> resultado = suscripcionRepository.findByIdCliente(idClienteInexistente);

        // Assert
        assertThat(resultado).isEmpty();
    }
}