package com.devsu.cuentas.dto;

import com.devsu.cuentas.enums.Estado;
import com.devsu.cuentas.enums.TipoCuenta;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteDTO {

    private LocalDate fecha;

    private String nombreCliente;

    private Long numeroCuenta;

    @Enumerated(EnumType.STRING)
    private TipoCuenta tipoCuenta;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private double saldoInicial;

    private double valor;

    private double saldo;

}
