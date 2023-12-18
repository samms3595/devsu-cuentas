package com.devsu.cuentas.model;

import com.devsu.cuentas.enums.Genero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    private Long id;
    private String nombre;
    private String apellidos;
    private String identificacion;
    private Genero genero;
    private Integer edad;
    private String direccion;
    private String telefono;
}
