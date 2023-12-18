package com.devsu.cuentas.service.impl;

import com.devsu.cuentas.enums.TipoMovimiento;
import com.devsu.cuentas.exceptions.CustomHandlerException;
import com.devsu.cuentas.exceptions.SQLCustomException;
import com.devsu.cuentas.feign.UsuarioFeignClient;
import com.devsu.cuentas.model.Cuenta;
import com.devsu.cuentas.repository.CuentaRepository;
import com.devsu.cuentas.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;

    @Autowired
    private UsuarioFeignClient usuarioFeignClient;

    private static final String RETIRO = "RETIRO";
    private static final String DEBITO = "DEPOSITO";

    public CuentaServiceImpl(CuentaRepository cuentaRepository){
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public Cuenta guardarCuenta(Cuenta cuenta) {

        try{
            cuenta.setSaldoActual(cuenta.getSaldoInicial());
            Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
            usuarioFeignClient.añadirCuenta(cuentaGuardada.getIdCliente(), cuentaGuardada.getId());
            return cuentaGuardada;
        }catch (DataIntegrityViolationException e) {
            throw new SQLCustomException("Error de integridad de datos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new CustomHandlerException("Error al guardar la cuenta: " + e.getMessage());
        }
    }

    @Override
    public Cuenta buscarCuentaPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new CustomHandlerException("Cuenta con el ID: " + id + " no encontrada"));
    }

    @Override
    public Cuenta actualizarCuenta(Cuenta cuenta) {
        return cuentaRepository.findById(cuenta.getId())
                .map(cuentaExistente ->{
                   copiarPropiedadesCuenta(cuentaExistente, cuenta);
                   return cuentaRepository.save(cuentaExistente);
                }).orElseThrow(()-> new CustomHandlerException("Cuenta con el ID: " + cuenta.getId() + " no encontrada"));
    }

    private void copiarPropiedadesCuenta(Cuenta existente, Cuenta actualizado) {
        existente.setId(existente.getId());
        existente.setIdCliente(existente.getIdCliente());
        existente.setNumeroCuenta(existente.getNumeroCuenta());
        existente.setTipoCuenta(actualizado.getTipoCuenta());
        existente.setSaldoInicial(actualizado.getSaldoInicial());
        existente.setMovimientos(actualizado.getMovimientos());
        existente.setEstado(actualizado.getEstado());
    }

    @Override
    public List<Cuenta> obtenerTodasLasCuentas() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        if(cuentas.isEmpty()){
            throw new CustomHandlerException("No hay cuentas disponibles");
        }
        return cuentas;
    }

    @Override
    public List<Cuenta> obtenerCuentaPoridCliente(Long idCliente) {
        List<Cuenta> cuentas = cuentaRepository.findCuentaByIdCliente(idCliente);
        if(cuentas.isEmpty()){
            throw new CustomHandlerException("No hay cuentas disponibles para el cliente: " +idCliente);
        }
        return cuentas;
    }

    @Override
    public boolean eliminarCuenta(Long id) {
        if(!cuentaRepository.existsById(id)){
            throw new CustomHandlerException("Cuenta con el ID: " + id + " no encontrada");
        }
        cuentaRepository.deleteById(id);
        return true;
    }

    @Override
    public Cuenta actualizarMonto(Long idCuenta, double monto, TipoMovimiento tipoMovimiento) {
        Cuenta cuenta = cuentaRepository.findById(idCuenta)
                .orElseThrow(() -> new CustomHandlerException("Cuenta no encontrada"));

        double saldoActual = cuenta.getSaldoActual();
        double saldoNuevo = tipoMovimiento.getValue().equalsIgnoreCase(RETIRO)
                ? saldoActual - Math.abs(monto)
                : saldoActual + Math.abs(monto);

        if (saldoNuevo < 0) {
            throw new CustomHandlerException("Saldo no disponible");
        }

        return ejecutarTransaccion(cuenta, saldoNuevo);
    }

    private Cuenta ejecutarTransaccion(Cuenta cuenta, double saldo) {
        cuenta.setSaldoActual(saldo);
        return cuentaRepository.save(cuenta);
    }
}
