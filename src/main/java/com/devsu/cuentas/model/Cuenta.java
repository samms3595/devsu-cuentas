package com.devsu.cuentas.model;

import com.devsu.cuentas.enums.Estado;
import com.devsu.cuentas.enums.TipoCuenta;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cuenta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "El id del cliente no puede ser nulo")
    private Long idCliente;

    @Column(unique = true)
    @NotNull(message = "El número de cuenta no puede ser nulo")
    private Long numeroCuenta;

    @NotNull(message = "El tipo de cuenta no puede ser nulo")
    private TipoCuenta tipoCuenta;

    @NotNull(message = "El saldo inicial no puede ser nulo")
    @Min(value = 0, message = "El saldo inicial no puede ser negativo")
    private double saldoInicial;

    private double saldoActual;

    private Estado estado;

    @OneToMany(mappedBy = "cuenta", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Movimiento> movimientos;

}
