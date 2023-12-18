package com.devsu.cuentas.enums;

public enum Genero {

    MASCULINO("Masculino"), FEMENINO("Femenino"), NO_ESPECIFICA("otro");

    private final String value;

    Genero(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
