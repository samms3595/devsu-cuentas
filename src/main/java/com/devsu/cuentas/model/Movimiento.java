package com.devsu.cuentas.model;

import com.devsu.cuentas.enums.TipoMovimiento;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Movimiento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "La fecha no puede ser nula")
    private LocalDate fecha;

    private double saldo;

    private double valor;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El tipo de movimiento no puede ser nulo")
    private TipoMovimiento tipoMovimiento;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "cuenta_id")
    @NotNull(message = "La cuenta asociada no puede ser nula")
    private Cuenta cuenta;
}
