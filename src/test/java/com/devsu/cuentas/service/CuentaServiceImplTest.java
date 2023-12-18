package com.devsu.cuentas.service.impl;

import com.devsu.cuentas.enums.TipoMovimiento;
import com.devsu.cuentas.exceptions.CustomHandlerException;
import com.devsu.cuentas.feign.UsuarioFeignClient;
import com.devsu.cuentas.model.Cuenta;
import com.devsu.cuentas.repository.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaServiceImplTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private UsuarioFeignClient usuarioFeignClient;

    @InjectMocks
    private CuentaServiceImpl cuentaService;

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta(1L, 1L, 123456789L, null, 1000.0, 1000.0, null, null);
    }

    @Test
    @DisplayName("Test guardarCuenta when valid Cuenta then return saved Cuenta")
    void testGuardarCuentaWhenValidCuentaThenReturnSavedCuenta() {
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);
        when(usuarioFeignClient.añadirCuenta(anyLong(), anyLong())).thenReturn(Collections.emptyMap());

        Cuenta savedCuenta = cuentaService.guardarCuenta(cuenta);

        assertThat(savedCuenta).isNotNull();
        assertThat(savedCuenta.getId()).isEqualTo(cuenta.getId());
        verify(cuentaRepository).save(cuenta);
        verify(usuarioFeignClient).añadirCuenta(cuenta.getIdCliente(), cuenta.getId());
    }

    @Test
    @DisplayName("Test buscarCuentaPorId when valid Id then return Cuenta")
    void testBuscarCuentaPorIdWhenValidIdThenReturnCuenta() {
        when(cuentaRepository.findById(anyLong())).thenReturn(Optional.of(cuenta));

        Cuenta foundCuenta = cuentaService.buscarCuentaPorId(1L);

        assertThat(foundCuenta).isNotNull();
        assertThat(foundCuenta.getId()).isEqualTo(cuenta.getId());
        verify(cuentaRepository).findById(1L);
    }

    @Test
    @DisplayName("Test actualizarCuenta when valid Cuenta then return updated Cuenta")
    void testActualizarCuentaWhenValidCuentaThenReturnUpdatedCuenta() {
        when(cuentaRepository.findById(anyLong())).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        Cuenta updatedCuenta = cuentaService.actualizarCuenta(cuenta);

        assertThat(updatedCuenta).isNotNull();
        assertThat(updatedCuenta.getId()).isEqualTo(cuenta.getId());
        verify(cuentaRepository).findById(cuenta.getId());
        verify(cuentaRepository).save(cuenta);
    }

    @Test
    @DisplayName("Test obtenerTodasLasCuentas when Cuentas exist then return Cuentas")
    void testObtenerTodasLasCuentasWhenCuentasExistThenReturnCuentas() {
        when(cuentaRepository.findAll()).thenReturn(List.of(cuenta));

        List<Cuenta> cuentas = cuentaService.obtenerTodasLasCuentas();

        assertThat(cuentas).isNotEmpty();
        assertThat(cuentas).hasSize(1);
        verify(cuentaRepository).findAll();
    }

    @Test
    @DisplayName("Test obtenerCuentaPoridCliente when valid idCliente and Cuentas exist then return Cuentas")
    void testObtenerCuentaPoridClienteWhenValidIdClienteAndCuentasExistThenReturnCuentas() {
        when(cuentaRepository.findCuentaByIdCliente(anyLong())).thenReturn(List.of(cuenta));

        List<Cuenta> cuentas = cuentaService.obtenerCuentaPoridCliente(1L);

        assertThat(cuentas).isNotEmpty();
        assertThat(cuentas).hasSize(1);
        verify(cuentaRepository).findCuentaByIdCliente(1L);
    }

    @Test
    @DisplayName("Test eliminarCuenta when valid Id and Cuenta exists then return true")
    void testEliminarCuentaWhenValidIdAndCuentaExistsThenReturnTrue() {
        when(cuentaRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(cuentaRepository).deleteById(anyLong());

        boolean result = cuentaService.eliminarCuenta(1L);

        assertThat(result).isTrue();
        verify(cuentaRepository).existsById(1L);
        verify(cuentaRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Test actualizarMonto when valid idCuenta, monto and TipoMovimiento and Cuenta exists then return updated Cuenta")
    void testActualizarMontoWhenValidIdCuentaMontoAndTipoMovimientoAndCuentaExistsThenReturnUpdatedCuenta() {
        when(cuentaRepository.findById(anyLong())).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        Cuenta updatedCuenta = cuentaService.actualizarMonto(1L, 500.0, TipoMovimiento.DEPOSITO);

        assertThat(updatedCuenta).isNotNull();
        assertThat(updatedCuenta.getSaldoActual()).isEqualTo(1500.0);
        verify(cuentaRepository).findById(1L);
        verify(cuentaRepository).save(cuenta);
    }
}