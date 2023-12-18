package com.devsu.cuentas.service.impl;

import com.devsu.cuentas.dto.MovimientoDTO;
import com.devsu.cuentas.dto.ReporteDTO;
import com.devsu.cuentas.enums.TipoMovimiento;
import com.devsu.cuentas.exceptions.CustomHandlerException;
import com.devsu.cuentas.exceptions.SQLCustomException;
import com.devsu.cuentas.feign.UsuarioFeignClient;
import com.devsu.cuentas.model.Cliente;
import com.devsu.cuentas.model.Cuenta;
import com.devsu.cuentas.model.Movimiento;
import com.devsu.cuentas.repository.MovimientoRepository;
import com.devsu.cuentas.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceImplTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaService cuentaService;

    @Mock
    private UsuarioFeignClient usuarioFeignClient;

    @InjectMocks
    private MovimientoServiceImpl movimientoService;

    private MovimientoDTO movimientoDTO;
    private Movimiento movimiento;
    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta(1L, 1L, 123456789L, null, 1000.0, 1000.0, null, null);
        movimientoDTO = new MovimientoDTO(LocalDate.now(), 1000.0, 100.0, TipoMovimiento.DEPOSITO, 1L);
        movimiento = new Movimiento(1L, LocalDate.now(), 1000.0, 100.0, TipoMovimiento.DEPOSITO, cuenta);
    }

    @Test
    void testGuardarMovimientoWhenValidMovimientoThenReturnMovimiento() {
        when(cuentaService.actualizarMonto(anyLong(), anyDouble(), any(TipoMovimiento.class))).thenReturn(cuenta);
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);

        Movimiento result = movimientoService.guardarMovimiento(movimientoDTO);

        assertNotNull(result);
        assertEquals(movimiento.getValor(), result.getValor());
        assertEquals(movimiento.getFecha(), result.getFecha());
        assertEquals(movimiento.getTipoMovimiento(), result.getTipoMovimiento());
        assertEquals(movimiento.getCuenta().getId(), result.getCuenta().getId());
    }

    @Test
    void testBuscarMovimientoPorIdWhenValidIdThenReturnMovimiento() {
        when(movimientoRepository.findById(anyLong())).thenReturn(Optional.of(movimiento));

        Movimiento result = movimientoService.buscarMovimientoPorId(1L);

        assertNotNull(result);
        assertEquals(movimiento.getId(), result.getId());
    }

    @Test
    void testListarMovimientosPorIdCuentaWhenValidIdCuentaThenReturnMovimientos() {
        when(movimientoRepository.findMovimientoByCuentaId(anyLong())).thenReturn(Collections.singletonList(movimiento));

        List<Movimiento> result = movimientoService.listarMovimientosPorIdCuenta(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(movimiento.getId(), result.get(0).getId());
    }

    @Test
    void testEliminarMovimientoWhenValidIdThenReturnTrue() {
        when(movimientoRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(movimientoRepository).deleteById(anyLong());

        boolean result = movimientoService.eliminarMovimiento(1L);

        assertTrue(result);
    }

    @Test
    void testConvertirDtoAEntidadWhenValidMovimientoDTOThenReturnMovimiento() {
        when(cuentaService.buscarCuentaPorId(anyLong())).thenReturn(cuenta);

        Movimiento result = movimientoService.convertirDtoAEntidad(movimientoDTO);

        assertNotNull(result);
        assertEquals(movimientoDTO.getFecha(), result.getFecha());
        assertEquals(movimientoDTO.getValor(), result.getValor());
        assertEquals(movimientoDTO.getTipoMovimiento(), result.getTipoMovimiento());
        assertEquals(movimientoDTO.getCuentaId(), result.getCuenta().getId());
    }

    @Test
    void testActualizarMovimientoWhenValidMovimientoThenReturnMovimiento() {
        when(movimientoRepository.existsById(anyLong())).thenReturn(true);
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);

        Movimiento result = movimientoService.actualizarMovimiento(movimiento);

        assertNotNull(result);
        assertEquals(movimiento.getId(), result.getId());
    }

    @Test
    void testEncontrarMovimientoPorIdClienteWhenValidIdThenReturnMovimientos() {
        when(movimientoRepository.findByCuentaIdCliente(anyLong())).thenReturn(Collections.singletonList(movimiento));

        List<Movimiento> result = movimientoService.encontrarMovimientoPorIdCliente(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(movimiento.getId(), result.get(0).getId());
    }

    @Test
    void testBuscarMovimientosPorFechaYIdClienteWhenValidFechaFinIdClienteThenReturnReporteDTOs() {
        when(usuarioFeignClient.obtenerClientePorClienteId(anyLong())).thenReturn(new Cliente());
        when(movimientoRepository.findByFechaBetweenAndCuentaIdCliente(any(LocalDate.class), any(LocalDate.class), anyLong()))
                .thenReturn(Collections.singletonList(movimiento));

        List<ReporteDTO> result = movimientoService.buscarMovimientosPorFechaYIdCliente(LocalDate.now(), LocalDate.now().plusDays(1), 1L);

        assertFalse(result.isEmpty());
    }

    // Additional tests for exception scenarios can be added here
}