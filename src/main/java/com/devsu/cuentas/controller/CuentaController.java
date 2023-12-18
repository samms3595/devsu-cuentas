package com.devsu.cuentas.controller;


import com.devsu.cuentas.enums.TipoMovimiento;
import com.devsu.cuentas.model.ApiError;
import com.devsu.cuentas.model.Cuenta;
import com.devsu.cuentas.service.CuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    @Autowired
    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @Operation(summary = "Crea una nueva cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta creada con éxito",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = Cuenta.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<Cuenta> guardarCuenta(@Valid @RequestBody Cuenta cuenta) {
        return ResponseEntity.ok(cuentaService.guardarCuenta(cuenta));
    }

    @Operation(summary = "Busca una cuenta por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busqueda de Cuenta",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = Cuenta.class))),
            @ApiResponse(responseCode = "400", description = "No hay datos que mostrar",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> buscarCuentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.buscarCuentaPorId(id));
    }

    @Operation(summary = "Actualiza una cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta actualizada con éxito",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = Cuenta.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping
    public ResponseEntity<Cuenta> actualizarCuenta(@Valid @RequestBody Cuenta cuenta) {
        return ResponseEntity.ok(cuentaService.actualizarCuenta(cuenta));
    }

    @Operation(summary = "Obtiene una lista de cuentas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cuentas",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = Cuenta.class))),
            @ApiResponse(responseCode = "400", description = "No hay datos que mostrar",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping
    public ResponseEntity<List<Cuenta>> obtenerTodasLasCuentas() {
        return ResponseEntity.ok(cuentaService.obtenerTodasLasCuentas());
    }

    @Operation(summary = "Obtiene una lista de cuentas por el id del cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lista de cuentas por el cliente",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = Cuenta.class))),
            @ApiResponse(responseCode = "400", description = "No hay datos que mostrar",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Cuenta>> obtenerCuentaPoridCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(cuentaService.obtenerCuentaPoridCliente(idCliente));
    }

    @Operation(summary = "Elimina una cuenta mediante el ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta eliminada con éxito",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = Cuenta.class))),
            @ApiResponse(responseCode = "400", description = "No se encontraron datos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCuenta(@PathVariable Long id) {
        if (cuentaService.eliminarCuenta(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
