package com.devsu.cuentas.model;

import com.devsu.cuentas.enums.Estado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente extends Persona {

    private Long clientId;
    private Set<Long> cuentas = new HashSet<>();
    private String password;
    private Estado estado;
}
