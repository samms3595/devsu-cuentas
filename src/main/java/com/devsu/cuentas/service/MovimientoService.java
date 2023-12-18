package com.devsu.cuentas.service;

import com.devsu.cuentas.dto.MovimientoDTO;
import com.devsu.cuentas.dto.ReporteDTO;
import com.devsu.cuentas.model.Movimiento;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoService {

    Movimiento guardarMovimiento(MovimientoDTO movimiento);
    Movimiento buscarMovimientoPorId(Long id);
    List<Movimiento> listarMovimientosPorIdCuenta(Long idCuenta);
    boolean eliminarMovimiento(Long id);
    Movimiento actualizarMovimiento(Movimiento movimiento);
    List<Movimiento> encontrarMovimientoPorIdCliente(Long id);
    List<ReporteDTO> buscarMovimientosPorFechaYIdCliente(LocalDate fechaInicio, LocalDate fechaFin, Long idCliente);
}
