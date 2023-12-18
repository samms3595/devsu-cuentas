package com.devsu.cuentas.repository;

import com.devsu.cuentas.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findMovimientoByCuentaId(Long id);
    List<Movimiento> findByCuentaIdCliente(Long idCliente);
    List<Movimiento> findByFechaBetweenAndCuentaIdCliente(LocalDate fechaInicio, LocalDate fechaFin, Long idCliente);
}
