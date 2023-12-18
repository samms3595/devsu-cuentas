package com.devsu.cuentas.service.impl;

import com.devsu.cuentas.dto.*;
import com.devsu.cuentas.exceptions.CustomHandlerException;
import com.devsu.cuentas.exceptions.SQLCustomException;
import com.devsu.cuentas.feign.UsuarioFeignClient;
import com.devsu.cuentas.model.Cliente;
import com.devsu.cuentas.model.Cuenta;
import com.devsu.cuentas.model.Movimiento;
import com.devsu.cuentas.repository.MovimientoRepository;
import com.devsu.cuentas.service.CuentaService;
import com.devsu.cuentas.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaService cuentaService;
    @Autowired
    private UsuarioFeignClient usuarioFeignClient;

    @Autowired
    public MovimientoServiceImpl(MovimientoRepository movimientoRepository, CuentaService cuentaService){
        this.movimientoRepository = movimientoRepository;
        this.cuentaService = cuentaService;
    }

    @Override
    public Movimiento guardarMovimiento(MovimientoDTO movimientoCuenta) {
        var movimiento = convertirDtoAEntidad(movimientoCuenta);
        var montoActualizado = cuentaService.actualizarMonto(movimiento.getCuenta().getId(),
                movimiento.getValor(), movimiento.getTipoMovimiento());
        try {
            movimiento.setSaldo(montoActualizado.getSaldoActual());
            return movimientoRepository.save(movimiento);
        } catch (DataIntegrityViolationException e) {
            throw new SQLCustomException("Error de integridad de datos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new CustomHandlerException("Error al guardar el movimiento: " + e.getMessage());
        }
    }

    @Override
    public Movimiento buscarMovimientoPorId(Long id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new CustomHandlerException("Movimiento no encontrado con ID: " + id));
    }

    @Override
    public List<Movimiento> listarMovimientosPorIdCuenta(Long idCuenta) {
        List<Movimiento> movimientos = movimientoRepository.findMovimientoByCuentaId(idCuenta);
        if(movimientos.isEmpty()){
           throw new CustomHandlerException("No hay datos que mostrar");
        }
        return movimientos;
    }

    @Override
    public boolean eliminarMovimiento(Long id) {
        if (!movimientoRepository.existsById(id)) {
            throw new CustomHandlerException("Movimiento no encontrado con ID: " + id);
        }
        try {
            movimientoRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new SQLCustomException("Error al eliminar el movimiento", e);
        }
    }

    public Movimiento convertirDtoAEntidad(MovimientoDTO movimientoDTO) {
        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(movimientoDTO.getFecha());
        movimiento.setSaldo(movimientoDTO.getSaldo());
        movimiento.setValor(movimientoDTO.getValor());
        movimiento.setTipoMovimiento(movimientoDTO.getTipoMovimiento());

        Cuenta cuenta = cuentaService.buscarCuentaPorId(movimientoDTO.getCuentaId());
        movimiento.setCuenta(cuenta);
        return movimiento;
    }

    @Override
    public Movimiento actualizarMovimiento(Movimiento movimiento) {
        if (movimiento == null || movimiento.getId() == null || !movimientoRepository.existsById(movimiento.getId())) {
            throw new CustomHandlerException("Movimiento no encontrado o datos inválidos");
        }
        try {
            return movimientoRepository.save(movimiento);
        } catch (Exception e) {
            throw new SQLCustomException("Error al actualizar el movimiento", e);
        }
    }

    @Override
    public List<Movimiento> encontrarMovimientoPorIdCliente(Long id) {
        List<Movimiento> movimientos = movimientoRepository.findByCuentaIdCliente(id);
        if(movimientos.isEmpty()) {
            throw new CustomHandlerException("No existen movimientos para el cliente: " + id);
        }
        return movimientos;
    }

    @Override
    public List<ReporteDTO> buscarMovimientosPorFechaYIdCliente(LocalDate fecha, LocalDate fin, Long idCliente) {

        List<ReporteDTO> reporteDTOList = new ArrayList<>();

        Cliente cliente = usuarioFeignClient.obtenerClientePorClienteId(idCliente);

        List<Movimiento> movimientos = movimientoRepository.findByFechaBetweenAndCuentaIdCliente(fecha, fin, idCliente);


        for (Movimiento movimiento: movimientos) {
            ReporteDTO reporteDTO = new ReporteDTO();
            reporteDTO.setFecha(movimiento.getFecha());
            reporteDTO.setNombreCliente(cliente.getNombre());
            reporteDTO.setNumeroCuenta(movimiento.getCuenta().getNumeroCuenta());
            reporteDTO.setTipoCuenta(movimiento.getCuenta().getTipoCuenta());
            reporteDTO.setSaldoInicial(movimiento.getCuenta().getSaldoInicial());
            reporteDTO.setEstado(movimiento.getCuenta().getEstado());
            reporteDTO.setValor(movimiento.getValor());
            reporteDTO.setSaldo(movimiento.getSaldo());

            reporteDTOList.add(reporteDTO);
        }
        if(reporteDTOList.isEmpty())
        {
            throw new CustomHandlerException("No hay datos que mostrar");
        }
        return reporteDTOList;
     }
}
