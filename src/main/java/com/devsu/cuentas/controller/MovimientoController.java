package com.devsu.cuentas.controller;

import com.devsu.cuentas.dto.MovimientoDTO;
import com.devsu.cuentas.dto.ReporteDTO;
import com.devsu.cuentas.model.ApiError;
import com.devsu.cuentas.model.Movimiento;
import com.devsu.cuentas.service.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    @Autowired
    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @Operation(summary = "Guarda un nuevo movimiento", responses = {
            @ApiResponse(responseCode = "200", description = "Movimiento guardado con éxito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movimiento.class))),
            @ApiResponse(responseCode = "400", description = "Datos del movimiento inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<Movimiento> guardarMovimiento(@Valid @RequestBody MovimientoDTO movimientoGuardar) {
        var movimiento = movimientoService.guardarMovimiento(movimientoGuardar);
        return movimiento != null ? ResponseEntity.ok(movimiento) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar Movimiento", responses = {
            @ApiResponse(responseCode = "200", description = "Busqueda de Movimiento por ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movimiento.class))),
            @ApiResponse(responseCode = "400", description = "Datos del movimiento No encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Movimiento> buscarMovimientoPorId(@PathVariable Long id) {
        var movimiento = movimientoService.buscarMovimientoPorId(id);
        return movimiento != null ? ResponseEntity.ok(movimiento) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar Movimientos  por Id de Cuenta", responses = {
            @ApiResponse(responseCode = "200", description = "Busqueda de Movimiento por ID de Cuenta",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movimiento.class))),
            @ApiResponse(responseCode = "400", description = "Datos de los movimiento No encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<List<Movimiento>> listarMovimientosPorIdCuenta(@PathVariable Long idCuenta) {
        var movimientos = movimientoService.listarMovimientosPorIdCuenta(idCuenta);
        return ResponseEntity.ok(movimientos);
    }

    @Operation(summary = "Borrar Movimiento por Id", responses = {
            @ApiResponse(responseCode = "200", description = "Eliminacion de Movimiento por ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movimiento.class))),
            @ApiResponse(responseCode = "400", description = "Datos del movimiento No encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMovimiento(@PathVariable Long id) {
        var resultado = movimientoService.eliminarMovimiento(id);
        return resultado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Actualizacion Movimiento por Id", responses = {
            @ApiResponse(responseCode = "200", description = "Actualizacion de Movimiento por ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movimiento.class))),
            @ApiResponse(responseCode = "400", description = "Datos del movimiento No encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Movimiento> actualizarMovimiento(@PathVariable Long id, @RequestBody Movimiento movimiento) {
        movimiento.setId(id);
        var movimientoActualizado = movimientoService.actualizarMovimiento(movimiento);
        return movimientoActualizado != null ? ResponseEntity.ok(movimientoActualizado) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Lista de Movimientos por el Id del Cliente", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de Movimientos por el Id del Cliente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Movimiento.class))),
            @ApiResponse(responseCode = "400", description = "Datos del movimiento No encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<Movimiento>> listarMovimientosPorIdCliente(@PathVariable Long id) {
        var listaMovimientos = movimientoService.encontrarMovimientoPorIdCliente(id);
        return ResponseEntity.ok(listaMovimientos);
    }

    @Operation(summary = "Busca movimientos por fecha", responses = {
            @ApiResponse(responseCode = "200", description = "Movimientos encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReporteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Fecha inválida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "400", description = "Datos no encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/reporte")
    public ResponseEntity<List<ReporteDTO>> buscarMovimientoPorFechaYIdCliente(@RequestParam String fechaI, @RequestParam String fechaF, @RequestParam Long idCliente){
        LocalDate fechaIParse = LocalDate.parse(fechaI);
        LocalDate fechaFParse = LocalDate.parse(fechaF);

        var listaMovimientos = movimientoService.buscarMovimientosPorFechaYIdCliente(fechaIParse, fechaFParse, idCliente);
        return ResponseEntity.ok(listaMovimientos);
    }
}
