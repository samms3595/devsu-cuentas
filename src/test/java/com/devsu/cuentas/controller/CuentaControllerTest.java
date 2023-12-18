package com.devsu.cuentas.controller;

import com.devsu.cuentas.enums.TipoMovimiento;
import com.devsu.cuentas.model.Cuenta;
import com.devsu.cuentas.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CuentaControllerTest {

    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private CuentaController cuentaController;

    private Cuenta cuenta;
    private List<Cuenta> cuentaList;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta(1L, 1L, 123456789L, null, 1000.0, 1000.0, null, null);
        cuentaList = Arrays.asList(cuenta);
    }

    @Test
    void testGuardarCuentaWhenValidCuentaThenReturnCuenta() {
        when(cuentaService.guardarCuenta(any(Cuenta.class))).thenReturn(cuenta);

        ResponseEntity<Cuenta> response = cuentaController.guardarCuenta(cuenta);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cuenta, response.getBody());
        verify(cuentaService, times(1)).guardarCuenta(cuenta);
    }

    @Test
    void testBuscarCuentaPorIdWhenValidIdThenReturnCuenta() {
        when(cuentaService.buscarCuentaPorId(anyLong())).thenReturn(cuenta);

        ResponseEntity<Cuenta> response = cuentaController.buscarCuentaPorId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cuenta, response.getBody());
        verify(cuentaService, times(1)).buscarCuentaPorId(1L);
    }

    @Test
    void testActualizarCuentaWhenValidCuentaThenReturnCuenta() {
        when(cuentaService.actualizarCuenta(any(Cuenta.class))).thenReturn(cuenta);

        ResponseEntity<Cuenta> response = cuentaController.actualizarCuenta(cuenta);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cuenta, response.getBody());
        verify(cuentaService, times(1)).actualizarCuenta(cuenta);
    }

    @Test
    void testObtenerTodasLasCuentasThenReturnCuentas() {
        when(cuentaService.obtenerTodasLasCuentas()).thenReturn(cuentaList);

        ResponseEntity<List<Cuenta>> response = cuentaController.obtenerTodasLasCuentas();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cuentaList, response.getBody());
        verify(cuentaService, times(1)).obtenerTodasLasCuentas();
    }

    @Test
    void testObtenerCuentaPoridClienteWhenValidIdClienteThenReturnCuentas() {
        when(cuentaService.obtenerCuentaPoridCliente(anyLong())).thenReturn(cuentaList);

        ResponseEntity<List<Cuenta>> response = cuentaController.obtenerCuentaPoridCliente(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cuentaList, response.getBody());
        verify(cuentaService, times(1)).obtenerCuentaPoridCliente(1L);
    }

    @Test
    void testEliminarCuentaWhenValidIdThenReturnOk() {
        when(cuentaService.eliminarCuenta(anyLong())).thenReturn(true);

        ResponseEntity<?> response = cuentaController.eliminarCuenta(1L);

        assertEquals(200, response.getStatusCodeValue());
        verify(cuentaService, times(1)).eliminarCuenta(1L);
    }

}