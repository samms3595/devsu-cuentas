package com.devsu.cuentas.controller;

import com.devsu.cuentas.dto.MovimientoDTO;
import com.devsu.cuentas.dto.ReporteDTO;
import com.devsu.cuentas.model.Movimiento;
import com.devsu.cuentas.service.MovimientoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MovimientoControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private MovimientoService movimientoService;

    @InjectMocks
    private MovimientoController movimientoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(movimientoController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGuardarMovimientoWhenValidMovimientoThenReturn200AndMovimiento() throws Exception {
        MovimientoDTO movimientoDTO = new MovimientoDTO(LocalDate.now(), 1000.0, 500.0, null, 1L);
        Movimiento movimiento = new Movimiento(1L, LocalDate.now(), 1000.0, 500.0, null, null);

        when(movimientoService.guardarMovimiento(any(MovimientoDTO.class))).thenReturn(movimiento);

        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movimientoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(movimiento)));
    }

    @Test
    void testBuscarMovimientoPorIdWhenValidIdThenReturn200AndMovimiento() throws Exception {
        Long id = 1L;
        Movimiento movimiento = new Movimiento(id, LocalDate.now(), 1000.0, 500.0, null, null);

        when(movimientoService.buscarMovimientoPorId(id)).thenReturn(movimiento);

        mockMvc.perform(get("/movimientos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(movimiento)));
    }

    @Test
    void testListarMovimientosPorIdCuentaWhenValidIdCuentaThenReturn200AndMovimientos() throws Exception {
        Long idCuenta = 1L;
        List<Movimiento> movimientos = Arrays.asList(
                new Movimiento(1L, LocalDate.now(), 1000.0, 500.0, null, null),
                new Movimiento(2L, LocalDate.now(), 2000.0, 300.0, null, null)
        );

        when(movimientoService.listarMovimientosPorIdCuenta(idCuenta)).thenReturn(movimientos);

        mockMvc.perform(get("/movimientos/cuenta/{idCuenta}", idCuenta))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(movimientos)));
    }

    @Test
    void testEliminarMovimientoWhenValidIdThenReturn204() throws Exception {
        Long id = 1L;

        when(movimientoService.eliminarMovimiento(id)).thenReturn(true);

        mockMvc.perform(delete("/movimientos/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void testActualizarMovimientoWhenValidIdAndMovimientoThenReturn200AndMovimiento() throws Exception {
        Long id = 1L;
        Movimiento movimiento = new Movimiento(id, LocalDate.now(), 1000.0, 500.0, null, null);

        when(movimientoService.actualizarMovimiento(any(Movimiento.class))).thenReturn(movimiento);

        mockMvc.perform(put("/movimientos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movimiento)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(movimiento)));
    }

    @Test
    void testListarMovimientosPorIdClienteWhenValidIdClienteThenReturn200AndMovimientos() throws Exception {
        Long idCliente = 1L;
        List<Movimiento> movimientos = Arrays.asList(
                new Movimiento(1L, LocalDate.now(), 1000.0, 500.0, null, null),
                new Movimiento(2L, LocalDate.now(), 2000.0, 300.0, null, null)
        );

        when(movimientoService.encontrarMovimientoPorIdCliente(idCliente)).thenReturn(movimientos);

        mockMvc.perform(get("/movimientos/cliente/{idCliente}", idCliente))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(movimientos)));
    }

    @Test
    void testBuscarMovimientoPorFechaYIdClienteWhenValidFechaIAndFechaFAndIdClienteThenReturn200AndMovimientos() throws Exception {
        String fechaI = "2023-01-01";
        String fechaF = "2023-01-31";
        Long idCliente = 1L;
        List<ReporteDTO> reporteDTOS = Arrays.asList(
                // Assuming ReporteDTO has a constructor that accepts parameters for its fields
                // Replace with appropriate constructor or builder pattern if necessary
                new ReporteDTO(LocalDate.parse(fechaI), "Cliente1", 123456L, null, null, 1000.0, 500.0, 1500.0),
                new ReporteDTO(LocalDate.parse(fechaF), "Cliente2", 654321L, null, null, 2000.0, 300.0, 2300.0)
        );

        when(movimientoService.buscarMovimientosPorFechaYIdCliente(LocalDate.parse(fechaI), LocalDate.parse(fechaF), idCliente))
                .thenReturn(reporteDTOS);

        mockMvc.perform(get("/movimientos/reporte")
                        .param("fechaI", fechaI)
                        .param("fechaF", fechaF)
                        .param("idCliente", idCliente.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(reporteDTOS)));
    }
}