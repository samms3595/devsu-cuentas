package com.devsu.cuentas.service;

import com.devsu.cuentas.enums.TipoMovimiento;
import com.devsu.cuentas.model.Cuenta;

import java.util.List;

public interface CuentaService {

    Cuenta guardarCuenta(Cuenta cuenta);
    Cuenta buscarCuentaPorId(Long id);
    Cuenta actualizarCuenta(Cuenta cuenta);
    List<Cuenta> obtenerTodasLasCuentas();
    List<Cuenta> obtenerCuentaPoridCliente(Long idCliente);
    boolean eliminarCuenta(Long id);
    Cuenta actualizarMonto(Long idCuenta, double monto, TipoMovimiento tipoMovimiento);
}
