package com.devsu.cuentas.dto;

import com.devsu.cuentas.enums.TipoMovimiento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoDTO {

    private LocalDate fecha;
    private double saldo;
    private double valor;
    private TipoMovimiento tipoMovimiento;
    private Long cuentaId;

}
